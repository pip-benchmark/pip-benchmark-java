package org.pipbenchmark.gui.params;

import java.io.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.*;
import org.pipbenchmark.gui.shell.*;
import org.pipbenchmark.runner.*;
import org.pipbenchmark.runner.results.BenchmarkResult;
import org.pipbenchmark.runner.results.IResultListener;

public class ParametersController extends AbstractChildController
	implements IParametersViewListener, IResultListener {
    private IParametersView _view;
    private BenchmarkRunner _model;
    private FileDialog _loadFromFileDialog;
    private FileDialog _saveToFileDialog;

    public ParametersController(MainController mainController, IParametersView view) {
        super(mainController);

        _view = view;
        _view.setListener(this);

        _model = mainController.getModel();
        _model.getResults().addUpdatedListener(this);

        initializeDialogs();
        updateView();
    }

    private void initializeDialogs() {
        _loadFromFileDialog = new FileDialog(getMainView().getHandler(), SWT.OPEN);
        _loadFromFileDialog.setFilterExtensions(new String[] {"*.properties", "*.*"});
        _loadFromFileDialog.setFilterNames(new String[] {"Property Files", "All Files"});
        _loadFromFileDialog.setFilterIndex(0);
        _loadFromFileDialog.setText("Load Parameters");

        _saveToFileDialog = new FileDialog(getMainView().getHandler(), SWT.SAVE);
        _saveToFileDialog.setFilterExtensions(new String[] {"*.properties", "*.*"});
        _saveToFileDialog.setFilterNames(new String[] {"Property Files", "All Files"});
        _saveToFileDialog.setFilterIndex(0);
        _saveToFileDialog.setText("Save Parameters");
        _saveToFileDialog.setFileName("BenchmarkConfiguration.properties");
    }

    private void updateView() {
        _view.setData(_model.getParameters().getUserDefined());
    }

    public void loadData() {
    	try {
	    	String fileName = _loadFromFileDialog.open();
	        if (fileName != null) {
	            _model.getParameters().loadFromFile(fileName);
	            updateView();
	            getMainController().getExecutionController().updateView();
	            getMainController().getInitializationController().updateView();
	        }
    	} catch (IOException ex) {
    		
    	}
    }

    public void saveData() {
    	try {
	    	String fileName = _saveToFileDialog.open();
	        if (fileName != null) {
	            _model.getParameters().saveToFile(fileName);
	        }
    	} catch (IOException ex) {
    		getMainController().showErrorDialog("Error",
    			"Failed to save parameters", ex);
    	}
    }

	public void onResultUpdated(BenchmarkResult result) {
	}
	
	public void configurationUpdated() {
        updateView();
    }

    public void loadFromFileClicked() {
        loadData();
    }

    public void saveToFileClicked() {
        saveData();
    }

    public void setToDefaultClicked() {
        _model.getParameters().setToDefault();
        _view.refreshData();
    }
}
