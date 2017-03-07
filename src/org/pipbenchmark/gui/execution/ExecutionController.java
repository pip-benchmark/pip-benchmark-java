package org.pipbenchmark.gui.execution;

import java.util.*;

import org.pipbenchmark.gui.shell.*;
import org.pipbenchmark.runner.BenchmarkRunner;
import org.pipbenchmark.runner.config.ExecutionType;
import org.pipbenchmark.runner.config.MeasurementType;
import org.pipbenchmark.runner.execution.ExecutionState;
import org.pipbenchmark.runner.execution.IExecutionListener;
import org.pipbenchmark.runner.results.BenchmarkResult;
import org.pipbenchmark.runner.results.IResultListener;

import java.text.*;

public class ExecutionController extends AbstractChildController
	implements IExecutionViewListener, IResultListener, IExecutionListener {
	
	private SimpleDateFormat _dateFormat = new SimpleDateFormat("HH:mm:ss"); 
    private boolean _updatingView = false;
    private IExecutionView _view;
    private BenchmarkRunner _model;
    private Map<String, ExecutionResult> _results = new HashMap<String, ExecutionResult>();

    public ExecutionController(MainController mainController, IExecutionView view) {
        super(mainController);

        _view = view;
        _view.setListener(this);

        _model = mainController.getModel();
        _model.getResults().addUpdatedListener(this);

        updateView();
    }

    public void updateView() {
        _updatingView = true;
        try {
            _view.setNumberOfThreads(_model.getConfiguration().getNumberOfThreads());
            _view.setMeasurementType(_model.getConfiguration().getMeasurementType());
            _view.setNominalRate(_model.getConfiguration().getNominalRate());
            _view.setExecutionType(_model.getConfiguration().getExecutionType());
            _view.setDuration(_model.getConfiguration().getDuration());
        } finally {
            _updatingView = false;
        }
    }

    public void startBenchmarking() {
        if (!_model.isRunning()) {
            getMainController().setStatusMessage("Benchmarking...");

            _results.clear();

            _model.getConfiguration().setNumberOfThreads(_view.getNumberOfThreads());
            _model.getConfiguration().setMeasurementType(_view.getMeasurementType());
            _model.getConfiguration().setNominalRate(_view.getNominalRate());
            _model.getConfiguration().setExecutionType(_view.getExecutionType());
            _model.getConfiguration().setDuration(_view.getDuration());

            try {
                _model.start();
            } catch (Exception ex) {
                getMainController().showErrorDialog("Start Benchmarking",
                    "Benchmarking process failed", ex);
            }
            
            _view.setBenchmarkActionButton("Stop");
            _view.setShowPerformanceChart(
            	_model.getConfiguration().getExecutionType() == ExecutionType.Proportional);
            _view.setExecutionResults(new ArrayList<ExecutionResult>(_results.values()));
        }
    }

    public void stopBenchmarking() {
        if (_model.isRunning()) {
            //getMainController().setStatusMessage("Benchmarking completed.");

            _model.stop();
            
            _view.setBenchmarkActionButton("Start");
            //getMainController().resultsController.generateReport();
            //getMainController().getView().setSelectedView("Results");
        }
    }

    public void benchmarkActionClicked() {
        if (_model.isRunning()) {
            stopBenchmarking();
        } else {
            startBenchmarking();
        }
    }

    public void dataUpdated() {
        if (_model.isRunning()) {
            getMainController().setStatusMessage("Benchmarking completed.");
            _model.stop();
            _view.setBenchmarkActionButton("Start");
            getMainController().getResultsController().generateReport();
        }

        if (!_updatingView) {
            _model.getConfiguration().setNumberOfThreads(_view.getNumberOfThreads());
            _model.getConfiguration().setMeasurementType(_view.getMeasurementType());
            _model.getConfiguration().setNominalRate(_view.getNominalRate());
            _model.getConfiguration().setExecutionType(_view.getExecutionType());
            _model.getConfiguration().setDuration(_view.getDuration());
        }
    }

	public void configurationUpdated() {}
    
	public void onResultUpdated(BenchmarkResult result) {
		getMainView().getHandler().getDisplay().asyncExec(
			new ResultUpdater(result)
		);
    }

	public void onStateUpdated(ExecutionState state) {
		getMainView().getHandler().getDisplay().asyncExec(
			new StateUpdater(state)
		);
    }

	private class ResultUpdater implements Runnable {
		private BenchmarkResult _result;
		
		public ResultUpdater(BenchmarkResult result) {
			_result = result;
		}

		public void run() {
			if (getMainView().getHandler().getDisplay().isDisposed()) 
				return;
			
            _view.setStartTime(formatDate(_result.getStartTime()));
            _view.setElapsedTime(formatTime(_result.getElapsedTime()));
            _view.setEndTime(formatDate(_result.getStartTime() + _result.getElapsedTime()));
            _view.setMinPerformance(formatNumber(_result.getPerformanceMeasurement().getMinValue()));
            _view.setAveragePerformance(formatNumber(_result.getPerformanceMeasurement().getAverageValue()));
            _view.setMaxPerformance(formatNumber(_result.getPerformanceMeasurement().getMaxValue()));
            _view.setMinCpuLoad(formatNumber(_result.getCpuLoadMeasurement().getMinValue()));
            _view.setAverageCpuLoad(formatNumber(_result.getCpuLoadMeasurement().getAverageValue()));
            _view.setMaxCpuLoad(formatNumber(_result.getCpuLoadMeasurement().getMaxValue()));
            _view.setMinMemoryUsage(formatNumber(_result.getMemoryUsageMeasurement().getMinValue()));
            _view.setAverageMemoryUsage(formatNumber(_result.getMemoryUsageMeasurement().getAverageValue()));
            _view.setMaxMemoryUsage(formatNumber(_result.getMemoryUsageMeasurement().getMaxValue()));

            String resultName = _result.getBenchmarks().size() != 1
            	? "All" : _result.getBenchmarks().get(0).getFullName();
            ExecutionResult executionResult = _results.get(resultName);
            if (executionResult != null)
                executionResult.update(_result);
            else {
                executionResult = new ExecutionResult(_result);
                _results.put(resultName, executionResult);
            }            
            _view.setExecutionResults(
            	new ArrayList<ExecutionResult>(_results.values()));

            _view.addCurrentPerformancePoint(
            	_model.getConfiguration().getMeasurementType() == MeasurementType.Peak
                ? _result.getPerformanceMeasurement().getCurrentValue()
                : _result.getCpuLoadMeasurement().getCurrentValue());
        }

		private String formatNumber(double value) {
			return String.format("%.2f", value);
		}
		
		private String formatDate(long ticks) {
			return _dateFormat.format(new Date(ticks));
		}
		
		private String formatTime(long ticks) {
			long millis = ticks % 1000;
			long seconds = (ticks / 1000) % 60;
			long minutes = (ticks / 1000 / 60) % 60;
			long hours = ticks / 1000 / 60 / 60;
			return String.format("%02d:%02d:%02d.%d", hours, minutes, seconds, millis);
		}
	}
	
	private class StateUpdater implements Runnable {
		private ExecutionState _state = ExecutionState.Initial;
		
		public StateUpdater(ExecutionState state) {
			_state = state;
		}

		public void run() {
			if (getMainView().getHandler().getDisplay().isDisposed()) 
				return;
			
            if (_model.getConfiguration().getMeasurementType() == MeasurementType.Peak)
                _view.setPerformanceChartName("Performance Chart (tps)");
            else
                _view.setPerformanceChartName("Cpu Load Chart (%)");

            if (_state == ExecutionState.Running) {
                _view.clearPerformancePoints();
            } else if (_state == ExecutionState.Completed) {
                getMainController().setStatusMessage("Benchmarking completed.");
                _view.setBenchmarkActionButton("Start");
                getMainController().getResultsController().generateReport();
                getMainController().getView().setSelectedView("Results");
            }
        }
	}	
}
