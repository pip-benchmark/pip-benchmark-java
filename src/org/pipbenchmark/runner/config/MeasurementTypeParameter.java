package org.pipbenchmark.runner.config;

import org.pipbenchmark.*;
import org.pipbenchmark.runner.MeasurementType;
import org.pipbenchmark.runner.execution.*;

public class MeasurementTypeParameter extends Parameter {
    private BenchmarkProcess _process;

    public MeasurementTypeParameter(BenchmarkProcess process) {
        super(
    		"General.Benchmarking.MeasurementType",
            "Performance type: peak or nominal", 
            "Peak"
        );
        _process = process;
    }

    @Override
    public String getValue() {
        return _process.getMeasurementType() == MeasurementType.Peak ? "Peak" : "Nominal"; 
    }
    
    @Override
    public void setValue(String value) {
    	value = value.toLowerCase();
        _process.setMeasurementType(value.startsWith("p")
            ? MeasurementType.Peak : MeasurementType.Nominal); 
    }
}
