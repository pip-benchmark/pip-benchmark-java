package org.pipbenchmark.runner.execution;

import java.util.List;

import org.pipbenchmark.runner.benchmarks.*;
import org.pipbenchmark.runner.results.*;

public class ResultAggregator {
    private static final int MaxErrorCount = 1000;

    private ResultsManager _results;
    private final Object _syncRoot = new Object();
    private List<BenchmarkInstance> _benchmarks;
    private double _transactionCounter = 0;
    private BenchmarkResult _result = null;
    private TransactionMeter _transactionMeter;
    private CpuLoadMeter _cpuLoadMeter;
    private MemoryUsageMeter _memoryUsageMeter;

    public ResultAggregator(ResultsManager results, List<BenchmarkInstance> benchmarks) {
        this._results = results;
        this._benchmarks = benchmarks;

        this._cpuLoadMeter = new CpuLoadMeter();
        this._transactionMeter = new TransactionMeter();
        this._memoryUsageMeter = new MemoryUsageMeter();

        this.start();
    }

    public BenchmarkResult getResult() {
        return _result;
    }

    public void start() {
        _result = new BenchmarkResult(_benchmarks);
        _result.setStartTime(System.currentTimeMillis());

        _transactionCounter = 0;
        _transactionMeter.clear();
        _cpuLoadMeter.clear();
        _memoryUsageMeter.clear();
    }

    public void incrementCounter(int increment) {
        incrementCounter(increment, System.currentTimeMillis());
    }

    protected void incrementCounter(int increment, long currentTicks) {
        _transactionCounter += increment;

        // If it's less then a second then wait
        long measureInterval = currentTicks - _transactionMeter.getLastMeasuredTicks();
        if (measureInterval >= 1000 && _result != null)
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
                    _result.setElapsedTime(System.currentTimeMillis() - _result.getStartTime());
                    _result.setPerformanceMeasurement(_transactionMeter.getMeasurement());
                    _result.setCpuLoadMeasurement(_cpuLoadMeter.getMeasurement());
                    _result.setMemoryUsageMeasurement(_memoryUsageMeter.getMeasurement());

                    _results.notifyUpdated(_result);
                }
            }
        }
    }

    public void sendMessage(String message) {
        _results.notifyMessage(message);
    }

    public void reportError(Object error) {
        if (_result.getErrors().size() < ResultAggregator.MaxErrorCount)
            _result.getErrors().add(error);

        _results.notifyError(error);
    }

    public void stop() {
        _results.add(_result);
    }
}
