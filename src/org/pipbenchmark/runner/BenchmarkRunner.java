package org.pipbenchmark.runner;

import java.util.*;

import org.pipbenchmark.*;
import org.pipbenchmark.runner.benchmarks.BenchmarkInstance;
import org.pipbenchmark.runner.benchmarks.BenchmarkSuiteInstance;
import org.pipbenchmark.runner.benchmarks.BenchmarksManager;
import org.pipbenchmark.runner.config.ConfigurationManager;
import org.pipbenchmark.runner.environment.*;
import org.pipbenchmark.runner.execution.*;
import org.pipbenchmark.runner.params.*;
import org.pipbenchmark.runner.reports.*;
import org.pipbenchmark.runner.results.BenchmarkResult;
import org.pipbenchmark.runner.results.IMessageListener;
import org.pipbenchmark.runner.results.IResultListener;

import java.io.*;

public class BenchmarkRunner {
	private ConfigurationManager _configuration;
    private BenchmarksManager _benchmarks;
    private ParametersManager _parameters;
    private ExecutionManager _execution;
    private ReportGenerator _report;
    private EnvironmentManager _environment;

    private List<IResultListener> _resultUpdatedListeners = new ArrayList<IResultListener>();
    private List<IMessageListener> _messageSentListeners = new ArrayList<IMessageListener>();
    private List<IMessageListener> _errorReportedListeners = new ArrayList<IMessageListener>();
    
    public BenchmarkRunner() {
    	_configuration = new ConfigurationManager();
        _parameters = new ParametersManager(_configuration);
        _benchmarks = new BenchmarksManager(this);
        _execution = new ExecutionManager(_configuration, this);
        _report = new ReportGenerator(this);
        _environment = new EnvironmentManager(this);
    }

    public ConfigurationManager getConfiguration() {
    	return _configuration;
    }
    
    public ParametersManager getParameters() {
        return _parameters;
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

    public List<BenchmarkSuiteInstance> getSuiteInstances() {
        return getBenchmarks().getSuites();
    }

    public void addSuiteFromClass(String className)
    	throws ClassNotFoundException, InstantiationException {
    	getBenchmarks().addSuiteFromClass(className);
    }

    public void addSuite(BenchmarkSuite suite) {
    	getBenchmarks().addSuite(suite);
    }

    public void addSuite(BenchmarkSuiteInstance suite) {
    	getBenchmarks().addSuite(suite);
    }
    
    public void loadSuitesFromLibrary(String fileName) throws IOException {
        getBenchmarks().loadSuitesFromLibrary(fileName);
    }

    public void unloadSuite(String suiteName) {
        getBenchmarks().removeSuite(suiteName);
    }

    public void unloadAllSuites() {
        getBenchmarks().removeAllSuites();
    }

    public void unloadSuite(BenchmarkSuiteInstance suite) {
    	getBenchmarks().removeSuite(suite);
    }

    public void selectAllBenchmarks() {
        getBenchmarks().selectAllBenchmarks();
    }

    public void selectBenchmarksByName(String ...benchmarkNames) {
       getBenchmarks().selectBenchmarksByName(benchmarkNames);
    }

    public void selectBenchmarks(BenchmarkInstance ...benchmarks) {
        getBenchmarks().selectBenchmarks(benchmarks);
    }

    public List<BenchmarkResult> getResults() {
        return getExecution().getResults();
    }

    public void addResultUpdatedListener(IResultListener listener) {
    	_resultUpdatedListeners.add(listener);
    }
    
    public void removeResultUpdatedListener(IResultListener listener) {
    	_resultUpdatedListeners.remove(listener);
    }

    public void notifyResultUpdated(ExecutionState status, BenchmarkResult result) {
    	for (int index = 0; index < _resultUpdatedListeners.size(); index++) {
    		try {
    			IResultListener listener = _resultUpdatedListeners.get(index);
    			listener.onResultUpdated(status, result);
    		} catch (Exception ex) {
    			// Ignore and send a message to the next listener.
    		}
    	}
    }

    public void addMessageSentListener(IMessageListener listener) {
    	_messageSentListeners.add(listener);
    }
    
    public void removeMessageSentListener(IMessageListener listener) {
    	_messageSentListeners.remove(listener);
    }

    public void notifyMessageSent(String message) {
    	for (int index = 0; index < _messageSentListeners.size(); index++) {
    		try {
    			IMessageListener listener = _messageSentListeners.get(index);
    			listener.onMessage(message);
    		} catch (Exception ex) {
    			// Ignore and send a message to the next listener.
    		}
    	}
    }

    public void addErrorReportedListener(IMessageListener listener) {
    	_errorReportedListeners.add(listener);
    }
    
    public void removeErrorReportedListener(IMessageListener listener) {
    	_errorReportedListeners.remove(listener);
    }

    public void notifyErrorReported(String message) {
    	for (int index = 0; index < _errorReportedListeners.size(); index++) {
    		try {
    			IMessageListener listener = _errorReportedListeners.get(index);
    			listener.onMessage(message);
    		} catch (Exception ex) {
    			// Ignore and send a message to the next listener.
    		}
    	}
    }
    
    public boolean isRunning() {
        return getExecution().isRunning();
    }

    public void start() {
        getExecution().start(getBenchmarks().getSuites());
    }

    public void stop() {
        getExecution().stop();
    }

    public String generateReport() {
        return getReport().generateReport();
    }

    public void saveReportToFile(String fileName) throws IOException {
        getReport().saveReportToFile(fileName);
    }

    public Map<String, String> getSystemInformation() {
        return getEnvironment().getSystemInformation();
    }

    public double getCpuBenchmark() {
        return getEnvironment().getCpuBenchmark();
    }

    public double getVideoBenchmark() {
        return getEnvironment().getVideoBenchmark();
    }

    public double getDiskBenchmark() {
        return getEnvironment().getDiskBenchmark();
    }

    public void benchmarkEnvironment(boolean cpu, boolean disk, boolean video) {
        getEnvironment().benchmarkEnvironment(cpu, disk, video);
    }

    public void benchmarkEnvironment() {
        getEnvironment().benchmarkEnvironment(true, true, true);
    }
}
