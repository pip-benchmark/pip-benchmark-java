package org.pipbenchmark.runner.execution;

import java.util.*;

import org.pipbenchmark.*;
import org.pipbenchmark.runner.benchmarks.BenchmarkSuiteInstance;

class ExecutionContext implements IExecutionContext {
    private ExecutionStrategy _strategy;
    private BenchmarkSuiteInstance _suite;

    public ExecutionContext(ExecutionStrategy strategy, BenchmarkSuiteInstance suite) {
        _strategy = strategy;
        _suite = suite;
    }
    
    public Map<String, Parameter> getParameters() {
    	return _suite.getParameters();
    }

    public void incrementCounter() {
        _strategy.incrementCounter(1);
    }

    public void incrementCounter(int increment) {
        _strategy.incrementCounter(increment);
    }

	public void sendMessage(String message) {
		_strategy.sendMessage(message);
	}

	public void reportError(String errorMessage) {
		_strategy.reportError(errorMessage);
	}

	public void stop() {
        _strategy.getProcess().stop();
    }

}
