package org.pipbenchmark.runner.gui.execution;

import java.util.List;

import org.eclipse.jface.viewers.*;
import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.pipbenchmark.*;
import org.pipbenchmark.runner.ExecutionType;
import org.pipbenchmark.runner.MeasurementType;
import org.pipbenchmark.runner.gui.charts.*;

public class ExecutionView extends Composite implements IExecutionView {
	private IExecutionViewListener _listener = null;
	private Spinner _numberOfThreadsSpinner;
	private Button _sequentialExecutionCheckbox;
	private Spinner _testDurationSpinner;
	private Button _peakPerformanceRadioButton;
	private Button _nominalPerformanceRadioButton;
	private Spinner _nominalPerformanceSpinner;
	private Button _actionButton;
	private Button _showChartCheckbox;
	private Text _startTimeText;
	private Text _endTimeText;
	private Text _elapsedTimeText;
	private Text _minPerformanceText;
	private Text _averagePerformanceText;
	private Text _maxPerformanceText;
	private Text _minCpuText;
	private Text _averageCpuText;
	private Text _maxCpuText;
	private Text _minMemoryText;
	private Text _averageMemoryText;
	private Text _maxMemoryText;
	private Group _resultsGroup;
	private Table _resultsTable;
	private TableViewer _resultsTableViewer;
	private List<ExecutionResult> _results;	
	private PerformanceChart _performanceChart;
	
	public ExecutionView(Composite parent) {
		super(parent, SWT.NONE);
		initializeComponent();
	}

	private void initializeComponent() {
		this.setLayout(new FormLayout());

		SelectionListener selectionListener = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent args) {
				if (_listener != null) {
					_listener.dataUpdated();
				}
			}
		};
		
		ModifyListener modifyListener = new ModifyListener() {
			public void modifyText(ModifyEvent args) {
				if (_listener != null) {
					_listener.dataUpdated();
				}
			}
		};
		
		_actionButton = new Button(this, SWT.PUSH);
		_actionButton.setText("Start");
		_actionButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent args) {
				if (_listener != null) {
					_listener.benchmarkActionClicked();
				}
			}
		});
		FormData data = new FormData();
		data.left = new FormAttachment(100, -80);
		data.right = new FormAttachment(100, -10);
		data.bottom = new FormAttachment(100, -10);
		_actionButton.setLayoutData(data);		
		
		Group settingsGroup = new Group(this, SWT.SHADOW_NONE);
		settingsGroup.setText("Benchmarking Settings");
		data = new FormData();
		data.top = new FormAttachment(0, 10);
		data.left = new FormAttachment(0, 10);
		data.right = new FormAttachment(100, -10);
		data.bottom = new FormAttachment(0, 91);
		settingsGroup.setLayoutData(data);
		settingsGroup.setLayout(new FormLayout());

		Label numberOfThreadsLabel = new Label(settingsGroup, SWT.NONE);
		numberOfThreadsLabel.setText("Number of Threads:");
		data = new FormData();
		data.top = new FormAttachment(0, 8);
		data.left = new FormAttachment(0, 10);
		numberOfThreadsLabel.setLayoutData(data);
	
		_numberOfThreadsSpinner = new Spinner(settingsGroup, SWT.BORDER);
		_numberOfThreadsSpinner.setValues(1, 1, 9999, 0, 1, 5);
		_numberOfThreadsSpinner.addModifyListener(modifyListener);
		data = new FormData();
		data.top = new FormAttachment(0, 5);
		data.left = new FormAttachment(numberOfThreadsLabel, 10);
		_numberOfThreadsSpinner.setLayoutData(data);

		_sequentialExecutionCheckbox = new Button(settingsGroup, SWT.CHECK);
		_sequentialExecutionCheckbox.setText("Execute Tests Sequentially For (sec):");
		_sequentialExecutionCheckbox.addSelectionListener(selectionListener);
		data = new FormData();
		data.top = new FormAttachment(0, 8);
		data.left = new FormAttachment(_numberOfThreadsSpinner, 15);
		_sequentialExecutionCheckbox.setLayoutData(data);

		_testDurationSpinner = new Spinner(settingsGroup, SWT.BORDER);
		_testDurationSpinner.setValues(60, 1, 9999, 0, 1, 5);
		_testDurationSpinner.addModifyListener(modifyListener);
		data = new FormData();
		data.top = new FormAttachment(0, 5);
		data.left = new FormAttachment(_sequentialExecutionCheckbox, 5);
		_testDurationSpinner.setLayoutData(data);
		
		Label testingModeLabel = new Label(settingsGroup, SWT.NONE);
		testingModeLabel.setText("Testing Mode:");
		data = new FormData();
		data.top = new FormAttachment(_numberOfThreadsSpinner, 8);
		data.left = new FormAttachment(0, 10);
		testingModeLabel.setLayoutData(data);

		_peakPerformanceRadioButton = new Button(settingsGroup, SWT.RADIO);
		_peakPerformanceRadioButton.setText("Peak Performance");
		_peakPerformanceRadioButton.setSelection(true);
		_peakPerformanceRadioButton.addSelectionListener(selectionListener);
		data = new FormData();
		data.top = new FormAttachment(_numberOfThreadsSpinner, 8);
		data.left = new FormAttachment(testingModeLabel, 20);
		_peakPerformanceRadioButton.setLayoutData(data);

		_nominalPerformanceRadioButton = new Button(settingsGroup, SWT.RADIO);
		_nominalPerformanceRadioButton.setText("Nominal Performance (tps):");
		_nominalPerformanceRadioButton.addSelectionListener(selectionListener);
		data = new FormData();
		data.top = new FormAttachment(_numberOfThreadsSpinner, 8);
		data.left = new FormAttachment(_peakPerformanceRadioButton, 5);
		_nominalPerformanceRadioButton.setLayoutData(data);

		_nominalPerformanceSpinner = new Spinner(settingsGroup, SWT.BORDER);
		_nominalPerformanceSpinner.setValues(100, 1, 999999, 2, 1, 100);
		_nominalPerformanceSpinner.addModifyListener(modifyListener);
		data = new FormData();
		data.top = new FormAttachment(_numberOfThreadsSpinner, 5);
		data.left = new FormAttachment(_nominalPerformanceRadioButton, 5);
		_nominalPerformanceSpinner.setLayoutData(data);
		
		_resultsGroup = new Group(this, SWT.SHADOW_NONE);
		_resultsGroup.setText("Benchmarking Results");
		data = new FormData();
		data.top = new FormAttachment(settingsGroup, 5);
		data.left = new FormAttachment(0, 10);
		data.right = new FormAttachment(100, -10);
