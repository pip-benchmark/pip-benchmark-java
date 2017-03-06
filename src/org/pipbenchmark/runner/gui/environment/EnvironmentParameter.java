package org.pipbenchmark.runner.gui.environment;

public class EnvironmentParameter {
    private String _parameter;
    private String _value;

    public EnvironmentParameter(String parameter, String value) {
        _parameter = parameter;
        _value = value;
    }

    public String getParameter() {
        return _parameter;
    }

    public String getValue() {
        return _value;
    }
}
