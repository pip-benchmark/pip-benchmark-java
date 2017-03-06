package org.pipbenchmark.runner.params;

import org.pipbenchmark.*;
import org.pipbenchmark.runner.config.*;
import org.pipbenchmark.util.*;

public class DurationParameter extends Parameter {
    private ConfigurationManager _configuration;

    public DurationParameter(ConfigurationManager configuration) {
        super(
    		"General.Benchmarking.Duration", 
    		"Duration of benchmark execution in seconds", 
    		"60"
		);
        _configuration = configuration;
    }

    @Override
    public String getValue() {
        return Converter.integerToString(_configuration.getDuration()); 
    }
    
    @Override
    public void setValue(String value) {
        _configuration.setDuration(Converter.stringToInteger(value, 60));
    }
}
