package org.pipbenchmark.runner.execution;

import java.util.*;

import org.pipbenchmark.runner.benchmarks.*;
import org.pipbenchmark.runner.config.*;
import org.pipbenchmark.runner.results.*;

public abstract class ExecutionStrategy {
    protected ConfigurationManager _configuration;
    protected ResultsManager _results;
    protected ExecutionManager _execution;
    protected final Object _syncRoot = new Object();
    protected List<BenchmarkInstance> _benchmarks;
    protected List<BenchmarkInstance> _activeBenchmarks;
    protected List<BenchmarkSuiteInstance> _suites;

    protected ExecutionStrategy(ConfigurationManager configuration, ResultsManager results, 
    	ExecutionManager execution, List<BenchmarkInstance> benchmarks) {
        
    	_configuration = configuration;
    	_results = results;
    	_execution = execution;
    	
        _benchmarks = benchmarks;
        _activeBenchmarks = getActiveBenchmarks(benchmarks);
        _suites = getBenchmarkSuites(benchmarks);
    }

    private List<BenchmarkInstance> getActiveBenchmarks(List<BenchmarkInstance> benchmarks) {
        List<BenchmarkInstance> activeBenchmarks = new ArrayList<BenchmarkInstance>();
        for (BenchmarkInstance benchmark : benchmarks) {
            if (!benchmark.isPassive())
                activeBenchmarks.add(benchmark);
        }
        return activeBenchmarks;
    }
    
    private List<BenchmarkSuiteInstance> getBenchmarkSuites(List<BenchmarkInstance> benchmarks) {
        List<BenchmarkSuiteInstance> suites = new ArrayList<BenchmarkSuiteInstance>();
        for (BenchmarkInstance benchmark : benchmarks) {
            BenchmarkSuiteInstance suite = benchmark.getSuite();
            if (!suites.contains(suite))
                suites.add(suite);
        }
        return suites;
    }

    public abstract void start();
    public abstract void stop();    
}
