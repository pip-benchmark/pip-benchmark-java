package org.pipbenchmark;

public class DelegatedBenchmark extends Benchmark {
    private Runnable _executeCallback;

    public DelegatedBenchmark(String name, String description, Runnable executeCallback) {  	
    	super(name, description);
    	
    	if (executeCallback == null)
            throw new NullPointerException("executeCallback");

       _executeCallback = executeCallback;
    }

    @Override
    public void setUp() { }

    @Override
    public void execute() {
        _executeCallback.run();
    }

    @Override
    public void tearDown() { }
}

