package org.pipbenchmark.runner.params;

import java.util.*;

import org.pipbenchmark.*;
import org.pipbenchmark.runner.benchmarks.*;
import org.pipbenchmark.runner.config.*;
import org.pipbenchmark.util.Properties;

import java.io.*;

public class ParametersManager {
    private List<Parameter> _parameters = new ArrayList<Parameter>();
    private ConfigurationManager _configuration;

    public ParametersManager(ConfigurationManager configuration) {
    	_configuration = configuration;
    	
        _parameters.add(new NumberOfThreadsParameter(configuration));
        _parameters.add(new MeasurementTypeParameter(configuration));
        _parameters.add(new NominalRateParameter(configuration));
        _parameters.add(new ExecutionTypeParameter(configuration));
        _parameters.add(new DurationParameter(configuration));
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

        _configuration.notifyChanged();
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
                = new BenchmarkSuiteParameter(suite, originalParameter);
            _parameters.add(indirectParameter);
        }

        _configuration.notifyChanged();
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

        _configuration.notifyChanged();
    }

    public void setConfigurationToDefault() {
        for (Parameter parameter : _parameters) {
            if (parameter instanceof BenchmarkSuiteParameter) {
                BenchmarkSuiteParameter indirectParameter = (BenchmarkSuiteParameter)parameter;
                indirectParameter.setValue(indirectParameter.getDefaultValue());
            }
        }

        _configuration.notifyChanged();
    }

    public void setConfiguration(Map<String, String> parameters) {
        for (Parameter parameter : _parameters) {
            if (parameters.containsKey(parameter.getName()))
                parameter.setValue(parameters.get(parameter.getName()));
        }

        _configuration.notifyChanged();
    }
 
}
