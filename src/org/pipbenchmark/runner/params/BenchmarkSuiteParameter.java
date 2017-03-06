package org.pipbenchmark.runner.params;

import org.pipbenchmark.*;
import org.pipbenchmark.runner.benchmarks.BenchmarkSuiteInstance;

public class BenchmarkSuiteParameter extends Parameter {
    private Parameter _originalParameter;

    public BenchmarkSuiteParameter(BenchmarkSuiteInstance suite,
        Parameter originalParameter) {
        super(String.format("%s.%s", suite.getName(), originalParameter.getName()),
          originalParameter.getDescription(), originalParameter.getDefaultValue());
        _originalParameter = originalParameter;
    }

    @Override
    public String getValue() {
        return _originalParameter.getValue(); 
    }
    
    @Override
    public void setValue(String value) {
        _originalParameter.setValue(value);
    }
}
