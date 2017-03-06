package org.pipbenchmark.runner.params;

import org.pipbenchmark.*;
import org.pipbenchmark.runner.benchmarks.BenchmarkInstance;
import org.pipbenchmark.util.Converter;

public class BenchmarkProportionParameter extends Parameter {
    private BenchmarkInstance _benchmark;

    public BenchmarkProportionParameter(BenchmarkInstance benchmark) {
        super(String.format("%s.%s.Proportion", benchmark.getSuite().getName(), benchmark.getName()),
            String.format("Sets execution proportion for benchmark %s in suite %s", 
            benchmark.getName(), benchmark.getSuite().getName()), "100");
        _benchmark = benchmark;
    }

    @Override
    public String getValue() {
        return Converter.integerToString(_benchmark.getProportion()); 
    }
    
    @Override
    public void setValue(String value) {
        _benchmark.setProportion(Converter.stringToInteger(value, 100));
    }
}
