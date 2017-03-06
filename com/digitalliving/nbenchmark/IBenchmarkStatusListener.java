package com.digitalliving.nbenchmark;

public interface IBenchmarkStatusListener {
	void benchmarkUpdated(BenchmarkingStatus status, BenchmarkingResult result);	
	void configurationUpdated();
}
