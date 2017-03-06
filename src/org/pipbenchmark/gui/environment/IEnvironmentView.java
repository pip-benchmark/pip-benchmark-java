package org.pipbenchmark.gui.environment;

import java.util.*;

public interface IEnvironmentView {
    void setSystemInformation(List<EnvironmentParameter> value);
    void setCpuPerformance(String value);
    void setVideoPerformance(String value);
    void setDiskPerformance(String value);
    
    void setListener(IEnvironmentViewListener listener);
}
