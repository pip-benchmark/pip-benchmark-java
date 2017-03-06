package org.pipbenchmark.runner.config;

import org.pipbenchmark.*;
import org.pipbenchmark.runner.ExecutionType;
import org.pipbenchmark.runner.execution.*;

public class ExecutionTypeParameter extends Parameter {
    private BenchmarkProcess _process;

    public ExecutionTypeParameter(BenchmarkProcess process) {
        super(
    		"General.Benchmarking.ExecutionType",
        	"Execution type: proportional or sequencial",
        	"Proportional"
    	);
        _process = process;
    }

    @Override
    public String getValue() {
        return _process.getExecutionType() == ExecutionType.Proportional 
        	? "Proportional" : "Sequencial"; 
    }
    
    @Override
    public void setValue(String value) {
    	value = value.toLowerCase();
        _process.setExecutionType(value.startsWith("p")
            ? ExecutionType.Proportional : ExecutionType.Sequential);
    }
}
