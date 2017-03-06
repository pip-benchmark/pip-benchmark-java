package org.pipbenchmark.runner.execution;

import java.util.*;

import org.pipbenchmark.runner.*;
import org.pipbenchmark.runner.benchmarks.*;
import org.pipbenchmark.runner.config.*;
import org.pipbenchmark.runner.results.*;

public class ExecutionManager {
	protected ConfigurationManager _configuration;
	private BenchmarkRunner _runner;
    private ExecutionStrategy _strategy = null;
    private List<BenchmarkSuiteInstance> _suites;

    private List<BenchmarkResult> _results = new ArrayList<BenchmarkResult>();

    public ExecutionManager(ConfigurationManager configuration, BenchmarkRunner runner) {
    	_configuration = configuration;
        _runner = runner;
    }

    public BenchmarkRunner getRunner() {
        return _runner;
    }
    
    public boolean isRunning() {
        return _strategy != null;
    }

    public List<BenchmarkResult> getResults() {
        return _results;
    }

    public void start(BenchmarkSuiteInstance suite) {
    	List<BenchmarkSuiteInstance> suites = new ArrayList<BenchmarkSuiteInstance>();
    	suites.add(suite);
        start(suites);
    }

    public void start(Collection<BenchmarkSuiteInstance> suites) {
        if (_strategy != null)
            stop();

        // Identify active tests
        _suites = new ArrayList<BenchmarkSuiteInstance>(suites);
        List<BenchmarkInstance> selectedBenchmarks = new ArrayList<BenchmarkInstance>();
        for (BenchmarkSuiteInstance suite : _suites) {
            for (BenchmarkInstance benchmark : suite.getBenchmarks()) {
                if (benchmark.isSelected())
                    selectedBenchmarks.add(benchmark);
            }
        }

        // Check if there is at least one test defined
        if (selectedBenchmarks.size() == 0) 
            throw new NullPointerException("There are no benchmarks to execute");

        // Create requested test strategy
        if (_configuration.getExecutionType() == ExecutionType.Sequential)
            _strategy = new SequencialExecutionStrategy(_configuration, this, selectedBenchmarks);
        else
            _strategy = new ProportionalExecutionStrategy(_configuration, this, selectedBenchmarks);

        // Initialize parameters and start 
        _results.clear();
        _strategy.start();
    }

    public void stop() {
        if (_strategy != null) {
            // Fill results
            _results.clear();
            _results.addAll(_strategy.getResults());

            // Stop strategy
            _strategy.stop();
            
            _strategy = null;
        }
    }

    public void notifyResultUpdate(ExecutionState status, BenchmarkResult result) {
    	getRunner().notifyResultUpdated(status, result);
    }

    public void notifyMessageSent(String message) {
    	getRunner().notifyMessageSent(message);
    }

    public void notifyErrorReported(String errorMessage) {
    	getRunner().notifyErrorReported(errorMessage);
    }
}
