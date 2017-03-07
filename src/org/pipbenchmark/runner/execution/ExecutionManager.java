package org.pipbenchmark.runner.execution;

import java.util.*;

import org.pipbenchmark.runner.benchmarks.*;
import org.pipbenchmark.runner.config.*;
import org.pipbenchmark.runner.results.*;

public class ExecutionManager {
	protected ConfigurationManager _configuration;
	protected ResultsManager _results;
	
	private final Object _syncRoot = new Object();
    private List<IExecutionListener> _updatedListeners = new ArrayList<IExecutionListener>();
    private ExecutionStrategy _strategy = null;

    public ExecutionManager(ConfigurationManager configuration, ResultsManager results) {
    	_configuration = configuration;
    	_results = results;
    }
    
    public boolean isRunning() {
        return _strategy != null;
    }

    public void start(List<BenchmarkInstance> benchmarks) {
        if (benchmarks == null || benchmarks.size() == 0)
            throw new NullPointerException("There are no benchmarks to execute");

        if (_strategy != null)
            stop();

        _results.clear();
        notifyUpdated(ExecutionState.Running);
        
        // Create requested execution strategy
        if (_configuration.getExecutionType() == ExecutionType.Sequential)
            _strategy = new SequencialExecutionStrategy(_configuration, _results, benchmarks);
        else
            _strategy = new ProportionalExecutionStrategy(_configuration, _results, benchmarks);

        _strategy.start();
    }

    public void stop() {
        if (_strategy != null) {
        	synchronized (_syncRoot) {
        		if (_strategy != null) {
		            _strategy.stop();            
		            _strategy = null;

	                notifyUpdated(ExecutionState.Completed);
    			}
        	}
        }
    }

    public void run(List<BenchmarkInstance> benchmarks) {
    	start(benchmarks);    	
    	try {
    		while (isRunning()) {
    			Thread.sleep(500);
    		}
    	} catch (InterruptedException ex) {
    		// Ignore...
    	} finally { 
    		stop();
    	}
    }

    public void addUpdatedListener(IExecutionListener listener) {
    	_updatedListeners.add(listener);
    }
    
    public void removeUpdatedListener(IExecutionListener listener) {
    	_updatedListeners.remove(listener);
    }

    public void notifyUpdated(ExecutionState state) {
    	for (int index = 0; index < _updatedListeners.size(); index++) {
    		try {
    			IExecutionListener listener = _updatedListeners.get(index);
    			listener.onStateUpdated(state);
    		} catch (Exception ex) {
    			// Ignore and send a message to the next listener.
    		}
    	}
    }

}
