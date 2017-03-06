package org.pipbenchmark.gui.shell;

import org.eclipse.swt.widgets.*;
import org.pipbenchmark.gui.config.*;
import org.pipbenchmark.gui.environment.*;
import org.pipbenchmark.gui.execution.*;
import org.pipbenchmark.gui.initialization.*;
import org.pipbenchmark.gui.result.*;

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
