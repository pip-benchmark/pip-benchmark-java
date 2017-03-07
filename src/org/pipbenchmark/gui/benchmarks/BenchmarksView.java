package org.pipbenchmark.gui.benchmarks;

import java.util.List;
import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.pipbenchmark.runner.benchmarks.BenchmarkInstance;
import org.pipbenchmark.runner.benchmarks.BenchmarkSuiteInstance;
import org.pipbenchmark.util.Converter;
import org.eclipse.jface.viewers.*;

public class BenchmarksView extends Composite implements IBenchmarksView {
	private IBenchmarksViewListener _listener = null;
	private Table _suitesTable;
	private TableViewer _suitesTableViewer;
	private Table _benchmarksTable;
	private TableViewer _benchmarksTableViewer;
	private List<BenchmarkSuiteInstance> _allSuites;
	private List<BenchmarkInstance> _allBenchmarks;
	
	public BenchmarksView(Composite parent) {
		super(parent, SWT.NONE);
		
		initializeComponent();
	}
	
	private void initializeComponent() {
		this.setLayout(new FormLayout());
		
		SashForm content = new SashForm(this, SWT.VERTICAL);
		FormData data = new FormData();
		data.top = new FormAttachment(0, 10);
		data.left = new FormAttachment(0, 10);
		data.right = new FormAttachment(100, -10);
		data.bottom = new FormAttachment(100, -10);
		content.setLayoutData(data);
		
		Composite upContent = new Composite(content, SWT.UP);
		upContent.setLayout(new FormLayout());

		Label suitesLabel = new Label(upContent, SWT.NONE);
		suitesLabel.setText("Suites:");
		data = new FormData();
		data.top = new FormAttachment(0);
		data.left = new FormAttachment(0);
		suitesLabel.setLayoutData(data);		
		
		Button unloadAllButton = new Button(upContent, SWT.PUSH);
		unloadAllButton.setText("Unload All");
		unloadAllButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent args) {
				if (_listener != null) {
					_listener.unloadAllSuitesClicked();
				}
			}
		});
		data = new FormData();
		data.left = new FormAttachment(100, -70);
		data.right = new FormAttachment(100);
		data.bottom = new FormAttachment(100);
		unloadAllButton.setLayoutData(data);		

		Button unloadButton = new Button(upContent, SWT.PUSH);
		unloadButton.setText("Unload");
		unloadButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent args) {
				if (_listener != null) {
					_listener.unloadSuiteClicked();
				}
			}
		});
		data = new FormData();
		data.left = new FormAttachment(100, -145);
		data.right = new FormAttachment(100, -75);
		data.bottom = new FormAttachment(100);
		unloadButton.setLayoutData(data);		
		
		Button loadButton = new Button(upContent, SWT.PUSH);
		loadButton.setText("Load...");
		loadButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent args) {
				if (_listener != null) {
					_listener.loadSuiteClicked();
				}
			}
		});
		data = new FormData();
		data.left = new FormAttachment(100, -220);
		data.right = new FormAttachment(100, -150);
		data.bottom = new FormAttachment(100);
		loadButton.setLayoutData(data);		
	
		_suitesTableViewer = new TableViewer(upContent,
			SWT.BORDER | SWT.MULTI | SWT.FULL_SELECTION);
		_suitesTable = _suitesTableViewer.getTable();
		data = new FormData();
		data.top = new FormAttachment(suitesLabel, 5);
		data.left = new FormAttachment(0);
		data.right = new FormAttachment(100);
		data.bottom = new FormAttachment(unloadAllButton, -5);
		_suitesTable.setLayoutData(data);		
		initializeSuitesTable();
		
		Composite downContent = new Composite(content, SWT.DOWN);
		downContent.setLayout(new FormLayout());

		Label benchmarksLabel = new Label(downContent, SWT.NONE);
		benchmarksLabel.setText("Benchmarks:");
		data = new FormData();
		data.top = new FormAttachment(0);
		data.left = new FormAttachment(0);
		benchmarksLabel.setLayoutData(data);

		Button unselectAllButton = new Button(downContent, SWT.PUSH);
		unselectAllButton.setText("Unselect All");
		unselectAllButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent args) {
				if (_listener != null) {
					_listener.unselectAllBenchmarksClicked();
				}
			}
		});
		data = new FormData();
		data.left = new FormAttachment(100, -70);
		data.right = new FormAttachment(100);
		data.bottom = new FormAttachment(100);
		unselectAllButton.setLayoutData(data);		

		Button unselectButton = new Button(downContent, SWT.PUSH);
		unselectButton.setText("Unselect");
		unselectButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent args) {
				if (_listener != null) {
					_listener.unselectBenchmarkClicked();
				}
			}
		});
		data = new FormData();
		data.left = new FormAttachment(100, -145);
		data.right = new FormAttachment(100, -75);
		data.bottom = new FormAttachment(100);
		unselectButton.setLayoutData(data);		
		
		Button selectAllButton = new Button(downContent, SWT.PUSH);
		selectAllButton.setText("Select All");
		selectAllButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent args) {
				if (_listener != null) {
					_listener.selectAllBenchmarksClicked();
				}
			}
		});
		data = new FormData();
		data.left = new FormAttachment(100, -220);
		data.right = new FormAttachment(100, -150);
		data.bottom = new FormAttachment(100);
		selectAllButton.setLayoutData(data);		

		Button selectButton = new Button(downContent, SWT.PUSH);
		selectButton.setText("Select");
		selectButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent args) {
				if (_listener != null) {
					_listener.selectBenchmarkClicked();
				}
			}
		});
		data = new FormData();
		data.left = new FormAttachment(100, -295);
		data.right = new FormAttachment(100, -225);
		data.bottom = new FormAttachment(100);
		selectButton.setLayoutData(data);		
		
		_benchmarksTableViewer = new TableViewer(downContent,
			SWT.BORDER | SWT.MULTI | SWT.FULL_SELECTION);
		_benchmarksTable = _benchmarksTableViewer.getTable();
		data = new FormData();
		data.top = new FormAttachment(benchmarksLabel, 5);
		data.left = new FormAttachment(0);
		data.right = new FormAttachment(100);
		data.bottom = new FormAttachment(unselectAllButton, -5);
		_benchmarksTable.setLayoutData(data);
		initializeBenchmarksTable();
	}

	private void initializeSuitesTable() {
		_suitesTable.setLinesVisible(true);
		_suitesTable.setHeaderVisible(true);
		
		TableLayout tableLayout = new TableLayout();
		tableLayout.addColumnData(new ColumnPixelData(55, false));
		tableLayout.addColumnData(new ColumnWeightData(33, 75, true));
		tableLayout.addColumnData(new ColumnWeightData(33, 75, true));
		tableLayout.addColumnData(new ColumnWeightData(33, 75, true));
		_suitesTable.setLayout(tableLayout);

		TableColumn column1 = new TableColumn(_suitesTable, SWT.LEFT);
		column1.setText("Selected");
		column1.setResizable(false);
		TableColumn column2 = new TableColumn(_suitesTable, SWT.LEFT);
		column2.setText("Name");
		TableColumn column3 = new TableColumn(_suitesTable, SWT.LEFT);
		column3.setText("Description");
		TableColumn column4 = new TableColumn(_suitesTable, SWT.LEFT);
		column4.setText("Package");
		
		_suitesTableViewer.setLabelProvider(new ITableLabelProvider() {
			public Image getColumnImage(Object element, int columnIndex) {
				return null;
			}
			
			public String getColumnText(Object element, int columnIndex) {
				BenchmarkSuiteInstance suite = (BenchmarkSuiteInstance)element;
				switch (columnIndex) {
					case 0: return "False"; //suite.isSelected() ? "True" : "False";
					case 1: return suite.getName(); 
					case 2: return suite.getDescription();
					case 3: return suite.getClass().getPackage().getName();
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
	
		_suitesTableViewer.setContentProvider(new IStructuredContentProvider() {
			public Object[] getElements(Object element) {
				return _allSuites.toArray();
			}

			public void dispose() {}
			public void inputChanged(Viewer arg0, Object arg1, Object arg2) {}
		});
		
		_suitesTableViewer.setColumnProperties(new String[] {
			"Selected", "Name", "Description", "Package"	
		});
		_suitesTableViewer.setCellEditors(new CellEditor[] {
			new CheckboxCellEditor(_suitesTable),
			new TextCellEditor(_suitesTable),
			new TextCellEditor(_suitesTable),
			new TextCellEditor(_suitesTable)
		});
		_suitesTableViewer.setCellModifier(new ICellModifier() {
			public boolean canModify(Object element, String property) {
				return property.equals("Selected");
			}

			public Object getValue(Object element, String property) {
				BenchmarkSuiteInstance suite = (BenchmarkSuiteInstance)element;
				if (property.equals("Selected")) {
					return false; //suite.isSelected();
				}
				if (property.equals("Name")) {
					return suite.getName();
				}
				if (property.equals("Description")) {
					return suite.getDescription();
				}
				if (property.equals("Package")) {
					return suite.getClass().getPackage().getName();
				}
				return null;
			}

			public void modify(Object element, String property, Object value) {
				TableItem tableItem = (TableItem)element;
				BenchmarkSuiteInstance suite = (BenchmarkSuiteInstance)tableItem.getData();
				if (property.equals("Selected")) {
					//suite.setSelected((Boolean)value);
					_suitesTableViewer.refresh(suite);
					if (_listener != null) {
						_listener.suiteSelectedChanged();
					}
				}
			}			
		});
	}

	private void initializeBenchmarksTable() {
		_benchmarksTable.setLinesVisible(true);
		_benchmarksTable.setHeaderVisible(true);
		
		TableLayout tableLayout = new TableLayout();
		tableLayout.addColumnData(new ColumnPixelData(55, false));
		tableLayout.addColumnData(new ColumnWeightData(33, 75, true));
		tableLayout.addColumnData(new ColumnWeightData(33, 75, true));
		tableLayout.addColumnData(new ColumnWeightData(33, 75, true));
		_benchmarksTable.setLayout(tableLayout);

		TableColumn column1 = new TableColumn(_benchmarksTable, SWT.LEFT);
		column1.setText("Selected");
		column1.setResizable(false);
		TableColumn column2 = new TableColumn(_benchmarksTable, SWT.LEFT);
		column2.setText("Name");
		TableColumn column3 = new TableColumn(_benchmarksTable, SWT.LEFT);
		column3.setText("Description");
		TableColumn column4 = new TableColumn(_benchmarksTable, SWT.LEFT);
		column4.setText("Proportion %");

		_benchmarksTableViewer.setLabelProvider(new ITableLabelProvider() {
			public Image getColumnImage(Object element, int columnIndex) {
				return null;
			}
			
			public String getColumnText(Object element, int columnIndex) {
				BenchmarkInstance test = (BenchmarkInstance)element;
				switch (columnIndex) {
					case 0: return test.isSelected() ? "True" : "False";
					case 1: return test.getName(); 
					case 2: return test.getDescription();
					case 3: return Integer.toString(test.getProportion());
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
	
		_benchmarksTableViewer.setContentProvider(new IStructuredContentProvider() {
			public Object[] getElements(Object element) {
				return _allBenchmarks.toArray();
			}

			public void dispose() {}
			public void inputChanged(Viewer arg0, Object arg1, Object arg2) {}
		});
		
		_benchmarksTableViewer.setColumnProperties(new String[] {
			"Selected", "Name", "Description", "Proportion"	
		});
		_benchmarksTableViewer.setCellEditors(new CellEditor[] {
			new CheckboxCellEditor(_benchmarksTable),
			new TextCellEditor(_benchmarksTable),
			new TextCellEditor(_benchmarksTable),
			new TextCellEditor(_benchmarksTable)
		});
		_benchmarksTableViewer.setCellModifier(new ICellModifier() {
			public boolean canModify(Object element, String property) {
				return property.equals("Selected") || property.equals("Proportion");
			}

			public Object getValue(Object element, String property) {
				BenchmarkInstance benchmark = (BenchmarkInstance)element;
				if (property.equals("Selected")) {
					return benchmark.isSelected();
				}
				if (property.equals("Name")) {
					return benchmark.getName();
				}
				if (property.equals("Description")) {
					return benchmark.getDescription();
				}
				if (property.equals("Proportion")) {
					return Converter.integerToString(benchmark.getProportion());
				}
				return null;
			}

			public void modify(Object element, String property, Object value) {
				TableItem tableItem = (TableItem)element;
				BenchmarkInstance benchmark = (BenchmarkInstance)tableItem.getData();
				if (property.equals("Selected")) {
					benchmark.setSelected((Boolean)value);
				}
				else if (property.equals("Proportion")) {					
					benchmark.setProportion(Converter.stringToInteger((String)value, 0));
				}
				_benchmarksTableViewer.refresh(benchmark);
			}			
		});
	}
	
    public List<BenchmarkSuiteInstance> getAllSuites() {
    	return _allSuites;
    }
    
    public void setAllSuites(List<BenchmarkSuiteInstance> value) {  
    	_allSuites = value;
    	_suitesTableViewer.setInput(_allSuites);
    }
    
    public List<BenchmarkSuiteInstance> getSelectedSuites() {
    	List<BenchmarkSuiteInstance> result = new ArrayList<BenchmarkSuiteInstance>();
    	for (int index : _suitesTable.getSelectionIndices()) {
    		result.add(_allSuites.get(index));
    	}
    	return result;
    }
    
    public List<BenchmarkInstance> getAllBenchmarks() {
    	return _allBenchmarks;
    }
    
    public void setAllBenchmarks(List<BenchmarkInstance> value) {
    	_allBenchmarks = value;
    	_benchmarksTableViewer.setInput(_allBenchmarks);
    }
    
    public List<BenchmarkInstance> getSelectedBenchmarks() {
    	List<BenchmarkInstance> result = new ArrayList<BenchmarkInstance>();
    	for (int index : _benchmarksTable.getSelectionIndices()) {
    		result.add(_allBenchmarks.get(index));
    	}
    	return result;
    }

    public void setListener(IBenchmarksViewListener listener) {
    	_listener = listener;
    }
    
    public void refreshData() {    	
    	_suitesTableViewer.refresh();
    	_benchmarksTableViewer.refresh();
    }
}
