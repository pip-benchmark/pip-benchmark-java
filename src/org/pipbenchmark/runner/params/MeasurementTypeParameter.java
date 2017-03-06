package org.pipbenchmark.runner.params;

import org.pipbenchmark.*;
import org.pipbenchmark.runner.config.*;

public class MeasurementTypeParameter extends Parameter {
    private ConfigurationManager _configuration;

    public MeasurementTypeParameter(ConfigurationManager configuration) {
        super(
    		"General.Benchmarking.MeasurementType",
            "Performance type: peak or nominal", 
            "Peak"
        );
        _configuration = configuration;
    }

    @Override
    public String getValue() {
        return _configuration.getMeasurementType() == MeasurementType.Peak ? "Peak" : "Nominal"; 
    }
    
    @Override
    public void setValue(String value) {
    	value = value.toLowerCase();
        _configuration.setMeasurementType(value.startsWith("p")
            ? MeasurementType.Peak : MeasurementType.Nominal); 
    }
}
