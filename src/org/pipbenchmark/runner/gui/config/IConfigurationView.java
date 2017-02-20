package org.pipbenchmark.runner.gui.config;

import java.util.*;

import org.pipbenchmark.*;

public interface IConfigurationView {
    void setConfiguration(List<Parameter> value);
    void setListener(IConfigurationViewListener listener);
    
    void refreshData();
}
