package org.pipbenchmark.runner.gui.config;

import java.util.List;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.pipbenchmark.*;
import org.eclipse.jface.viewers.*;

public class ConfigurationView extends Composite implements IConfigurationView {
	private IConfigurationViewListener _listener = null;
	private List<Parameter> _configurationParameters;
	private Table _configurationParametersTable;
	private TableViewer _configurationParametersTableViewer;
	
	public ConfigurationView(Composite parent) {
		super(parent, SWT.NONE);
		
		initializeComponent();
	}
	
	private void initializeComponent() {
		this.setLayout(new FormLayout());

		Label parametersLabel = new Label(this, SWT.NONE);
		parametersLabel.setText("Parameters:");
		FormData data = new FormData();
		data.top = new FormAttachment(0, 10);
		data.left = new FormAttachment(0, 10);
		parametersLabel.setLayoutData(data);		
		
		Button setToDefaultButton = new Button(this, SWT.PUSH);
		setToDefaultButton.setText("Set to Default");
		setToDefaultButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent args) {
				if (_listener != null) {
					_listener.setToDefaultClicked();
				}
			}
		});
		data = new FormData();
		data.left = new FormAttachment(100, -100);
		data.right = new FormAttachment(100, -10);
		data.bottom = new FormAttachment(100, -10);
		setToDefaultButton.setLayoutData(data);		

		Button saveButton = new Button(this, SWT.PUSH);
		saveButton.setText("Save...");
		saveButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent args) {
				if (_listener != null) {
					_listener.saveConfigurationClicked();
				}
			}
		});
		data = new FormData();
		data.left = new FormAttachment(100, -175);
		data.right = new FormAttachment(100, -105);
		data.bottom = new FormAttachment(100, -10);
		saveButton.setLayoutData(data);		
		
		Button loadButton = new Button(this, SWT.PUSH);
		loadButton.setText("Load...");
		loadButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent args) {
				if (_listener != null) {
					_listener.loadConfigurationClicked();
				}
			}
		});
		data = new FormData();
		data.left = new FormAttachment(100, -250);
		data.right = new FormAttachment(100, -180);
		data.bottom = new FormAttachment(100, -10);
		loadButton.setLayoutData(data);		
	
		_configurationParametersTableViewer = new TableViewer(this,
			SWT.BORDER | SWT.MULTI | SWT.FULL_SELECTION);
		_configurationParametersTable = _configurationParametersTableViewer.getTable();
		data = new FormData();
		data.top = new FormAttachment(parametersLabel, 5);
		data.left = new FormAttachment(0, 10);
		data.right = new FormAttachment(100, -10);
		data.bottom = new FormAttachment(setToDefaultButton, -5);
		_configurationParametersTable.setLayoutData(data);		
		initializeConfigurationParametersTable();
	}
	
	private void initializeConfigurationParametersTable() {
		_configurationParametersTable.setLinesVisible(true);
		_configurationParametersTable.setHeaderVisible(true);
		
		TableLayout tableLayout = new TableLayout();
		tableLayout.addColumnData(new ColumnWeightData(33, 75, true));
		tableLayout.addColumnData(new ColumnWeightData(33, 75, true));
		tableLayout.addColumnData(new ColumnWeightData(33, 75, true));
		_configurationParametersTable.setLayout(tableLayout);

		TableColumn column1 = new TableColumn(_configurationParametersTable, SWT.LEFT);
		column1.setText("Name");
		TableColumn column2 = new TableColumn(_configurationParametersTable, SWT.LEFT);
		column2.setText("Description");
		TableColumn column3 = new TableColumn(_configurationParametersTable, SWT.LEFT);
		column3.setText("Value");
		
		_configurationParametersTableViewer.setLabelProvider(new ITableLabelProvider() {
			public Image getColumnImage(Object element, int columnIndex) {
				return null;
			}
			
			public String getColumnText(Object element, int columnIndex) {
				Parameter parameter = (Parameter)element;
				switch (columnIndex) {
					case 0: return parameter.getName(); 
					case 1: return parameter.getDescription();
					case 2: return parameter.getValue();
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
	
		_configurationParametersTableViewer.setContentProvider(
			new IStructuredContentProvider() {
				
			public Object[] getElements(Object element) {
				return _configurationParameters.toArray();
			}

			public void dispose() {}
			public void inputChanged(Viewer arg0, Object arg1, Object arg2) {}
		});
		
		_configurationParametersTableViewer.setColumnProperties(new String[] {
			"Name", "Description", "Value"	
		});
		_configurationParametersTableViewer.setCellEditors(new CellEditor[] {
			new TextCellEditor(_configurationParametersTable),
			new TextCellEditor(_configurationParametersTable),
			new TextCellEditor(_configurationParametersTable)
		});
		_configurationParametersTableViewer.setCellModifier(new ICellModifier() {
			public boolean canModify(Object element, String property) {
				return property.equals("Value");
			}

			public Object getValue(Object element, String property) {
				Parameter parameter = (Parameter)element;
				if (property.equals("Name")) {
					return parameter.getName();
				}
				if (property.equals("Description")) {
					return parameter.getDescription();
				}
				if (property.equals("Value")) {
					return parameter.getValue();
				}
				return null;
			}

			public void modify(Object element, String property, Object value) {
				TableItem tableItem = (TableItem)element;
				Parameter parameter = (Parameter)tableItem.getData();
				if (property.equals("Value")) {
					parameter.setValue((String)value);
					_configurationParametersTableViewer.refresh(parameter);
				}
			}			
		});
	}
	
    public void setConfiguration(List<Parameter> value) {
    	_configurationParameters = value;
    	_configurationParametersTableViewer.setInput(_configurationParameters);
    }
    
    public void setListener(IConfigurationViewListener listener) {
    	_listener = listener;
    }
    
    public void refreshData() {
    	_configurationParametersTableViewer.refresh();
    }
}
