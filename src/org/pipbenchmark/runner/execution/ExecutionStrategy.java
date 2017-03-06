package org.pipbenchmark.runner.execution;

import java.util.*;

import org.pipbenchmark.runner.benchmarks.*;
import org.pipbenchmark.runner.config.*;
import org.pipbenchmark.runner.results.*;

public abstract class ExecutionStrategy {
    private final static int MaxErrorCount = 1000;

    protected ConfigurationManager _configuration;
    protected final Object _syncRoot = new Object();
    protected ExecutionManager _process;
    protected List<BenchmarkInstance> _benchmarks;
    protected List<BenchmarkSuiteInstance> _suites;

    private double _transactionCounter = 0;

    private BenchmarkResult _currentResult = null;

    private TransactionMeter _transactionMeter;
    private CpuLoadMeter _cpuLoadMeter;
    private MemoryUsageMeter _memoryUsageMeter;

    protected ExecutionStrategy(ConfigurationManager configuration,
		ExecutionManager parentProcess, List<BenchmarkInstance> benchmarks) {
        
    	_configuration = configuration;
    	_process = parentProcess;
        _benchmarks = benchmarks;
        _suites = getAllSuitesFromBenchmarks(benchmarks);

        _cpuLoadMeter = new CpuLoadMeter();
        _transactionMeter = new TransactionMeter();
        _memoryUsageMeter = new MemoryUsageMeter();
    }

    private List<BenchmarkSuiteInstance> getAllSuitesFromBenchmarks(List<BenchmarkInstance> benchmarks) {
        List<BenchmarkSuiteInstance> suites = new ArrayList<BenchmarkSuiteInstance>();
        for (BenchmarkInstance benchmark : benchmarks) {
            BenchmarkSuiteInstance suite = benchmark.getSuite();
            if (!suites.contains(suite))
                suites.add(suite);
        }
        return suites;
    }

    protected Object getSyncRoot() {
        return _syncRoot;
    }

    public ExecutionManager getProcess() {
        return _process;
    }

    public List<BenchmarkInstance> getBenchmarks() {
        return _benchmarks;
    }

    public List<BenchmarkSuiteInstance> getSuites() {
        return _suites;
    }

    protected BenchmarkResult getCurrentResult() {
        return _currentResult;
    }

    public abstract void start();
    public abstract void stop();
    public abstract List<BenchmarkResult> getResults();

    public void sendMessage(String message) {
    	_process.notifyMessageSent(message);
    }
    
    protected void initializeMeasurements() {
        _currentResult = new BenchmarkResult();
        _currentResult.setStartTime(System.currentTimeMillis());

        _transactionCounter = 0;
        _transactionMeter.reset();
        _cpuLoadMeter.reset();
        _memoryUsageMeter.reset();
    }

    public void incrementCounter(int increment) {
        incrementCounter(increment, System.currentTimeMillis());
    }

    protected void incrementCounter(int increment, long currentTicks) {
        _transactionCounter += increment;

        // If it's less then a second then wait
        long measureInterval = currentTicks - _transactionMeter.getLastMeasuredTicks();
        if (measureInterval >= 1000 && _currentResult != null)
        {
            synchronized (_syncRoot) {
                measureInterval = currentTicks - _transactionMeter.getLastMeasuredTicks();
                if (measureInterval >= 1000) {
                    // Perform measurements
                    _transactionMeter.setTransactionCounter(_transactionCounter);
                    _transactionCounter = 0;
                    _transactionMeter.measure();
                    _cpuLoadMeter.measure();
                    _memoryUsageMeter.measure();

                    // Store measurement results
                    _currentResult.setElapsedTime(System.currentTimeMillis() - _currentResult.getStartTime());
                    _currentResult.setPerformanceMeasurement(_transactionMeter.getMeasurement());
                    _currentResult.setCpuLoadMeasurement(_cpuLoadMeter.getMeasurement());
                    _currentResult.setMemoryUsageMeasurement(_memoryUsageMeter.getMeasurement());

                    notifyResultUpdate(ExecutionState.Running);
                }
            }
        }
    }

    public void reportError(String errorMessage) {
    	synchronized (_syncRoot) {
    		if (_currentResult.getErrors().size() < MaxErrorCount)
    			_currentResult.getErrors().add(errorMessage);
    	}
    	
    	_process.notifyErrorReported(errorMessage);
    }
    
    protected void executeBenchmark(BenchmarkInstance benchmark) throws InterruptedException {
        try {
            benchmark.execute();
        } catch (Exception ex) {
        	if (ex instanceof InterruptedException) {
        		// Ignore...
        	} else if (_configuration.isForceContinue()) {
                reportError(ex.getMessage());
        	} else
                throw ex;
        }
    }    
    
    protected void notifyResultUpdate(ExecutionState status) {
        getProcess().notifyResultUpdate(status, _currentResult);
    }
}
