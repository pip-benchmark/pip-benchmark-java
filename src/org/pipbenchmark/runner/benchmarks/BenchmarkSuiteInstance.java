package org.pipbenchmark.runner.benchmarks;

import java.util.*;

import org.pipbenchmark.*;

public class BenchmarkSuiteInstance {
	private BenchmarkSuite _suite;
    private List<BenchmarkInstance> _benchmarks = new ArrayList<BenchmarkInstance>();

    public BenchmarkSuiteInstance(BenchmarkSuite suite) {
    	_suite = suite;
    	
    	for (Benchmark benchmark : suite.getBenchmarks())
    		_benchmarks.add(new BenchmarkInstance(this, benchmark));
    }

    public BenchmarkSuite getSuite() {
        return _suite;
    }

    public String getName() {
        return _suite.getName();
    }

    public String getDescription() {
        return _suite.getDescription();
    }

    public Map<String, Parameter> getParameters() {
        return _suite.getParameters(); 
    }

    public List<BenchmarkInstance> getBenchmarks() {
        return _benchmarks;
    }

    public void selectAllBenchmarks() {
        for (BenchmarkInstance benchmark : _benchmarks)
            benchmark.setSelected(true);
    }

    public void selectBenchmark(String benchmarkName) {
        for (BenchmarkInstance benchmark : _benchmarks) {
            if (benchmark.getName().equals(benchmarkName))
                benchmark.setSelected(true);
        }
    }

    public void unselectAllBenchmarks() {
        for (BenchmarkInstance benchmark : _benchmarks)
            benchmark.setSelected(false);
    }

    public void unselectBenchmark(String benchmarkName) {
        for (BenchmarkInstance benchmark : _benchmarks) {
            if (benchmark.getName().equals(benchmarkName))
                benchmark.setSelected(false);
        }
    }
    
    public void setUp(IExecutionContext context) {
        _suite.setContext(context);
        _suite.setUp();
        
        for (BenchmarkInstance benchmark : _benchmarks) {
        	if (benchmark.isSelected())
        		benchmark.setUp(context);
        }
    }

    public void tearDown() {
        for (BenchmarkInstance benchmark : _benchmarks) {
        	if (benchmark.isSelected())
        		benchmark.tearDown();
        }
        
        _suite.tearDown();
        _suite.setContext(null);
    }

}
