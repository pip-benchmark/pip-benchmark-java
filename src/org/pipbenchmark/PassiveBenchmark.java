package org.pipbenchmark;

public class PassiveBenchmark extends Benchmark {
	public PassiveBenchmark(String name, String description) {
		super(name, description);
	}
	
	@Override
	public void execute() {
		throw new RuntimeException("Active measurement via Execute is not allow for passive benchmarks");
	}
}
