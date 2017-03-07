package org.pipbenchmark.runner.execution;

import java.util.*;

import org.pipbenchmark.*;
import org.pipbenchmark.runner.benchmarks.BenchmarkSuiteInstance;

class ExecutionContext implements IExecutionContext {
    private BenchmarkSuiteInstance _suite;
    private ResultAggregator _aggregator;
    private ExecutionStrategy _strategy;

    public ExecutionContext(BenchmarkSuiteInstance suite,
    	ResultAggregator aggregator,ExecutionStrategy strategy) {
        _strategy = strategy;
        _aggregator = aggregator;
        _suite = suite;
    }
    
    public Map<String, Parameter> getParameters() {
    	return _suite.getSuite().getParameters();
    }

    public void incrementCounter() {
        _aggregator.incrementCounter(1);
    }

    public void incrementCounter(int increment) {
        _aggregator.incrementCounter(increment);
    }

	public void sendMessage(String message) {
		_aggregator.sendMessage(message);
	}

	public void reportError(String errorMessage) {
		_aggregator.reportError(errorMessage);
	}

	public boolean isStopped() {
		return _strategy.isStopped();
	}
	
	public void stop() {
        _strategy.stop();
    }

}
