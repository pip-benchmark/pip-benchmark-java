package org.pipbenchmark.runner.config;

import java.util.*;

import org.pipbenchmark.*;
import org.pipbenchmark.runner.*;

import java.io.*;

public class ConfigurationManager {
    private List<Parameter> _parameters = new ArrayList<Parameter>();
    private BenchmarkRunner _runner;

    public ConfigurationManager(BenchmarkRunner runner) {
    	_runner = runner;
    	
        _parameters.add(new NumberOfThreadsParameter(getRunner().getProcess()));
        _parameters.add(new MeasurementTypeParameter(getRunner().getProcess()));
        _parameters.add(new NominalRateParameter(getRunner().getProcess()));
        _parameters.add(new ExecutionTypeParameter(getRunner().getProcess()));
        _parameters.add(new DurationParameter(getRunner().getProcess()));
    }

    public BenchmarkRunner getRunner() {
        return _runner;
    }
 
    public List<Parameter> getFilteredParameters() {
        List<Parameter> filteredParameters = new ArrayList<Parameter>();
        for (Parameter parameter : _parameters) {
            if (!parameter.getName().endsWith(".Selected") 
        		&& !parameter.getName().endsWith(".Proportion")
                && !parameter.getName().startsWith("General.")) {
                filteredParameters.add(parameter);
            }
        }
        return filteredParameters; 
    }

    public List<Parameter> getAllParameters() {
        return _parameters;
    }

    public void loadConfigurationFromFile(String fileName) throws IOException {
        Properties properties = new Properties();
        FileInputStream stream = new FileInputStream(fileName);
        try {
            properties.loadFromStream(stream);
            for (Map.Entry<String, String> pair : properties.entrySet()) {
                setParameterValue(pair.getKey(), pair.getValue());
            }
        } finally {
        	stream.close();
        }

        getRunner().notifyConfigurationUpdated();
    }

    private void setParameterValue(String parameterName, String value) {
        for (Parameter parameter : _parameters) {
            if (parameter.getName().equals(parameterName)) {
                parameter.setValue(value);
            }
        }
    }

    public void saveConfigurationToFile(String fileName) throws IOException {
        Properties properties = new Properties();
        FileOutputStream stream = new FileOutputStream(fileName);
        try {
            for (Parameter parameter : _parameters) {
                properties.put(parameter.getName(), parameter.getValue());
            }
            properties.saveToStream(stream);
        } finally {
        	stream.close();
        }
    }

    public void createParametersForSuite(BenchmarkSuiteInstance suite) {
        // Create benchmark related parameters
        for (BenchmarkInstance benchmark : suite.getBenchmarks()) {
            Parameter benchmarkSelectedParameter
                = new BenchmarkSelectedParameter(benchmark);
            _parameters.add(benchmarkSelectedParameter);

            Parameter benchmarkProportionParameter
                = new BenchmarkProportionParameter(benchmark);
            _parameters.add(benchmarkProportionParameter);
        }

        // Create indirect suite parameters
        for (Parameter originalParameter : suite.getParameters().values()) {
            Parameter indirectParameter
                = new IndirectSuiteParameter(suite, originalParameter);
            _parameters.add(indirectParameter);
        }

        getRunner().notifyConfigurationUpdated();
    }

    public void removeParametersForSuite(BenchmarkSuiteInstance suite) {
        String parameterNamePrefix = suite.getName() + ".";
        for (int index = _parameters.size() - 1; index >= 0; index--) {
            Parameter parameter = _parameters.get(index);
            // Remove parameter from the list
            if (parameter.getName().startsWith(parameterNamePrefix)) {
                _parameters.remove(index);
            }
        }

        getRunner().notifyConfigurationUpdated();
    }

    public void setConfigurationToDefault() {
        for (Parameter parameter : _parameters) {
            if (parameter instanceof IndirectSuiteParameter) {
                IndirectSuiteParameter indirectParameter = (IndirectSuiteParameter)parameter;
                indirectParameter.setValue(indirectParameter.getDefaultValue());
            }
        }
    }

    public void setConfiguration(Map<String, String> parameters) {
        for (Parameter parameter : _parameters) {
            if (parameters.containsKey(parameter.getName()))
                parameter.setValue(parameters.get(parameter.getName()));
        }
    }
 
}
