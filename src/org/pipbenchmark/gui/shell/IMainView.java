package org.pipbenchmark.gui.shell;

import org.eclipse.swt.widgets.*;
import org.pipbenchmark.gui.benchmarks.*;
import org.pipbenchmark.gui.environment.*;
import org.pipbenchmark.gui.execution.*;
import org.pipbenchmark.gui.params.*;
import org.pipbenchmark.gui.results.*;

public interface IMainView {
    Shell getHandler();
    void setFormTitle(String title);
    void setStatusMessage(String message);
    void setSelectedView(String viewName);
    
    IInitializationView getInitializationView();
    IParametersView getConfigurationView();
    IExecutionView getExecutionView();
    IResultsView getResultsView();
    IEnvironmentView getEnvironmentView();
    
    void setListener(IMainViewListener listener);    
}
