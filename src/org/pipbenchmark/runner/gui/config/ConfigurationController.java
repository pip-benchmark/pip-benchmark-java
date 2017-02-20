package org.pipbenchmark.runner.gui.config;

import java.io.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.*;
import org.pipbenchmark.runner.*;
import org.pipbenchmark.runner.gui.shell.*;

public class ConfigurationController extends AbstractChildController
	implements IConfigurationViewListener, IResultListener {
    private IConfigurationView _view;
    private BenchmarkRunner _model;
    private FileDialog _loadConfigurationDialog;
    private FileDialog _saveConfigurationDialog;

    public ConfigurationController(MainController mainController, IConfigurationView view) {
        super(mainController);

        _view = view;
        _view.setListener(this);

        _model = mainController.getModel();
        _model.addResultUpdatedListener(this);

        initializeDialogs();
        updateView();
    }

    private void initializeDialogs() {
        _loadConfigurationDialog = new FileDialog(getMainView().getHandler(), SWT.OPEN);
        _loadConfigurationDialog.setFilterExtensions(new String[] {"*.properties", "*.*"});
        _loadConfigurationDialog.setFilterNames(new String[] {"Property Files", "All Files"});
        _loadConfigurationDialog.setFilterIndex(0);
        _loadConfigurationDialog.setText("Load Configuration Properties");

        _saveConfigurationDialog = new FileDialog(getMainView().getHandler(), SWT.SAVE);
        _saveConfigurationDialog.setFilterExtensions(new String[] {"*.properties", "*.*"});
        _saveConfigurationDialog.setFilterNames(new String[] {"Property Files", "All Files"});
        _saveConfigurationDialog.setFilterIndex(0);
        _saveConfigurationDialog.setText("Save Configuration Properties");
        _saveConfigurationDialog.setFileName("BenchmarkConfiguration.properties");
    }

    private void updateView() {
        _view.setConfiguration(_model.getConfiguration());
    }

    public void loadConfiguration() {
    	try {
	    	String fileName = _loadConfigurationDialog.open();
	        if (fileName != null) {
	            _model.loadConfigurationFromFile(fileName);
	            updateView();
	            getMainController().getExecutionController().updateView();
	            getMainController().getInitializationController().updateView();
	        }
    	} catch (IOException ex) {
    		
    	}
    }

    public void saveConfiguration() {
    	try {
	    	String fileName = _saveConfigurationDialog.open();
	        if (fileName != null) {
	            _model.saveConfigurationToFile(fileName);
	        }
    	} catch (IOException ex) {
    		getMainController().showErrorDialog("Error",
    			"Failed to save configuration properties", ex);
    	}
    }

	public void onResultUpdated(ExecutionState status, BenchmarkResult result) {
	}
	
	public void configurationUpdated() {
        updateView();
    }

    public void loadConfigurationClicked() {
        loadConfiguration();
    }

    public void saveConfigurationClicked() {
        saveConfiguration();
    }

    public void setToDefaultClicked() {
        _model.setConfigurationToDefault();
        _view.refreshData();
    }
}
