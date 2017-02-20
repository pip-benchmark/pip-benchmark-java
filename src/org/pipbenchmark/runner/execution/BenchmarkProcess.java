package org.pipbenchmark.runner.execution;

import java.util.*;

import org.pipbenchmark.runner.*;

public class BenchmarkProcess {
	private BenchmarkRunner _runner;
    private ExecutionStrategy _strategy = null;
    private List<BenchmarkSuiteInstance> _suites;

    private int _numberOfThreads = 1;
    private MeasurementType _performanceType = MeasurementType.Peak;
    private double _nominalPerformance = 1;
    private ExecutionType _samplingType = ExecutionType.Proportional;
    private int _duration = 60000;
    private boolean _forceContinue = false;

    private List<BenchmarkResult> _results = new ArrayList<BenchmarkResult>();

    public BenchmarkProcess(BenchmarkRunner runner) {
        _runner = runner;
    }

    public BenchmarkRunner getRunner() {
        return _runner;
    }
    
    public boolean isRunning() {
        return _strategy != null;
    }

    public int getNumberOfThreads() {
        return _numberOfThreads; 
    }
    
    public void setNumberOfThreads(int value) {
        _numberOfThreads = value;
    }

    public MeasurementType getMeasurementType() {
        return _performanceType;
    }
    
    public void setMeasurementType(MeasurementType value) {
        _performanceType = value; 
    }

    public double getNominalRate() {
        return _nominalPerformance; 
    }
    
    public void setNominalRate(double value) {
        _nominalPerformance = value;
    }

    public ExecutionType getExecutionType() {
        return _samplingType; 
    }
    
    public void setExecutionType(ExecutionType value) {
        _samplingType = value;
    }

    public int getDuration() {
        return _duration; 
    }
    
    public void setDuration(int value) {
        _duration = value;
    }
    
    public boolean isForceContinue() {
    	return _forceContinue;
    }
    
    public void setForceContinue(boolean value) {
    	_forceContinue = value;
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
            throw new BenchmarkException("There are no benchmarks to execute");

        // Create requested test strategy
        if (_samplingType == ExecutionType.Sequential)
            _strategy = new SequencialExecutionStrategy(this, selectedBenchmarks);
        else
            _strategy = new ProportionalExecutionStrategy(this, selectedBenchmarks);

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
