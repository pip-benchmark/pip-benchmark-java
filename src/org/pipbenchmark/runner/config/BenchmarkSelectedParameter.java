package org.pipbenchmark.runner.config;

import org.pipbenchmark.*;
import org.pipbenchmark.runner.BenchmarkInstance;

public class BenchmarkSelectedParameter extends Parameter {
    private BenchmarkInstance _benchmark;

    public BenchmarkSelectedParameter(BenchmarkInstance benchmark) {
        super(String.format("%s.%s.Selected", benchmark.getSuite().getName(), benchmark.getName()),
            String.format("Selecting benchmark %s in suite %s", 
            benchmark.getName(), benchmark.getSuite().getName()), "true");
        _benchmark = benchmark;
    }

    @Override
    public String getValue() {
        return SimpleTypeConverter.booleanToString(_benchmark.isSelected()); 
    }
    
    @Override
    public void setValue(String value) {
        _benchmark.setSelected(SimpleTypeConverter.stringToBoolean(value));
    }
}
