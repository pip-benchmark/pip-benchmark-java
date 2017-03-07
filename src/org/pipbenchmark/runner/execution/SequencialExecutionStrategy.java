package org.pipbenchmark.runner.execution;

import java.util.*;

import org.pipbenchmark.runner.benchmarks.*;
import org.pipbenchmark.runner.config.*;
import org.pipbenchmark.runner.results.*;

public class SequencialExecutionStrategy extends ExecutionStrategy {
    private boolean _running = false;
    private Thread _controlThread = null;

    public SequencialExecutionStrategy(ConfigurationManager configuration, ResultsManager results, 
    	ExecutionManager execution, List<BenchmarkInstance> benchmarks) {
        
    	super(configuration, results, execution, benchmarks);
    }

    @Override
    public void start() {
        if (_configuration.getDuration() <= 0) 
            throw new NullPointerException("Duration was not set");

        if (_running) return;
        _running = true;
        
        // Start control thread
        _controlThread = new Thread(
        	new Runnable() {
        		@Override
        		public final void run() { execute(); }
        	});
        _controlThread.setName("Benchmarking Control Thread");
        _controlThread.start();
    }

    @Override
    public void stop() {
    	if (_running) {
    		synchronized (_syncRoot) {
    			if (_running) {
			        _running = false;
			
			        if (_execution != null)
			        	_execution.stop();
			        
			        // Stop control thread
			        if (_controlThread != null) {
			            _controlThread.interrupt();
			            _controlThread = null;
			        }
    			}
    		}
    	}
    }

    private void execute() {
        ProportionalExecutionStrategy current = null;

        try {
            for (BenchmarkInstance benchmark : _benchmarks) {
            	// Skip if benchmarking was interrupted
            	if (_running == false) break;
            	
            	// Start embedded strategy
            	List<BenchmarkInstance> benchmarks = new ArrayList<BenchmarkInstance>();
            	benchmarks.add(benchmark);
            	current = new ProportionalExecutionStrategy(_configuration, _results, null, benchmarks);            	
            	current.start();
            	
            	Thread.sleep(_configuration.getDuration() * 1000);
            	
            	// Stop embedded strategy
            	current.stop();
            }

            _controlThread = null;
        } catch (InterruptedException ex) {
        	// Ignore
        } finally {
        	if (current != null) current.stop();
        }
    }

}
