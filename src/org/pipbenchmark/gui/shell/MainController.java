package org.pipbenchmark.gui.shell;

import org.eclipse.core.runtime.*;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.pipbenchmark.gui.benchmarks.*;
import org.pipbenchmark.gui.environment.*;
import org.pipbenchmark.gui.execution.*;
import org.pipbenchmark.gui.params.*;
import org.pipbenchmark.gui.results.*;
import org.pipbenchmark.runner.*;

public class MainController implements IMainViewListener {
    private IMainView _view;
    private BenchmarkRunner _model;
    private InitializationController _initializationController;
    private ParametersController _configurationController;
    private ExecutionController _executionController;
    private ResultsController _resultsController;
    private EnvironmentController _environmentController;

    public MainController(IMainView view, BenchmarkRunner model) {
        _view = view;
        _view.setListener(this);
        _model = model;

        initializeControllers();
    }

    private void initializeControllers() {
        _initializationController = new InitializationController(
        	this, _view.getInitializationView());
        _configurationController = new ParametersController(this, _view.getConfigurationView());
        _executionController = new ExecutionController(this, _view.getExecutionView());
        _resultsController = new ResultsController(this, _view.getResultsView());
        _environmentController = new EnvironmentController(this, _view.getEnvironmentView());
    }

    public IMainView getView() {
        return _view;
    }

    public BenchmarkRunner getModel() {
        return _model;
    }

    public InitializationController getInitializationController() {
        return _initializationController;
    }

    public ParametersController getConfigurationController() {
        return _configurationController;
    }

    public ExecutionController getExecutionController() {
        return _executionController;
    }

    public ResultsController getResultsController() {
        return _resultsController;
    }

    public EnvironmentController getEnvironmentController() {
        return _environmentController;
    }

    public void loadSuiteClicked() {
        _view.setSelectedView("Initialization");
        _initializationController.loadSuitesFromLibrary();
    }

    public void saveReportClicked() {
        _view.setSelectedView("Results");
        _resultsController.saveReport();
    }

    public void printReportClicked() {
        _view.setSelectedView("Results");
        _resultsController.printReport();
    }

    public void loadConfigurationClicked() {
        _view.setSelectedView("Configuration");
        _configurationController.loadData();
    }

    public void saveConfigurationClicked() {
        _view.setSelectedView("Configuration");
        _configurationController.saveData();
    }

    public void startBenchmarkingClicked() {
        _view.setSelectedView("Execution");
        _executionController.startBenchmarking();
    }

    public void stopBenchmarkingClicked() {
        _view.setSelectedView("Execution");
        _executionController.stopBenchmarking();
    }

    public void benchmarkEnvironmentClicked() {
        _view.setSelectedView("Environment");
        _environmentController.updateSystemBenchmark();
    }

    public void exitClicked() {
        closeApplication();
    }

    public void aboutClicked() {
    	MessageDialog.openInformation(_view.getHandler(), "About",
    		  "                          Pip.Benchmark\n\n"
    		+ "            (c) Digital Living Software Corp. 2009-2010\n"
			+ "            (c) Conceptual Vision Consulting LLC 2017"
		);
    }

    public void formExited() {
    	if (_model.isRunning()) {
    		_executionController.stopBenchmarking();
//    		try {
//    			Thread.sleep(1000);
//    		} catch (InterruptedException ex) {
//    			// Ignore...
//    		}
    	}
    }

    public void closeApplication() {
        _executionController.stopBenchmarking();
    	_view.getHandler().close();
    }

    public void setStatusMessage(Object message) {
    	_view.getHandler().getDisplay().asyncExec(new StatusUpdater(message));
    }
    
    private class StatusUpdater implements Runnable {
    	private Object _message;
    	
    	public StatusUpdater(Object message) {
    		_message = message;
    	}
    	
    	public void run() {
            _view.setStatusMessage(_message != null ? _message.toString() : null);
    	}
    }
    
    public void showErrorDialog(String title, String message, Exception ex) {
    	// Todo: perhaps we need to call it from UI thread...
    	ErrorDialog.openError(_view.getHandler(), title, message,
    		new Status(IStatus.ERROR, "some plugin", 0, ex.getMessage(), ex));
    	
    	_view.setStatusMessage("Error: " + message);
    }
}
