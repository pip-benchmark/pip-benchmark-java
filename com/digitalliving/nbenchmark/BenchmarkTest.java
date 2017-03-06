package com.digitalliving.nbenchmark;

public abstract class BenchmarkTest {
    private Object _syncRoot = new Object();
    private BenchmarkTestSuite _parentTestSuite;
    private String _name;
    private String _description;
    private boolean _enabled = false;
    private int _frequency = 100;
    private boolean _passive = false;

    public BenchmarkTest(BenchmarkTestSuite parentTestSuite,
        String name, String description) {
        _parentTestSuite = parentTestSuite;
        _name = name;
        _description = description;
    }

    public BenchmarkTest(BenchmarkTestSuite parentTestSuite,
        String name, String description, boolean passive) {
    	this(parentTestSuite, name, description);
    	_passive = passive;
    }

    public BenchmarkTest(BenchmarkTestSuite parentTestSuite,
        String name, String description, int frequency) {
    	this(parentTestSuite, name, description);
    	_frequency = frequency;
    }

    public Object getSyncRoot() {
        return _syncRoot;
    }

    public String getName() {
        return _name;
    }

    public String getFullName() {
        return String.format("%s.%s", getTestSuite().getName(), getName());
    }

    public String getDescription() {
        return _description;
    }

    public BenchmarkTestSuite getTestSuite() {
        return _parentTestSuite;
    }

    public IExecutionContext getContext() {
        return _parentTestSuite.getContext();
    }

    public boolean isEnabled() {
        return _enabled;
    }
    
    public void setEnabled(boolean value) {
        _enabled = value;
    }

    public boolean isPassive() {
        return _passive;
    }

    public int getFrequency() {
        return _frequency;
    }
    
    public void setFrequency(int value) {
        _frequency = Math.max(0, Math.min(10000, value));
    }

    public abstract void setUp();
    public abstract void execute();
    public abstract void tearDown();
}
