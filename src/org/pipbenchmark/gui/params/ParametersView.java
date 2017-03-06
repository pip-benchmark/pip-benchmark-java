package org.pipbenchmark.gui.params;

import java.util.List;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.pipbenchmark.*;
import org.eclipse.jface.viewers.*;

public class ParametersView extends Composite implements IParametersView {
	private IParametersViewListener _listener = null;
	private List<Parameter> _parameters;
	private Table _parametersTable;
	private TableViewer _parametersTableViewer;
	
	public ParametersView(Composite parent) {
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
					_listener.saveToFileClicked();
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
					_listener.loadFromFileClicked();
				}
			}
		});
		data = new FormData();
		data.left = new FormAttachment(100, -250);
		data.right = new FormAttachment(100, -180);
		data.bottom = new FormAttachment(100, -10);
		loadButton.setLayoutData(data);		
	
		_parametersTableViewer = new TableViewer(this,
			SWT.BORDER | SWT.MULTI | SWT.FULL_SELECTION);
		_parametersTable = _parametersTableViewer.getTable();
		data = new FormData();
		data.top = new FormAttachment(parametersLabel, 5);
		data.left = new FormAttachment(0, 10);
		data.right = new FormAttachment(100, -10);
		data.bottom = new FormAttachment(setToDefaultButton, -5);
		_parametersTable.setLayoutData(data);		
		initializeParametersTable();
	}
	
	private void initializeParametersTable() {
		_parametersTable.setLinesVisible(true);
		_parametersTable.setHeaderVisible(true);
		
		TableLayout tableLayout = new TableLayout();
		tableLayout.addColumnData(new ColumnWeightData(33, 75, true));
		tableLayout.addColumnData(new ColumnWeightData(33, 75, true));
		tableLayout.addColumnData(new ColumnWeightData(33, 75, true));
		_parametersTable.setLayout(tableLayout);

		TableColumn column1 = new TableColumn(_parametersTable, SWT.LEFT);
		column1.setText("Name");
		TableColumn column2 = new TableColumn(_parametersTable, SWT.LEFT);
		column2.setText("Description");
		TableColumn column3 = new TableColumn(_parametersTable, SWT.LEFT);
		column3.setText("Value");
		
		_parametersTableViewer.setLabelProvider(new ITableLabelProvider() {
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
	
		_parametersTableViewer.setContentProvider(
			new IStructuredContentProvider() {
				
			public Object[] getElements(Object element) {
				return _parameters.toArray();
			}

			public void dispose() {}
			public void inputChanged(Viewer arg0, Object arg1, Object arg2) {}
		});
		
		_parametersTableViewer.setColumnProperties(new String[] {
			"Name", "Description", "Value"	
		});
		_parametersTableViewer.setCellEditors(new CellEditor[] {
			new TextCellEditor(_parametersTable),
			new TextCellEditor(_parametersTable),
			new TextCellEditor(_parametersTable)
		});
		_parametersTableViewer.setCellModifier(new ICellModifier() {
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
					_parametersTableViewer.refresh(parameter);
				}
			}			
		});
	}
	
    public void setData(List<Parameter> value) {
    	_parameters = value;
    	_parametersTableViewer.setInput(_parameters);
    }
    
    public void setListener(IParametersViewListener listener) {
    	_listener = listener;
    }
    
    public void refreshData() {
    	_parametersTableViewer.refresh();
    }
}
