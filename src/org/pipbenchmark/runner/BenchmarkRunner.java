package org.pipbenchmark.runner;

import java.util.*;

import org.pipbenchmark.*;
import org.pipbenchmark.runner.config.*;
import org.pipbenchmark.runner.environment.*;
import org.pipbenchmark.runner.execution.*;
import org.pipbenchmark.runner.report.*;

import java.io.*;

public class BenchmarkRunner {
    private BenchmarkSuiteManager _suiteManager;
    private ConfigurationManager _configurationManager;
    private BenchmarkProcess _process;
    private ReportGenerator _reportGenerator;
    private EnvironmentState _environmentState;

    private List<IResultListener> _resultUpdatedListeners = new ArrayList<IResultListener>();
    private List<IConfigurationListener> _configurationUpdatedListeners = new ArrayList<IConfigurationListener>();
    private List<IMessageListener> _messageSentListeners = new ArrayList<IMessageListener>();
    private List<IMessageListener> _errorReportedListeners = new ArrayList<IMessageListener>();
    
    public BenchmarkRunner() {
        _suiteManager = new BenchmarkSuiteManager(this);
        _process = new BenchmarkProcess(this);
        _configurationManager = new ConfigurationManager(this);
        _reportGenerator = new ReportGenerator(this);
        _environmentState = new EnvironmentState(this);
    }

    public ConfigurationManager getConfigurationManager() {
        return _configurationManager;
    }

    public BenchmarkProcess getProcess() {
        return _process;
    }

    public BenchmarkSuiteManager getSuiteManager() {
        return _suiteManager;
    }

    public ReportGenerator getReportGenerator() {
        return _reportGenerator;
    }

    public EnvironmentState getEnvironmentState() {
        return _environmentState;
    }

    public List<BenchmarkSuiteInstance> getSuiteInstances() {
        return getSuiteManager().getSuites();
    }

    public void addSuiteFromClass(String className)
    	throws ClassNotFoundException, InstantiationException {
    	getSuiteManager().addSuiteFromClass(className);
    }

    public void addSuite(BenchmarkSuite suite) {
    	getSuiteManager().addSuite(suite);
    }

    public void addSuiteInstance(BenchmarkSuiteInstance suite) {
    	getSuiteManager().addSuite(suite);
    }
    
    public void loadSuitesFromLibrary(String fileName) throws IOException {
        getSuiteManager().loadSuitesFromLibrary(fileName);
    }

    public void unloadSuite(String suiteName) {
        getSuiteManager().removeSuite(suiteName);
    }

    public void unloadAllSuites() {
        getSuiteManager().removeAllSuites();
    }

    public void unloadSuite(BenchmarkSuiteInstance suite) {
    	getSuiteManager().removeSuite(suite);
    }

    public void selectAllBenchmarks() {
        getSuiteManager().selectAllBenchmarks();
    }

    public void selectBenchmarks(String ...benchmarkNames) {
       getSuiteManager().selectBenchmarks(benchmarkNames);
    }

    public void selectBenchmarks(BenchmarkInstance ...benchmarks) {
        getSuiteManager().selectBenchmarks(benchmarks);
    }
    
    public List<Parameter> getConfiguration() {
        return getConfigurationManager().getFilteredParameters();
    }

    public void loadConfigurationFromFile(String fileName) throws IOException {
        getConfigurationManager().loadConfigurationFromFile(fileName);
    }

    public void saveConfigurationToFile(String fileName) throws IOException {
        getConfigurationManager().saveConfigurationToFile(fileName);
    }

    public void setConfigurationToDefault() {
        getConfigurationManager().setConfigurationToDefault();
    }

    public void setConfiguration(Map<String, String> parameters) {
        getConfigurationManager().setConfiguration(parameters);
    }

    public int getNumberOfThreads() {
        return getProcess().getNumberOfThreads(); 
    }
    
    public void setNumberOfThreads(int value) {
        getProcess().setNumberOfThreads(value);
    }

    public MeasurementType getMeasurementType() {
        return getProcess().getMeasurementType(); 
    }
    
    public void setMeasurementType(MeasurementType value) {
        getProcess().setMeasurementType(value);
    }

    public double getNominalRate() {
        return getProcess().getNominalRate(); 
    }
    
    public void setNominalRate(double value) {
        getProcess().setNominalRate(value);
    }

    public ExecutionType getExecutionType() {
        return getProcess().getExecutionType(); 
    }
    
    public void setExecutionType(ExecutionType value) {
        getProcess().setExecutionType(value);
    }

    public int getDuration() {
        return getProcess().getDuration(); 
    }
    
    public void setDuration(int value) {
        getProcess().setDuration(value);
    }
    
    public boolean isForceContinue() {
    	return getProcess().isForceContinue();
    }
    
    public void setForceContinue(boolean value) {
    	getProcess().setForceContinue(value);
    }

    public List<BenchmarkResult> getResults() {
        return getProcess().getResults();
    }

    public void addConfigurationUpdatedListener(IConfigurationListener listener) {
    	_configurationUpdatedListeners.add(listener);
    }
    
    public void removeConfigurationUpdatedListener(IConfigurationListener listener) {
    	_configurationUpdatedListeners.remove(listener);
    }
    
    public void notifyConfigurationUpdated() {
    	for (int index = 0; index < _configurationUpdatedListeners.size(); index++) {
    		try {
    			IConfigurationListener listener = _configurationUpdatedListeners.get(index);
    			listener.onConfigurationUpdated();
    		} catch (Exception ex) {
    			// Ignore and send a message to the next listener.
    		}
    	}
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
        return getProcess().isRunning();
    }

    public void start() {
        getProcess().start(getSuiteManager().getSuites());
    }

    public void stop() {
        getProcess().stop();
    }

    public String generateReport() {
        return getReportGenerator().generateReport();
    }

    public void saveReportToFile(String fileName) throws IOException {
        getReportGenerator().saveReportToFile(fileName);
    }

    public Map<String, String> getSystemInformation() {
        return getEnvironmentState().getSystemInformation();
    }

    public double getCpuBenchmark() {
        return getEnvironmentState().getCpuBenchmark();
    }

    public double getVideoBenchmark() {
        return getEnvironmentState().getVideoBenchmark();
    }

    public double getDiskBenchmark() {
        return getEnvironmentState().getDiskBenchmark();
    }

    public void benchmarkEnvironment(boolean cpu, boolean disk, boolean video) {
        getEnvironmentState().benchmarkEnvironment(cpu, disk, video);
    }

    public void benchmarkEnvironment() {
        getEnvironmentState().benchmarkEnvironment(true, true, true);
    }
}
