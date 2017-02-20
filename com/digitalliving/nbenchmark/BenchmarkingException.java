package com.digitalliving.nbenchmark;

public class BenchmarkingException extends RuntimeException {
	private static final long serialVersionUID = 4696460830872070784L;

	public BenchmarkingException() {
	}

	public BenchmarkingException(String arg0) {
		super(arg0);
	}

	public BenchmarkingException(Throwable cause) {
		super(cause);
	}

	public BenchmarkingException(String message, Throwable cause) {
		super(message, cause);
	}

}
