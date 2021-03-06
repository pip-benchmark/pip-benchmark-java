package org.pipbenchmark.gui.benchmarks;

import java.io.*;
import java.util.*;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.*;
import org.pipbenchmark.gui.shell.*;
import org.pipbenchmark.runner.*;
import org.pipbenchmark.runner.benchmarks.BenchmarkInstance;
import org.pipbenchmark.runner.benchmarks.BenchmarkSuiteInstance;

public class BenchmarksController extends AbstractChildController
	implements IBenchmarksViewListener {
	
    private IBenchmarksView _view;
    private BenchmarkRunner _model;
    private FileDialog _loadSuiteDialog;

    public BenchmarksController(
    	MainController mainController, IBenchmarksView view) {
    	
        super(mainController);

        _view = view;
        _view.setListener(this);
        _model = mainController.getModel();

        _loadSuiteDialog = new FileDialog(getMainView().getHandler(), SWT.OPEN);
        _loadSuiteDialog.setFilterNames(new String[] {"Java JAR Files", "All Files"});
        _loadSuiteDialog.setFilterExtensions(new String[] {"*.jar", "*.*"});
        _loadSuiteDialog.setText("Load Suite From JAR");
        _loadSuiteDialog.setFilterIndex(0);
        _loadSuiteDialog.setFilterPath(System.getProperty("user.dir"));

        updateView();
    }

    public void updateView() {
        _view.setAllSuites(_model.getBenchmarks().getSuites());
        _view.setAllBenchmarks(getAllBenchmarks());
        _view.refreshData();
    }

    private List<BenchmarkInstance> getAllBenchmarks() {
        List<BenchmarkInstance> benchmarks = new ArrayList<BenchmarkInstance>();
        for (BenchmarkSuiteInstance suite : _model.getBenchmarks().getSuites()) {
            for (BenchmarkInstance benchmark : suite.getBenchmarks()) {
                benchmarks.add(benchmark);
            }
        }
        return benchmarks;
    }

    public void loadSuitesFromLibrary() {
    	try {
	    	String fileName = _loadSuiteDialog.open();
	        if (fileName != null) {
	            int numberOfSuites = _model.getBenchmarks().getSuites().size();
	            _model.getBenchmarks().addSuitesFromLibrary(fileName);

	            updateView();
	
	            if (numberOfSuites < _model.getBenchmarks().getSuites().size())
	                getMainController().setStatusMessage(
	                	String.format("Loaded suites from %s", fileName));
	            else
	                getMainController().setStatusMessage("No suites were founded to load");
	        }
    	} catch (IOException ex) {
    		getMainController().showErrorDialog("Error",
    			"Cannot load suites from specified library", ex);
    	}
    }

    public void unloadAllSuites() {
        _model.getBenchmarks().clear();

        updateView();
        
        getMainController().setStatusMessage("Unloaded all suites");
    }

    public void loadSuiteClicked() {
        loadSuitesFromLibrary();
    }

    public void unloadSuiteClicked() {
        int numberOfSuites = _model.getBenchmarks().getSuites().size();
        for (BenchmarkSuiteInstance suite : _view.getSelectedSuites())
            _model.getBenchmarks().removeSuite(suite.getName());

        updateView();

        if (numberOfSuites > _model.getBenchmarks().getSuites().size()) {
            getMainController().setStatusMessage("Unloaded specified suites");
        } else {
            getMainController().setStatusMessage("No suites were unloaded");
        }
    }

    public void unloadAllSuitesClicked() {
        unloadAllSuites();
    }

    public void selectBenchmarkClicked() {
        for (BenchmarkInstance benchmark : _view.getSelectedBenchmarks())
            benchmark.setSelected(true);

        _view.refreshData();
    }

    public void selectAllBenchmarksClicked() {
        for (BenchmarkInstance benchmark : _view.getAllBenchmarks())
            benchmark.setSelected(true);

        _view.refreshData();
    }

    public void unselectBenchmarkClicked() {
        for (BenchmarkInstance benchmark : _view.getSelectedBenchmarks())
            benchmark.setSelected(false);

        _view.refreshData();
    }

    public void unselectAllBenchmarksClicked() {
        for (BenchmarkInstance benchmark : _view.getAllBenchmarks())
            benchmark.setSelected(false);

            _view.refreshData();
    }

    public void suiteSelectedChanged() {
        _view.setAllBenchmarks(getAllBenchmarks());
    }
}
