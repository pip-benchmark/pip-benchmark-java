package org.pipbenchmark.runner.execution;

import java.util.*;

import org.pipbenchmark.runner.benchmarks.*;
import org.pipbenchmark.runner.config.*;
import org.pipbenchmark.runner.results.*;

public class ProportionalExecutionStrategy extends ExecutionStrategy {
    private boolean _running = false;
    private Thread[] _threads = null;
    private double _ticksPerTransaction = 0;

    public ProportionalExecutionStrategy(ConfigurationManager configuration,
		ExecutionManager parentProcess, List<BenchmarkInstance> benchmarks) {
        
    	super(configuration, parentProcess, benchmarks);
        calculateExecutionTriggers();
    }

    private void calculateExecutionTriggers() {
        double proportionSum = 0;
        for (BenchmarkInstance benchmark : getBenchmarks()) {
        	proportionSum += !benchmark.isPassive() ? benchmark.getProportion() : 0;
        }

        double startExecutionTrigger = 0;
        for (BenchmarkInstance benchmark : getBenchmarks()) {
        	if (benchmark.isPassive()) {
	            benchmark.setStartExecutionTrigger(0);
	            benchmark.setEndExecutionTrigger(0);
        	} else { 
        		double normalizedProportion = ((double)benchmark.getProportion()) / proportionSum;
	            benchmark.setStartExecutionTrigger(startExecutionTrigger);
	            benchmark.setEndExecutionTrigger(startExecutionTrigger + normalizedProportion);
	            startExecutionTrigger += normalizedProportion;
        	}
        }
    }

    @Override
    public void start() {
        initializeMeasurements();

        if (_configuration.getMeasurementType() == MeasurementType.Peak)
            _ticksPerTransaction = 0;
        else
            _ticksPerTransaction = 1000.0 / _configuration.getNominalRate() 
            	* _configuration.getNumberOfThreads();

        // Initialize test suites
        for (BenchmarkSuiteInstance suite : getSuites())
            suite.setUp(new ExecutionContext(this, suite));

        _running = true;

        // Start benchmarking threads
        _threads = new Thread[_configuration.getNumberOfThreads()];
        for (int index = 0; index < _configuration.getNumberOfThreads(); index++) {
            _threads[index] = new Thread(
            	new Runnable() {
            		@Override
            		public final void run() { performBenchmarking(); }
            	});
            _threads[index].setName(String.format("Benchmarking Thread #%d/%d", 
                index, _configuration.getNumberOfThreads()));
            //_threads[index].setPriority(ThreadPriority.Highest);
            _threads[index].start();
        }
    }

    @Override
    public void stop() {
        _running = false;

        // Stop benchmarking threads
        if (_threads != null) {
            for (int index = 0; index < _threads.length; index++) {
                _threads[index].interrupt();
                _threads[index] = null;
            }
            _threads = null;
        }

        for (BenchmarkSuiteInstance suite : getSuites()) {
            suite.tearDown();
        }
    }

    @Override
    public List<BenchmarkResult> getResults() {
        List<BenchmarkResult> results = new ArrayList<BenchmarkResult>();

        if (getCurrentResult() != null)
            results.add(getCurrentResult());

        return results;
    }

    private void performBenchmarking() {
        Random randomGenerator = new Random();
        long lastExecutedTicks = System.currentTimeMillis();
        int benchmarkCount = getBenchmarks().size();
        BenchmarkInstance firstBenchmark = getBenchmarks().size() == 1 
        	? getBenchmarks().get(0) : null;

        notifyResultUpdate(ExecutionState.Starting);

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
                    firstBenchmark.execute();
                    lastExecutedTicks = System.currentTimeMillis();
                    incrementCounter(1, lastExecutedTicks);
                } else if (benchmarkCount == 0) {
                    Thread.sleep(500);
                } else {
                    double selector = randomGenerator.nextDouble();
                    for (int index = 0; index < getBenchmarks().size(); index++) {
                        BenchmarkInstance benchmark = getBenchmarks().get(index);
                        if (benchmark.isTriggered(selector)) {
                            lastExecutedTicks = System.currentTimeMillis();
                            executeBenchmark(benchmark);
                            incrementCounter(1, lastExecutedTicks);
                            break;
                        }
                    }
                }
            }
        } catch (InterruptedException ex) {
            // Ignore the exception...
        } finally {
            notifyResultUpdate(ExecutionState.Completed);
        }
    }
}
