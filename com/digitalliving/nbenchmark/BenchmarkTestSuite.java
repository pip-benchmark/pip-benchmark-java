package com.digitalliving.nbenchmark;

public abstract class BenchmarkTestSuite {
	private Object _syncRoot = new Object();
    private String _name;
    private String _description;
    private boolean _enabled = true;
    private ConfigurationParameterCollection _configuration = new ConfigurationParameterCollection();
    private BenchmarkTestCollection _tests = new BenchmarkTestCollection();
    private IExecutionContext _context;

    protected BenchmarkTestSuite(String name, String description) {
        _name = name;
        _description = description;
    }

    public Object getSyncRoot() {
        return _syncRoot;
    }

    public String getName() {
        return _name;
    }

    public String getDescription() {
        return _description;
    }

    public boolean isEnabled() {
        return _enabled;
    }
    
    public void setEnabled(boolean value) {
        _enabled = value;
    }

    public IExecutionContext getContext() {
        return _context;
    }

    public ConfigurationParameterCollection getConfiguration() {
        return _configuration; 
    }
    
    protected void setConfiguration(ConfigurationParameterCollection value) {
        if (value == null) {
            throw new NullPointerException("value");
        }
        _configuration = value; 
    }

    public BenchmarkTestCollection getTests() {
        return _tests;
    }

    public void initialize(IExecutionContext context) {
        _context = context;
        setUp();
        setUpAllTests();
    }

    public void deinitialize() {
        tearDownAllTests();
        tearDown();
        _context = null;
    }

    protected void addTest(BenchmarkTest test) {
        _tests.add(test);
    }

    protected void addTest(BenchmarkTest test, int frequency) {
        test.setFrequency(frequency);
        _tests.add(test);
    }

    protected void addTest(String name, String description, Runnable executeCallback) {
        addTest(name, description, executeCallback, 100);
    }

    protected void addTest(String name, String description, Runnable executeCallback, 
        int frequency) {
        BenchmarkTest test = new DelegatedBenchmarkTest(this, name, description, executeCallback, frequency);
        _tests.add(test);
    }

    private void setUpAllTests() {
        for (BenchmarkTest test : _tests) {
            if (test.isEnabled()) {
                test.setUp();
            }
        }
    }

    private void tearDownAllTests()
    {
        for (BenchmarkTest test : _tests) {
            if (test.isEnabled()) {
                test.tearDown();
            }
        }
    }

    public abstract void setUp();
    public abstract void tearDown();
}
