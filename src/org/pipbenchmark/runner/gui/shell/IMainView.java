package org.pipbenchmark.runner.gui.shell;

import org.eclipse.swt.widgets.*;
import org.pipbenchmark.runner.gui.config.*;
import org.pipbenchmark.runner.gui.environment.*;
import org.pipbenchmark.runner.gui.execution.*;
import org.pipbenchmark.runner.gui.initialization.*;
import org.pipbenchmark.runner.gui.result.*;

public interface IMainView {
    Shell getHandler();
    void setFormTitle(String title);
    void setStatusMessage(String message);
    void setSelectedView(String viewName);
    
    IInitializationView getInitializationView();
    IConfigurationView getConfigurationView();
    IExecutionView getExecutionView();
    IResultsView getResultsView();
    IEnvironmentView getEnvironmentView();
    
    void setListener(IMainViewListener listener);    
}
