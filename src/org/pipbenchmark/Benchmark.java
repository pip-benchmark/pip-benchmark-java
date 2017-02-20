package org.pipbenchmark;

public abstract class Benchmark {
    private String _name;
    private String _description;
    private IExecutionContext _context;

    public Benchmark(String name, String description) {
        _name = name;
        _description = description;
    }

    public String getName() {
        return _name;
    }

    public String getDescription() {
        return _description;
    }

    public IExecutionContext getContext() {
        return _context;
    }

    public void setContext(IExecutionContext value) {
    	_context = value;
    }
    
    public void setUp() {}

    public abstract void execute();
    
    public void tearDown() {}
}
