package org.pipbenchmark.runner;

import org.pipbenchmark.runner.benchmarks.*;
import org.pipbenchmark.runner.config.*;
import org.pipbenchmark.runner.environment.*;
import org.pipbenchmark.runner.execution.*;
import org.pipbenchmark.runner.params.*;
import org.pipbenchmark.runner.reports.*;
import org.pipbenchmark.runner.results.*;

public class BenchmarkRunner {
	private ConfigurationManager _configuration;
    private ParametersManager _parameters;
    private ResultsManager _results;
    private BenchmarksManager _benchmarks;
    private ExecutionManager _execution;
    private ReportGenerator _report;
    private EnvironmentManager _environment;
    
    public BenchmarkRunner() {
    	_configuration = new ConfigurationManager();
        _parameters = new ParametersManager(_configuration);
        _results = new ResultsManager();
        _benchmarks = new BenchmarksManager(_parameters);
        _execution = new ExecutionManager(_configuration, _results);
        _environment = new EnvironmentManager();
        _report = new ReportGenerator(_configuration, _results, _parameters, _benchmarks, _environment);
    }

    public ConfigurationManager getConfiguration() {
    	return _configuration;
    }
    
    public ParametersManager getParameters() {
        return _parameters;
    }
    
    public ResultsManager getResults() {
    	return _results;
    }

    public ExecutionManager getExecution() {
        return _execution;
    }

    public BenchmarksManager getBenchmarks() {
        return _benchmarks;
    }

    public ReportGenerator getReport() {
        return _report;
    }

    public EnvironmentManager getEnvironment() {
        return _environment;
    }
    
    public boolean isRunning() {
        return getExecution().isRunning();
    }

    public void start() {
        getExecution().start(getBenchmarks().getSelected());
    }

    public void stop() {
        getExecution().stop();
    }

    public void run() {
    	getExecution().run(getBenchmarks().getSelected());
    }
    
}
