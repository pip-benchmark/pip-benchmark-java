package org.pipbenchmark;

import java.util.*;

public abstract class BenchmarkSuite {
    private String _name;
    private String _description;
    private Map<String, Parameter> _parameters = new HashMap<String, Parameter>();
    private List<Benchmark> _benchmarks = new ArrayList<Benchmark>();
    private IExecutionContext _context;

    protected BenchmarkSuite(String name, String description) {
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

    public Map<String, Parameter> getParameters() {
        return _parameters; 
    }
    
    public Parameter addParameter(Parameter parameter) {
    	_parameters.put(parameter.getName(), parameter);
    	return parameter;
    }
    
    public Parameter createParameter(String name, String description) {
    	return createParameter(name, description, null);
    }

    public Parameter createParameter(String name, String description, String defaultValue) {
    	Parameter parameter = new Parameter(name, description, defaultValue);
    	_parameters.put(name, parameter);
    	return parameter;
    }
    
    public List<Benchmark> getBenchmarks() {
        return _benchmarks;
    }

    protected Benchmark addBenchmark(Benchmark benchmark) {
        _benchmarks.add(benchmark);
        return benchmark;
    }

    protected Benchmark createBenchmark(String name, String description, Runnable executeCallback) {
    	Benchmark benchmark = new DelegatedBenchmark(name, description, executeCallback);
        _benchmarks.add(benchmark);
    	return benchmark;
    }

    public void setUp() {}
    public void tearDown() {}
}
