package com.digitalliving.nbenchmark;

public interface IExecutionContext {
    void incrementTransactionCounter();
    void incrementTransactionCounter(int increment);
    void stopBenchmarking();
}
