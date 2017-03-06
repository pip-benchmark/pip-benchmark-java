package com.digitalliving.nbenchmark;

import java.util.*;

public class ConfigurationParameterCollection extends ArrayList<ConfigurationParameter> {
	private static final long serialVersionUID = 6277244765306555922L;

	public ConfigurationParameter addNew(String name, String description, String defaultValue) {
        ConfigurationParameter parameter = new ConfigurationParameter(name, description, defaultValue);
        add(parameter);
        return parameter;
    }

    public ConfigurationParameter findByName(String name) {
        for (ConfigurationParameter parameter : this) {
            if (parameter.getName().equalsIgnoreCase(name)) {
                return parameter;
            }
        }
        return null;
    }

    public void setDefaultValues() {
        for (ConfigurationParameter parameter : this) {
            parameter.setValue(parameter.getDefaultValue());
        }
    }

    public void setValues(Iterable<ConfigurationParameter> parameters) {
        for (ConfigurationParameter parameter : parameters) {
            ConfigurationParameter thisParameter = findByName(parameter.getName());
            if (thisParameter != null) {
                thisParameter.setValue(parameter.getValue());
            }
        }
    }

    public void setValues(Map<String, String> parameters) {
        for (Map.Entry<String, String> parameter : parameters.entrySet()) {
            ConfigurationParameter thisParameter = findByName(parameter.getKey());
            if (thisParameter != null) {
                thisParameter.setValue(parameter.getValue());
            }
        }
    }
}
