package org.pipbenchmark.runner;

public interface IResultListener {
	void onResultUpdated(ExecutionState status, BenchmarkResult result);	
}
