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
 
    public List<Parameter> getUserDefined() {
        List<Parameter> parameters = new ArrayList<Parameter>();
        for (Parameter parameter : _parameters) {
            if (!parameter.getName().endsWith(".Selected") 
        		&& !parameter.getName().endsWith(".Proportion")
                && !parameter.getName().startsWith("General.")) {
                parameters.add(parameter);
            }
        }
        return parameters; 
    }

    public List<Parameter> getAll() {
        return _parameters;
    }

    public void loadFromFile(String path) throws IOException {
        Properties properties = new Properties();
        FileInputStream stream = new FileInputStream(path);
        try {
            properties.loadFromStream(stream);
            for (Map.Entry<String, String> pair : properties.entrySet()) {
                for (Parameter parameter : _parameters) {
                    if (parameter.getName().equals(pair.getKey())) {
                        parameter.setValue(pair.getValue());
                    }
                }
            }
        } finally {
        	stream.close();
        }

        _configuration.notifyChanged();
    }

    public void saveToFile(String path) throws IOException {
        Properties properties = new Properties();
        FileOutputStream stream = new FileOutputStream(path);
        try {
            for (Parameter parameter : _parameters) {
                properties.put(parameter.getName(), parameter.getValue());
            }
            properties.saveToStream(stream);
        } finally {
        	stream.close();
        }
    }

    public void addSuite(BenchmarkSuiteInstance suite) {
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
        for (Parameter originalParameter : suite.getParameters()) {
            Parameter parameter = new BenchmarkSuiteParameter(suite, originalParameter);
            _parameters.add(parameter);
        }

        _configuration.notifyChanged();
    }

    public void removeSuite(BenchmarkSuiteInstance suite) {
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

    public void setToDefault() {
        for (Parameter parameter : _parameters) {
            if (parameter instanceof BenchmarkSuiteParameter) {
                BenchmarkSuiteParameter suiteParameter = (BenchmarkSuiteParameter)parameter;
                suiteParameter.setValue(suiteParameter.getDefaultValue());
            }
        }

        _configuration.notifyChanged();
    }

    public void set(Map<String, String> parameters) {
        for (Parameter parameter : _parameters) {
            if (parameters.containsKey(parameter.getName()))
                parameter.setValue(parameters.get(parameter.getName()));
        }

        _configuration.notifyChanged();
    }
 
}
