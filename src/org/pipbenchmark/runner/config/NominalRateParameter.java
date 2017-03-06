package org.pipbenchmark.runner.config;

import org.pipbenchmark.*;
import org.pipbenchmark.runner.execution.*;

public class NominalRateParameter extends Parameter {
    private BenchmarkProcess _process;

    public NominalRateParameter(BenchmarkProcess process) {
        super(
    		"General.Benchmarking.NominalRate",
        	"Rate for nominal benchmarking in TPS", 
        	"1"
    	);
        _process = process;
    }

    @Override
    public String getValue() {
        return SimpleTypeConverter.doubleToString(_process.getNominalRate()); 
    }
    
    @Override
    public void setValue(String value) {
        _process.setNominalRate(SimpleTypeConverter.stringToDouble(value, 1));
    }
}
