package org.pipbenchmark.runner.results;

import org.pipbenchmark.runner.execution.ExecutionState;

public interface IResultListener {
	void onResultUpdated(ExecutionState status, BenchmarkResult result);	
}
