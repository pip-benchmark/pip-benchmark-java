package org.pipbenchmark.runner.gui.execution;

import java.util.*;

import org.pipbenchmark.*;
import org.pipbenchmark.runner.BenchmarkResult;
import org.pipbenchmark.runner.BenchmarkRunner;
import org.pipbenchmark.runner.ExecutionState;
import org.pipbenchmark.runner.ExecutionType;
import org.pipbenchmark.runner.IResultListener;
import org.pipbenchmark.runner.MeasurementType;
import org.pipbenchmark.runner.gui.shell.*;

import java.text.*;

public class ExecutionController extends AbstractChildController
	implements IExecutionViewListener, IResultListener {
	
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
        _model.addResultUpdatedListener(this);

        updateView();
    }

    public void updateView() {
        _updatingView = true;
        try {
            _view.setNumberOfThreads(_model.getNumberOfThreads());
            _view.setMeasurementType(_model.getMeasurementType());
            _view.setNominalRate(_model.getNominalRate());
            _view.setExecutionType(_model.getExecutionType());
            _view.setDuration(_model.getDuration());
        } finally {
            _updatingView = false;
        }
    }

    public void startBenchmarking() {
        if (!_model.isRunning()) {
            getMainController().setStatusMessage("Benchmarking...");

            _results.clear();

            _model.setNumberOfThreads(_view.getNumberOfThreads());
            _model.setMeasurementType(_view.getMeasurementType());
            _model.setNominalRate(_view.getNominalRate());
            _model.setExecutionType(_view.getExecutionType());
            _model.setDuration(_view.getDuration());

            try {
                _model.start();
            } catch (Exception ex) {
                getMainController().showErrorDialog("Start Benchmarking",
                    "Benchmarking process failed", ex);
            }
            
            _view.setBenchmarkActionButton("Stop");
            _view.setShowPerformanceChart(
            	_model.getExecutionType() == ExecutionType.Proportional);
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
            _model.setNumberOfThreads(_view.getNumberOfThreads());
            _model.setMeasurementType(_view.getMeasurementType());
            _model.setNominalRate(_view.getNominalRate());
            _model.setExecutionType(_view.getExecutionType());
            _model.setDuration(_view.getDuration());
        }
    }

	public void configurationUpdated() {}
    
	public void onResultUpdated(ExecutionState status, BenchmarkResult result) {
		getMainView().getHandler().getDisplay().asyncExec(
			new StatusUpdater(status, result));
    }
	
	private class StatusUpdater implements Runnable {
		private ExecutionState _status;
		private BenchmarkResult _result;
		
		public StatusUpdater(ExecutionState status, BenchmarkResult result) {
			_status = status;
			_result = result;
		}

		public void run() {
			if (getMainView().getHandler().getDisplay().isDisposed()) 
				return;
			
            if (_result != null) {
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
            }

            if (_model.getMeasurementType() == MeasurementType.Peak)
                _view.setPerformanceChartName("Performance Chart (tps)");
            else
                _view.setPerformanceChartName("Cpu Load Chart (%)");

            if (_status == ExecutionState.Starting) {
                _view.clearPerformancePoints();
            } else if (_status == ExecutionState.Running) {
                if (_result != null) {
                    _view.addCurrentPerformancePoint(
                    	_model.getMeasurementType() == MeasurementType.Peak
                        ? _result.getPerformanceMeasurement().getCurrentValue()
                        : _result.getCpuLoadMeasurement().getCurrentValue());
                }
            } else if (_status == ExecutionState.Completed) {
                getMainController().setStatusMessage("Benchmarking completed.");
                _view.setBenchmarkActionButton("Start");
                getMainController().getResultsController().generateReport();
                getMainController().getView().setSelectedView("Results");
            }
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
	
}
