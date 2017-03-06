package org.pipbenchmark.runner.config;

import org.pipbenchmark.*;
import org.pipbenchmark.runner.BenchmarkSuiteInstance;

public class IndirectSuiteParameter extends Parameter {
    private Parameter _originalParameter;

    public IndirectSuiteParameter(BenchmarkSuiteInstance suite,
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
