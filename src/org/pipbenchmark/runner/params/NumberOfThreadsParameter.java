package org.pipbenchmark.runner.params;

import org.pipbenchmark.*;
import org.pipbenchmark.runner.config.*;
import org.pipbenchmark.util.*;

public class NumberOfThreadsParameter extends Parameter {
    private ConfigurationManager _configuration;

    public NumberOfThreadsParameter(ConfigurationManager configuration) {
        super(
    		"General.Benchmarking.NumberOfThreads", 
    		"Number of threads for concurrent benchmarking", 
    		"1"
		);
        _configuration = configuration;
    }

    @Override
    public String getValue() {
        return Converter.integerToString(_configuration.getNumberOfThreads()); 
    }
    
    @Override
    public void setValue(String value) {
        _configuration.setNumberOfThreads(Converter.stringToInteger(value, 1));
    }
}
