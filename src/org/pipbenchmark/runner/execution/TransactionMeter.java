package org.pipbenchmark.runner.execution;

public class TransactionMeter extends BenchmarkMeter {
    private double _transactionCounter;
    private long _lastMeasuredTicks = 0;

    public TransactionMeter() {
    	super();
    }

    public long getLastMeasuredTicks() {
        return _lastMeasuredTicks;
    }

    public void incrementTransactionCounter() {
        _transactionCounter++;
    }

    public void setTransactionCounter(double value) {
        _transactionCounter = value;
    }

    @Override
    protected double performMeasurement() {
        long currentTicks = System.currentTimeMillis();
        double durationInMsecs = currentTicks - _lastMeasuredTicks;
        double result = _transactionCounter * 1000 / durationInMsecs;
        _lastMeasuredTicks = currentTicks;
        _transactionCounter = 0;
        return result;
    }
}