//		data.bottom = new FormAttachment(settingsGroup, 105);
		_resultsGroup.setLayoutData(data);
		_resultsGroup.setLayout(new FormLayout());
		
		Label timeLabel = new Label(_resultsGroup, SWT.NONE);
		timeLabel.setText("Time");
		data = new FormData();
		data.top = new FormAttachment(0, 5);
		data.left = new FormAttachment(0, 60);
		timeLabel.setLayoutData(data);

		Label performanceLabel = new Label(_resultsGroup, SWT.NONE);
		performanceLabel.setText("Performance(tps)");
		data = new FormData();
		data.top = new FormAttachment(0, 5);
		data.left = new FormAttachment(0, 230);
		performanceLabel.setLayoutData(data);

		Label cpuLabel = new Label(_resultsGroup, SWT.NONE);
		cpuLabel.setText("CPU (%)");
		data = new FormData();
		data.top = new FormAttachment(0, 5);
		data.left = new FormAttachment(0, 335);
		cpuLabel.setLayoutData(data);

		Label memoryLabel = new Label(_resultsGroup, SWT.NONE);
		memoryLabel.setText("Memory (Mb)");
		data = new FormData();
		data.top = new FormAttachment(0, 8);
		data.left = new FormAttachment(0, 410);
		memoryLabel.setLayoutData(data);
		
		Label startLabel = new Label(_resultsGroup, SWT.NONE);
		startLabel.setText("Start:");
		data = new FormData();
		data.top = new FormAttachment(timeLabel, 5);
		data.left = new FormAttachment(0, 10);
		startLabel.setLayoutData(data);
		
		Color textBackgroundColor = this.getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND);
		
		_startTimeText = new Text(_resultsGroup, SWT.BORDER);
		_startTimeText.setEditable(false);
		_startTimeText.setBackground(textBackgroundColor);
		data = new FormData();
		data.top = new FormAttachment(timeLabel, 3);
		data.left = new FormAttachment(0, 60);
		data.right = new FormAttachment(0, 160);
		_startTimeText.setLayoutData(data);
		
		Label minLabel = new Label(_resultsGroup, SWT.NONE);
		minLabel.setText("Min:");
		data = new FormData();
		data.top = new FormAttachment(timeLabel, 5);
		data.left = new FormAttachment(0, 175);
		minLabel.setLayoutData(data);

		_minPerformanceText = new Text(_resultsGroup, SWT.BORDER);
		_minPerformanceText.setEditable(false);
		_minPerformanceText.setBackground(textBackgroundColor);
		data = new FormData();
		data.top = new FormAttachment(timeLabel, 3);
		data.left = new FormAttachment(0, 230);
		data.right = new FormAttachment(0, 330);
		_minPerformanceText.setLayoutData(data);

		_minCpuText = new Text(_resultsGroup, SWT.BORDER);
		_minCpuText.setEditable(false);
		_minCpuText.setBackground(textBackgroundColor);
		data = new FormData();
		data.top = new FormAttachment(timeLabel, 3);
		data.left = new FormAttachment(0, 335);
		data.right = new FormAttachment(0, 405);
		_minCpuText.setLayoutData(data);

		_minMemoryText = new Text(_resultsGroup, SWT.BORDER);
		_minMemoryText.setEditable(false);
		_minMemoryText.setBackground(textBackgroundColor);
		data = new FormData();
		data.top = new FormAttachment(timeLabel, 3);
		data.left = new FormAttachment(0, 410);
		data.right = new FormAttachment(0, 510);
		_minMemoryText.setLayoutData(data);

		Label endLabel = new Label(_resultsGroup, SWT.NONE);
		endLabel.setText("End:");
		data = new FormData();
		data.top = new FormAttachment(_startTimeText, 8);
		data.left = new FormAttachment(0, 10);
		endLabel.setLayoutData(data);
		
		_endTimeText = new Text(_resultsGroup, SWT.BORDER);
		_endTimeText.setEditable(false);
		_endTimeText.setBackground(textBackgroundColor);
		data = new FormData();
		data.top = new FormAttachment(_startTimeText, 5);
		data.left = new FormAttachment(0, 60);
		data.right = new FormAttachment(0, 160);
		_endTimeText.setLayoutData(data);
		
		Label averageLabel = new Label(_resultsGroup, SWT.NONE);
		averageLabel.setText("Average:");
		data = new FormData();
		data.top = new FormAttachment(_startTimeText, 8);
		data.left = new FormAttachment(0, 175);
		averageLabel.setLayoutData(data);

		_averagePerformanceText = new Text(_resultsGroup, SWT.BORDER);
		_averagePerformanceText.setEditable(false);
		_averagePerformanceText.setBackground(textBackgroundColor);
		data = new FormData();
		data.top = new FormAttachment(_startTimeText, 5);
		data.left = new FormAttachment(0, 230);
		data.right = new FormAttachment(0, 330);
		_averagePerformanceText.setLayoutData(data);

		_averageCpuText = new Text(_resultsGroup, SWT.BORDER);
		_averageCpuText.setEditable(false);
		_averageCpuText.setBackground(textBackgroundColor);
		data = new FormData();
		data.top = new FormAttachment(_startTimeText, 5);
		data.left = new FormAttachment(0, 335);
		data.right = new FormAttachment(0, 405);
		_averageCpuText.setLayoutData(data);

		_averageMemoryText = new Text(_resultsGroup, SWT.BORDER);
		_averageMemoryText.setEditable(false);
		_averageMemoryText.setBackground(textBackgroundColor);
		data = new FormData();
		data.top = new FormAttachment(_startTimeText, 5);
		data.left = new FormAttachment(0, 410);
		data.right = new FormAttachment(0, 510);
		_averageMemoryText.setLayoutData(data);

		Label elapsedLabel = new Label(_resultsGroup, SWT.NONE);
		elapsedLabel.setText("Elapsed:");
		data = new FormData();
		data.top = new FormAttachment(_endTimeText, 8);
		data.left = new FormAttachment(0, 10);
		elapsedLabel.setLayoutData(data);
		
		_elapsedTimeText = new Text(_resultsGroup, SWT.BORDER);
		_elapsedTimeText.setEditable(false);
		_elapsedTimeText.setBackground(textBackgroundColor);
		data = new FormData();
		data.top = new FormAttachment(_endTimeText, 5);
		data.left = new FormAttachment(0, 60);
		data.right = new FormAttachment(0, 160);
		_elapsedTimeText.setLayoutData(data);
		
		Label maxLabel = new Label(_resultsGroup, SWT.NONE);
		maxLabel.setText("Max:");
		data = new FormData();
		data.top = new FormAttachment(_endTimeText, 8);
		data.left = new FormAttachment(0, 175);
		maxLabel.setLayoutData(data);

		_maxPerformanceText = new Text(_resultsGroup, SWT.BORDER);
		_maxPerformanceText.setEditable(false);
		_maxPerformanceText.setBackground(textBackgroundColor);
		data = new FormData();
		data.top = new FormAttachment(_endTimeText, 5);
		data.left = new FormAttachment(0, 230);
		data.right = new FormAttachment(0, 330);
		_maxPerformanceText.setLayoutData(data);

		_maxCpuText = new Text(_resultsGroup, SWT.BORDER);
		_maxCpuText.setEditable(false);
		_maxCpuText.setBackground(textBackgroundColor);
		data = new FormData();
		data.top = new FormAttachment(_endTimeText, 5);
		data.left = new FormAttachment(0, 335);
		data.right = new FormAttachment(0, 405);
		_maxCpuText.setLayoutData(data);

		_maxMemoryText = new Text(_resultsGroup, SWT.BORDER);
		_maxMemoryText.setEditable(false);
		_maxMemoryText.setBackground(textBackgroundColor);
		data = new FormData();
		data.top = new FormAttachment(_endTimeText, 5);
		data.left = new FormAttachment(0, 410);
		data.right = new FormAttachment(0, 510);
		data.bottom = new FormAttachment(100, -8);
		_maxMemoryText.setLayoutData(data);
		
		_showChartCheckbox = new Button(this, SWT.CHECK);
		_showChartCheckbox.setText("Performance Chart (tps)");
		_showChartCheckbox.setSelection(true);
		data = new FormData();
		data.top = new FormAttachment(_resultsGroup, 5);
		data.left = new FormAttachment(0, 10);
		data.right = new FormAttachment(100, -10);
		_showChartCheckbox.setLayoutData(data);		

		_performanceChart = new PerformanceChart(this, SWT.BORDER);
		_performanceChart.setScaleMode(ScaleMode.Relative);
		data = new FormData();
		data.top = new FormAttachment(_showChartCheckbox, 5);
		data.left = new FormAttachment(0, 10);
		data.right = new FormAttachment(100, -10);
		data.bottom = new FormAttachment(_actionButton, -5);
		_performanceChart.setLayoutData(data);		
		
		_resultsTableViewer = new TableViewer(this,
			SWT.BORDER | SWT.MULTI | SWT.FULL_SELECTION);
		_resultsTable = _resultsTableViewer.getTable();
		_resultsTable.setVisible(false);
		data = new FormData();
		data.top = new FormAttachment(settingsGroup, 5);
		data.left = new FormAttachment(0, 10);
		data.right = new FormAttachment(100, -10);
		data.bottom = new FormAttachment(_actionButton, -5);
		_resultsTable.setLayoutData(data);		
		initializeResultsTable();
	}
	
	private void initializeResultsTable() {
		_resultsTable.setLinesVisible(true);
		_resultsTable.setHeaderVisible(true);
		
		TableLayout tableLayout = new TableLayout();
		tableLayout.addColumnData(new ColumnWeightData(40, 75, true));
		tableLayout.addColumnData(new ColumnWeightData(20, 50, true));
		tableLayout.addColumnData(new ColumnWeightData(20, 50, true));
		tableLayout.addColumnData(new ColumnWeightData(20, 50, true));
		_resultsTable.setLayout(tableLayout);

		TableColumn column1 = new TableColumn(_resultsTable, SWT.LEFT);
		column1.setText("Test Name");
		TableColumn column2 = new TableColumn(_resultsTable, SWT.LEFT);
		column2.setText("Performance (tps)");
		TableColumn column3 = new TableColumn(_resultsTable, SWT.LEFT);
		column3.setText("CPU (%)");
		TableColumn column4 = new TableColumn(_resultsTable, SWT.LEFT);
		column4.setText("Memory (Mb)");
		
		_resultsTableViewer.setLabelProvider(new ITableLabelProvider() {
			public Image getColumnImage(Object element, int columnIndex) {
				return null;
			}
			
			public String getColumnText(Object element, int columnIndex) {
				ExecutionResult result = (ExecutionResult)element;
				switch (columnIndex) {
					case 0: return result.getBenchmark(); 
					case 1: return String.format("%.2f", result.getPerformance());
					case 2: return String.format("%.2f", result.getCpuLoad());
					case 3: return String.format("%.2f", result.getMemoryUsage());
					default: return null;
				}
			}
			
			public boolean isLabelProperty(Object element, String prop) { 
				return false; 
			}
			public void addListener(ILabelProviderListener listener) {}
			public void removeListener(ILabelProviderListener listener) {}
			public void dispose() {}
		});
	
		_resultsTableViewer.setContentProvider(
			new IStructuredContentProvider() {
				
			public Object[] getElements(Object element) {
				return _results.toArray();
			}

			public void dispose() {}
			public void inputChanged(Viewer arg0, Object arg1, Object arg2) {}
		});		
	}
	
    public void setStartTime(String value) {
    	_startTimeText.setText(value);
    }
    
    public void setEndTime(String value) {
    	_endTimeText.setText(value);
    }
    
    public void setElapsedTime(String value) {
    	_elapsedTimeText.setText(value);
    }
    
    public void setMinPerformance(String value) {
    	_minPerformanceText.setText(value);
    }
    
    public void setAveragePerformance(String value) {
    	_averagePerformanceText.setText(value);
    }
    
    public void setMaxPerformance(String value) {
    	_maxPerformanceText.setText(value);
    }
    
    public void setMinCpuLoad(String value) {
    	_minCpuText.setText(value);
    }
    
    public void setAverageCpuLoad(String value) {
    	_averageCpuText.setText(value);
    }
    
    public void setMaxCpuLoad(String value) {
    	_maxCpuText.setText(value);
    }
    
    public void setMinMemoryUsage(String value) {
    	_minMemoryText.setText(value);
    }
    
    public void setAverageMemoryUsage(String value) {
    	_averageMemoryText.setText(value);
    }
    
    public void setMaxMemoryUsage(String value) {
    	_maxMemoryText.setText(value);
    }
    
    public void setExecutionResults(List<ExecutionResult> value) {
    	_results = value;
    	_resultsTableViewer.setInput(_results);
    }
    
    public int getNumberOfThreads() {
    	return _numberOfThreadsSpinner.getSelection();
    }
    
    public void setNumberOfThreads(int value) {
    	_numberOfThreadsSpinner.setSelection(value);
    }
    
    public MeasurementType getMeasurementType() {
    	return _peakPerformanceRadioButton.getSelection() 
    		? MeasurementType.Peak : MeasurementType.Nominal;
    }
    
    public void setMeasurementType(MeasurementType value) {
    	if (value == MeasurementType.Peak) {
    		_peakPerformanceRadioButton.setSelection(true);
    	} else {
    		_nominalPerformanceRadioButton.setSelection(true);
    	}
    }
    
    public double getNominalRate() {
    	return ((float)_nominalPerformanceSpinner.getSelection()) / 100.0;
    }
    
    public void setNominalRate(double value) {
    	_nominalPerformanceSpinner.setSelection((int)(value * 100));
    }
    
    public ExecutionType getExecutionType() {
    	return _sequentialExecutionCheckbox.getSelection() 
    		? ExecutionType.Sequential : ExecutionType.Proportional;
    }
    
    public void setExecutionType(ExecutionType value) {
    	_sequentialExecutionCheckbox.setSelection(
    		value == ExecutionType.Sequential);
    }
    
    public int getDuration() {
    	return _testDurationSpinner.getSelection();
    }
    
    public void setDuration(int value) {
    	_testDurationSpinner.setSelection(value);
    }

    public boolean getPerformanceChartEnabled() {
    	return _showChartCheckbox.getSelection();
    }
    
    public void setPerformanceChartEnabled(boolean value) {
    	_showChartCheckbox.setSelection(value);
    }
    
    public String getPerformanceChartName() {
    	return _showChartCheckbox.getText();
    }
    
    public void setPerformanceChartName(String value) {
    	_showChartCheckbox.setText(value);
    }
    
    public String getBenchmarkActionButton() {
    	return _actionButton.getText();
    }
    
    public void setBenchmarkActionButton(String value) {
    	_actionButton.setText(value);
    }
    
    public boolean getShowPerformanceChart() {
    	return !_resultsTable.getVisible();
    }
    
    public void setShowPerformanceChart(boolean value) {
    	_resultsTable.setVisible(!value);
    	_resultsGroup.setVisible(value);
    	_showChartCheckbox.setVisible(value);
    	_performanceChart.setVisible(value);
    }

    public void setListener(IExecutionViewListener listener) {
    	_listener = listener;
    }
    
    public void addCurrentPerformancePoint(double currentPerformance) {
        if (_showChartCheckbox.getSelection()) {
            _performanceChart.addValue(currentPerformance);
        }
    }
    
    public void clearPerformancePoints() {
        _performanceChart.clear();
    }
}
