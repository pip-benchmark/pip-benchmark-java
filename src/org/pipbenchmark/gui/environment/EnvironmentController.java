package org.pipbenchmark.gui.environment;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.pipbenchmark.gui.shell.*;
import org.pipbenchmark.runner.*;

public class EnvironmentController extends AbstractChildController
	implements IEnvironmentViewListener {
	
    private IEnvironmentView _view;
    private BenchmarkRunner _model;

    public EnvironmentController(MainController mainController, IEnvironmentView view) {
        super(mainController);

        _view = view;
        _view.setListener(this);
        _model = mainController.getModel();

        updateView();
    }

    private void updateView() {
        _view.setSystemInformation(getSystemInformation());
        _view.setCpuPerformance(
        	String.format("%.2f", _model.getEnvironment().getCpuMeasurement()));
        _view.setVideoPerformance(
        	String.format("%.2f", _model.getEnvironment().getVideoMeasurement()));
        _view.setDiskPerformance(
        	String.format("%.2f", _model.getEnvironment().getDiskMeasurement()));
    }

    private List<EnvironmentParameter> getSystemInformation() {
        List<EnvironmentParameter> result = new ArrayList<EnvironmentParameter>();
        for (Map.Entry<String, String> pair : _model.getEnvironment().getSystemInfo().entrySet()) {
            result.add(new EnvironmentParameter(pair.getKey(), pair.getValue()));
        }
        return result;
    }

    public void updateSystemBenchmark() {
    	ProgressMonitorDialog dialog = new ProgressMonitorDialog(getMainView().getHandler());
    	try {
    		IRunnableWithProgress runnable = new IRunnableWithProgress() {
				public void run(IProgressMonitor monitor)
					throws InvocationTargetException, InterruptedException {
					monitor.setTaskName("Updating System Benchmark\n\n"
						+ "Benchmarking process may take up to 2 mins. Please wait...");
			        _model.getEnvironment().measure(true, true, true);
			        updateView();
			        monitor.done();
				}
    		};
    		dialog.run(false, true, runnable);
    	} catch (Exception ex) {
    		getMainController().showErrorDialog("Benchmarking Environment",
    				"Updating system benchmarks failed", ex);
    	}
    }

    public void updateSystemBenchmarkClicked() {
        updateSystemBenchmark();
    }

}
