package org.pipbenchmark.gui.params;

import java.util.*;

import org.pipbenchmark.*;

public interface IParametersView {
    void setData(List<Parameter> value);
    void setListener(IParametersViewListener listener);
    
    void refreshData();
}
