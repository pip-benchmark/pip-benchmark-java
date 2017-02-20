package org.pipbenchmark.runner.config;

import org.pipbenchmark.*;
import org.pipbenchmark.runner.execution.*;

public class DurationParameter extends Parameter {
    private BenchmarkProcess _process;

    public DurationParameter(BenchmarkProcess process) {
        super(
    		"General.Benchmarking.Duration", 
    		"Duration of benchmark execution in seconds", 
    		"60"
		);
        _process = process;
    }

    @Override
    public String getValue() {
        return SimpleTypeConverter.integerToString(_process.getDuration() / 1000); 
    }
    
    @Override
    public void setValue(String value) {
        _process.setDuration(SimpleTypeConverter.stringToInteger(value, 60) * 1000);
    }
}
