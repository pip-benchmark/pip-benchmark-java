package org.pipbenchmark.gui.environment;

import java.util.List;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.jface.viewers.*;

public class EnvironmentView extends Composite implements IEnvironmentView {
	private IEnvironmentViewListener _listener = null;
	private List<EnvironmentParameter> _environmentParameters;
	private Table _environmentParametersTable;
	private TableViewer _environmentParametersTableViewer;
	private Text _cpuPerformanceText;
	private Text _videoPerformanceText;
	private Text _diskPerformanceText;
	
	public EnvironmentView(Composite parent) {
		super(parent, SWT.NONE);
		initializeComponent();
	}

	private void initializeComponent() {
		this.setLayout(new FormLayout());

		Label parametersLabel = new Label(this, SWT.NONE);
		parametersLabel.setText("System Information:");
		FormData data = new FormData();
		data.top = new FormAttachment(0, 10);
		data.left = new FormAttachment(0, 10);
		parametersLabel.setLayoutData(data);		
		
		Button updateBenchmarkButton = new Button(this, SWT.PUSH);
		updateBenchmarkButton.setText("Update Benchmark");
		updateBenchmarkButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent args) {
				if (_listener != null) {
					_listener.updateSystemBenchmarkClicked();
				}
			}
		});
		data = new FormData();
		data.left = new FormAttachment(100, -130);
		data.right = new FormAttachment(100, -10);
		data.bottom = new FormAttachment(100, -10);
		updateBenchmarkButton.setLayoutData(data);		
	
		Group group = new Group(this, SWT.SHADOW_NONE);
		group.setText("System Benchmark Results");
		data = new FormData();
		data.top = new FormAttachment(updateBenchmarkButton, -137);
		data.left = new FormAttachment(0, 10);
		data.right = new FormAttachment(100, -10);
		data.bottom = new FormAttachment(updateBenchmarkButton, -5);
		group.setLayoutData(data);		
		group.setLayout(new FormLayout());
		
		Label cpuPerformanceLabel = new Label(group, SWT.NONE);
		cpuPerformanceLabel.setText("CPU Performance (MFLOP/s):");
		data = new FormData();
		data.top = new FormAttachment(0, 8);
		data.left = new FormAttachment(0, 10);
		cpuPerformanceLabel.setLayoutData(data);		
		
		Color textBackgroundColor = this.getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND);
		
		_cpuPerformanceText = new Text(group, SWT.BORDER);
		_cpuPerformanceText.setEditable(false);
		_cpuPerformanceText.setBackground(textBackgroundColor);
		data = new FormData();
		data.top = new FormAttachment(0, 5);
		data.left = new FormAttachment(0, 175);
		data.right = new FormAttachment(0, 275);
		_cpuPerformanceText.setLayoutData(data);

		Label videoPerformanceLabel = new Label(group, SWT.NONE);
		videoPerformanceLabel.setText("Video Performance (GOP/s):");
		data = new FormData();
		data.top = new FormAttachment(_cpuPerformanceText, 7);
		data.left = new FormAttachment(0, 10);
		videoPerformanceLabel.setLayoutData(data);		
		
		_videoPerformanceText = new Text(group, SWT.BORDER);
		_videoPerformanceText.setEditable(false);
		_videoPerformanceText.setBackground(textBackgroundColor);
		data = new FormData();
		data.top = new FormAttachment(_cpuPerformanceText, 5);
		data.left = new FormAttachment(0, 175);
		data.right = new FormAttachment(0, 275);
		_videoPerformanceText.setLayoutData(data);

		Label diskPerformanceLabel = new Label(group, SWT.NONE);
		diskPerformanceLabel.setText("Disk Performance (MB/s):");
		data = new FormData();
		data.top = new FormAttachment(_videoPerformanceText, 7);
		data.left = new FormAttachment(0, 10);
		diskPerformanceLabel.setLayoutData(data);		
		
		_diskPerformanceText = new Text(group, SWT.BORDER);
		_diskPerformanceText.setEditable(false);
		_diskPerformanceText.setBackground(textBackgroundColor);
		data = new FormData();
		data.top = new FormAttachment(_videoPerformanceText, 5);
		data.left = new FormAttachment(0, 175);
		data.right = new FormAttachment(0, 275);
		_diskPerformanceText.setLayoutData(data);
		
		_environmentParametersTableViewer = new TableViewer(this,
			SWT.BORDER | SWT.MULTI | SWT.FULL_SELECTION);
		_environmentParametersTable = _environmentParametersTableViewer.getTable();
		data = new FormData();
		data.top = new FormAttachment(parametersLabel, 5);
		data.left = new FormAttachment(0, 10);
		data.right = new FormAttachment(100, -10);
		data.bottom = new FormAttachment(group, -5);
		_environmentParametersTable.setLayoutData(data);
		
		initializeEnvironmentParametersTable();
	}
	
	private void initializeEnvironmentParametersTable() {
		_environmentParametersTable.setLinesVisible(true);
		_environmentParametersTable.setHeaderVisible(true);
		
		TableLayout tableLayout = new TableLayout();
		tableLayout.addColumnData(new ColumnWeightData(50, 75, true));
		tableLayout.addColumnData(new ColumnWeightData(50, 75, true));
		_environmentParametersTable.setLayout(tableLayout);

		TableColumn column1 = new TableColumn(_environmentParametersTable, SWT.LEFT);
		column1.setText("Parameter");
		TableColumn column2 = new TableColumn(_environmentParametersTable, SWT.LEFT);
		column2.setText("Value");
		
		_environmentParametersTableViewer.setLabelProvider(new ITableLabelProvider() {
			public Image getColumnImage(Object element, int columnIndex) {
				return null;
			}
			
			public String getColumnText(Object element, int columnIndex) {
				EnvironmentParameter parameter = (EnvironmentParameter)element;
				switch (columnIndex) {
					case 0: return parameter.getParameter(); 
					case 1: return parameter.getValue();
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
	
		_environmentParametersTableViewer.setContentProvider(
			new IStructuredContentProvider() {
				
			public Object[] getElements(Object element) {
				return _environmentParameters.toArray();
			}

			public void dispose() {}
			public void inputChanged(Viewer arg0, Object arg1, Object arg2) {}
		});		
	}
	
    public void setSystemInformation(List<EnvironmentParameter> value) {
    	_environmentParameters = value;
    	_environmentParametersTableViewer.setInput(_environmentParameters);
    }
    
    public void setCpuPerformance(String value) {
    	_cpuPerformanceText.setText(value);
    }
    
    public void setVideoPerformance(String value) {
    	_videoPerformanceText.setText(value);
    }
    
    public void setDiskPerformance(String value) {
    	_diskPerformanceText.setText(value);
    }
    
    public void setListener(IEnvironmentViewListener listener) {
    	_listener = listener;
    }
}
