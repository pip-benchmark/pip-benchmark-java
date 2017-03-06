package org.pipbenchmark.runner.params;

import org.pipbenchmark.*;
import org.pipbenchmark.runner.config.*;
import org.pipbenchmark.util.*;

public class NominalRateParameter extends Parameter {
    private ConfigurationManager _configuration;

    public NominalRateParameter(ConfigurationManager configuration) {
        super(
    		"General.Benchmarking.NominalRate",
        	"Rate for nominal benchmarking in TPS", 
        	"1"
    	);
        _configuration = configuration;
    }

    @Override
    public String getValue() {
        return Converter.doubleToString(_configuration.getNominalRate()); 
    }
    
    @Override
    public void setValue(String value) {
        _configuration.setNominalRate(Converter.stringToDouble(value, 1));
    }
}
