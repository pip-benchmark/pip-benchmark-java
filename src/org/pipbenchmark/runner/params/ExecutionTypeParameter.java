package org.pipbenchmark.runner.params;

import org.pipbenchmark.*;
import org.pipbenchmark.runner.config.*;

public class ExecutionTypeParameter extends Parameter {
    private ConfigurationManager _configuration;

    public ExecutionTypeParameter(ConfigurationManager configuration) {
        super(
    		"General.Benchmarking.ExecutionType",
        	"Execution type: proportional or sequencial",
        	"Proportional"
    	);
        _configuration = configuration;
    }

    @Override
    public String getValue() {
        return _configuration.getExecutionType() == ExecutionType.Proportional 
        	? "Proportional" : "Sequencial"; 
    }
    
    @Override
    public void setValue(String value) {
    	value = value.toLowerCase();
        _configuration.setExecutionType(value.startsWith("p")
            ? ExecutionType.Proportional : ExecutionType.Sequential);
    }
}
