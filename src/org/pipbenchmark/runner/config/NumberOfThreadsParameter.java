package org.pipbenchmark.runner.config;

import org.pipbenchmark.*;
import org.pipbenchmark.runner.execution.*;

public class NumberOfThreadsParameter extends Parameter {
    private BenchmarkProcess _process;

    public NumberOfThreadsParameter(BenchmarkProcess process) {
        super(
    		"General.Benchmarking.NumberOfThreads", 
    		"Number of threads for concurrent benchmarking", 
    		"1"
		);
        _process = process;
    }

    @Override
    public String getValue() {
        return SimpleTypeConverter.integerToString(_process.getNumberOfThreads()); 
    }
    
    @Override
    public void setValue(String value) {
        _process.setNumberOfThreads(SimpleTypeConverter.stringToInteger(value, 1));
    }
}
