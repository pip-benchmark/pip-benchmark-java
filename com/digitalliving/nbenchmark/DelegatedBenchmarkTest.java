package com.digitalliving.nbenchmark;

public class DelegatedBenchmarkTest extends BenchmarkTest {
    private Runnable _executeCallback;

    public DelegatedBenchmarkTest(BenchmarkTestSuite parentTestSuite,
        String name, String description, Runnable executeCallback) {
    	
    	super(parentTestSuite, name, description);
    	if (executeCallback == null) {
            throw new NullPointerException("executeCallback");
        }
        _executeCallback = executeCallback;
    }

    public DelegatedBenchmarkTest(BenchmarkTestSuite parentTestSuite,
        String name, String description, Runnable executeCallback, int frequency) {
    	this(parentTestSuite, name, description, executeCallback);
    	super.setFrequency(frequency);
    }

    @Override
    public void setUp() {
    }

    @Override
    public void execute() {
        _executeCallback.run();
    }

    @Override
    public void tearDown() {
    }
}

