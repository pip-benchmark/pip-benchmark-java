package org.pipbenchmark.runner.execution;

import java.util.*;

import org.pipbenchmark.runner.benchmarks.*;
import org.pipbenchmark.runner.config.*;
import org.pipbenchmark.runner.results.*;

public class ProportionalExecutionStrategy extends ExecutionStrategy {
    private boolean _running = false;
    private Thread[] _threads = null;
    private Thread _controlThread = null;
    private double _ticksPerTransaction = 0;
    private ResultAggregator _aggregator;

    public ProportionalExecutionStrategy(ConfigurationManager configuration, ResultsManager results, 
    	ExecutionManager execution, List<BenchmarkInstance> benchmarks) {
        
    	super(configuration, results, execution, benchmarks);

        _aggregator = new ResultAggregator(results, benchmarks);
    }

    @Override
    public boolean isStopped() {
    	return !_running;
    }
    
    @Override
    public void start() {
    	if (_running) return;
    	
        _running = true;
        _aggregator.start();

        calculateProportionalRanges();
        
        if (_configuration.getMeasurementType() == MeasurementType.Peak)
            _ticksPerTransaction = 0;
        else
            _ticksPerTransaction = 1000.0 / _configuration.getNominalRate() 
            	* _configuration.getNumberOfThreads();

        // Initialize test suites
        for (BenchmarkSuiteInstance suite : _suites)
            suite.setUp(new ExecutionContext(suite, _aggregator, this));

        // Start benchmarking threads
        _threads = new Thread[_configuration.getNumberOfThreads()];
        for (int index = 0; index < _configuration.getNumberOfThreads(); index++) {
            _threads[index] = new Thread(
            	new Runnable() {
            		@Override
            		public final void run() { execute(); }
            	});
            _threads[index].setName(String.format("Benchmarking Thread #%d/%d", 
                index, _configuration.getNumberOfThreads()));
            //_threads[index].setPriority(ThreadPriority.Highest);
            _threads[index].start();
        }

        // Start control thread
        _controlThread = new Thread(
        	new Runnable() {
        		@Override
        		public final void run() { control(); }
        	});
        _controlThread.setName("Benchmarking Control Thread");
        _controlThread.start();
    }

    @Override
    public void stop() {
    	if (_running) {
    		synchronized(_syncRoot) {
    			if (_running) {
			        _running = false;
			        _aggregator.stop();
			
			        if (_execution != null)
			        	_execution.stop();
			        
			        // Stop benchmarking threads
			        if (_threads != null) {
			            for (int index = 0; index < _threads.length; index++) {
			                _threads[index].interrupt();
			                _threads[index] = null;
			            }
			            _threads = null;
			        }
			
			        for (BenchmarkSuiteInstance suite : _suites)
			            suite.tearDown();
    			}
    		}
    	}
    }

    private void calculateProportionalRanges() {
        double proportionSum = 0;
        for (BenchmarkInstance benchmark : _activeBenchmarks) {
        	proportionSum += !benchmark.isPassive() ? benchmark.getProportion() : 0;
        }

        double startRange = 0;
        for (BenchmarkInstance benchmark : _activeBenchmarks) {
    		double normalizedProportion = ((double)benchmark.getProportion()) / proportionSum;
            benchmark.setStartRange(startRange);
            benchmark.setEndRange(startRange + normalizedProportion);
            startRange += normalizedProportion;
        }
    }

    private void control() {
        // Wait for set duration (in seconds)
        try {
        	Thread.sleep(_configuration.getDuration() * 1000);
        } catch (InterruptedException ex) {
        	// Ignore
        } finally {
		    _controlThread = null;
	    
	        stop();
        }
    }

    private void executeBenchmark(BenchmarkInstance benchmark) throws InterruptedException {
        try {
            benchmark.execute();
        } catch (Exception ex) {
        	if (ex instanceof InterruptedException) {
        		// Ignore...
        	} else if (_configuration.isForceContinue()) {
                _aggregator.reportError(ex);
        	} else
                throw ex;
        }
    }    

    private void execute() {
        Random randomGenerator = new Random();
        long lastExecutedTicks = System.currentTimeMillis();
        int benchmarkCount = _activeBenchmarks.size();
        BenchmarkInstance firstBenchmark = _activeBenchmarks.size() == 1 
        	? _activeBenchmarks.get(0) : null;

        try {
            while (_running) {
                if (_configuration.getMeasurementType() == MeasurementType.Nominal) {
                    double ticksToNextTransaction = _ticksPerTransaction
                        - (System.currentTimeMillis() - lastExecutedTicks);
                    
                    // Wait to ensure nominal transaction rate
                    if (ticksToNextTransaction > 0)
                        Thread.sleep((int)ticksToNextTransaction);
                }

                if (benchmarkCount == 1) {
                    executeBenchmark(firstBenchmark);
                    lastExecutedTicks = System.currentTimeMillis();
                    _aggregator.incrementCounter(1, lastExecutedTicks);
                } else if (benchmarkCount == 0) {
                    Thread.sleep(500);
                } else {
                    double selector = randomGenerator.nextDouble();
                    for (int index = 0; index < _activeBenchmarks.size(); index++) {
                        BenchmarkInstance benchmark = _activeBenchmarks.get(index);
                        if (benchmark.withinRange(selector)) {
                            lastExecutedTicks = System.currentTimeMillis();
                            executeBenchmark(benchmark);
                            _aggregator.incrementCounter(1, lastExecutedTicks);
                            break;
                        }
                    }
                }
            }
        } catch (InterruptedException ex) {
            // Ignore the exception...
        }
    }
}
