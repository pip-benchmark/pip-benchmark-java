package org.pipbenchmark.runner;

public class BenchmarkException extends RuntimeException {
	private static final long serialVersionUID = 4696460830872070784L;

	public BenchmarkException() {
	}

	public BenchmarkException(String arg0) {
		super(arg0);
	}

	public BenchmarkException(Throwable cause) {
		super(cause);
	}

	public BenchmarkException(String message, Throwable cause) {
		super(message, cause);
	}

}
