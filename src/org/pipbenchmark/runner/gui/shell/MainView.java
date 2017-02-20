package org.pipbenchmark.runner.gui.shell;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.pipbenchmark.runner.gui.config.*;
import org.pipbenchmark.runner.gui.environment.*;
import org.pipbenchmark.runner.gui.execution.*;
import org.pipbenchmark.runner.gui.initialization.*;
import org.pipbenchmark.runner.gui.result.*;

public class MainView implements IMainView {
	private IMainViewListener _listener = null;
	private Shell _shell;
	private TabFolder _content;
	private Label _statusLabel;
	private InitializationView _initializationView;
	private ConfigurationView _configurationView;
	private ExecutionView _executionView;
	private ResultsView _resultsView;
	private EnvironmentView _environmentView;
	private MenuItem[] _viewMenuItems = new MenuItem[5];
	private TabItem[] _viewTabItems = new TabItem[5];
	
	public MainView(Display display) {
		initializeComponent(display);
	}
	
	private void initializeComponent(Display display) {
		_shell = new Shell(display);
		_shell.setSize(640, 520);
		_shell.setText("Pip.Benchmark");
		_shell.addShellListener(new ShellAdapter() {
			public void shellClosed(ShellEvent args) {
				if (_listener != null) {
					_listener.formExited();
				}
			}
		});
		
		initializeMenuBar();
		initializeContent();
		
		switchView(_viewMenuItems[0]);
	}

	private void initializeMenuBar() {
		Menu menuBar = new Menu(_shell, SWT.BAR);
		_shell.setMenuBar(menuBar);
		
		MenuItem fileMenuItem = new MenuItem(menuBar, SWT.CASCADE);
		fileMenuItem.setText("&File");
		Menu fileMenu = new Menu(_shell, SWT.DROP_DOWN);
		fileMenuItem.setMenu(fileMenu);
		
		MenuItem loadTestSuiteMenuItem = new MenuItem(fileMenu, SWT.PUSH);
		loadTestSuiteMenuItem.setText("&Load Suite...");
		loadTestSuiteMenuItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent args) {
				if (_listener != null) {
					_listener.loadSuiteClicked();
				}
			}
		});
		
		new MenuItem(fileMenu, SWT.SEPARATOR);
		
		MenuItem loadConfigurationMenuItem = new MenuItem(fileMenu, SWT.PUSH);
		loadConfigurationMenuItem.setText("&Load Configuration...");
		loadConfigurationMenuItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent args) {
				if (_listener != null) {
					_listener.loadConfigurationClicked();
				}
			}
		});

		MenuItem saveConfigurationMenuItem = new MenuItem(fileMenu, SWT.PUSH);
		saveConfigurationMenuItem.setText("&Save Configuration...");
		saveConfigurationMenuItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent args) {
				if (_listener != null) {
					_listener.saveConfigurationClicked();
				}
			}
		});
		
		new MenuItem(fileMenu, SWT.SEPARATOR);
		
		MenuItem saveReportMenuItem = new MenuItem(fileMenu, SWT.PUSH);
		saveReportMenuItem.setText("&Save Report...");
		saveReportMenuItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent args) {
				if (_listener != null) {
					_listener.saveReportClicked();
				}
			}
		});

		MenuItem printReportMenuItem = new MenuItem(fileMenu, SWT.PUSH);
		printReportMenuItem.setText("&Print Report...");
		printReportMenuItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent args) {
				if (_listener != null) {
					_listener.printReportClicked();
				}
			}
		});
		
		new MenuItem(fileMenu, SWT.SEPARATOR);

		MenuItem exitMenuItem = new MenuItem(fileMenu, SWT.PUSH);
		exitMenuItem.setText("E&xit");
		exitMenuItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent args) {
				if (_listener != null) {
					_listener.exitClicked();
				}
			}
		});
				
		SelectionAdapter viewMenuItemSelectioAdapter = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent args) {
				switchView((MenuItem)args.widget);
			}
		};
		
		MenuItem viewMenuItem = new MenuItem(menuBar, SWT.CASCADE);
		viewMenuItem.setText("&View");
		Menu viewMenu = new Menu(_shell, SWT.DROP_DOWN);
		viewMenuItem.setMenu(viewMenu);
		MenuItem initializationMenuItem = new MenuItem(viewMenu, SWT.RADIO);
		initializationMenuItem.setText("&Initialization");
		initializationMenuItem.addSelectionListener(viewMenuItemSelectioAdapter);
		MenuItem configurationMenuItem = new MenuItem(viewMenu, SWT.RADIO);
		configurationMenuItem.setText("&Configuration");
		configurationMenuItem.addSelectionListener(viewMenuItemSelectioAdapter);
		MenuItem executionMenuItem = new MenuItem(viewMenu, SWT.RADIO);
		executionMenuItem.setText("&Execution");
		executionMenuItem.addSelectionListener(viewMenuItemSelectioAdapter);
		MenuItem resultsMenuItem = new MenuItem(viewMenu, SWT.RADIO);
		resultsMenuItem.setText("&Results");
		resultsMenuItem.addSelectionListener(viewMenuItemSelectioAdapter);
		MenuItem environmentMenuItem = new MenuItem(viewMenu, SWT.RADIO);
		environmentMenuItem.setText("E&nvironment");
		environmentMenuItem.addSelectionListener(viewMenuItemSelectioAdapter);

		MenuItem benchmarkMenuItem = new MenuItem(menuBar, SWT.CASCADE);
		benchmarkMenuItem.setText("&Benchmark");
		Menu benchmarkMenu = new Menu(_shell, SWT.DROP_DOWN);
		benchmarkMenuItem.setMenu(benchmarkMenu);

		MenuItem startBenchmarkingMenuItem = new MenuItem(benchmarkMenu, SWT.PUSH);
		startBenchmarkingMenuItem.setText("&Start Benchmarking");
		startBenchmarkingMenuItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent args) {
				if (_listener != null) {
					_listener.startBenchmarkingClicked();
				}
			}
		});
	
		MenuItem stopBenchmarkingMenuItem = new MenuItem(benchmarkMenu, SWT.PUSH);
		stopBenchmarkingMenuItem.setText("S&top Benchmarking");
		stopBenchmarkingMenuItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent args) {
				if (_listener != null) {
					_listener.stopBenchmarkingClicked();
				}
			}
		});

		new MenuItem(benchmarkMenu, SWT.SEPARATOR);

		MenuItem benchmarkEnvironmentMenuItem = new MenuItem(benchmarkMenu, SWT.PUSH);
		benchmarkEnvironmentMenuItem.setText("Benchmark &Environment");
		benchmarkEnvironmentMenuItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent args) {
				if (_listener != null) {
					_listener.benchmarkEnvironmentClicked();
				}
			}
		});

		MenuItem helpMenuItem = new MenuItem(menuBar, SWT.CASCADE);
		helpMenuItem.setText("&Help");
		Menu helpMenu = new Menu(_shell, SWT.DROP_DOWN);
		helpMenuItem.setMenu(helpMenu);

		MenuItem aboutMenuItem = new MenuItem(helpMenu, SWT.PUSH);
		aboutMenuItem.setText("&About...");
		aboutMenuItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent args) {
				if (_listener != null) {
					_listener.aboutClicked();
				}
			}
		});
		
		_viewMenuItems[0] = initializationMenuItem;
		_viewMenuItems[1] = configurationMenuItem;
		_viewMenuItems[2] = executionMenuItem;
		_viewMenuItems[3] = resultsMenuItem;
		_viewMenuItems[4] = environmentMenuItem;
	}
	
	private void initializeContent() {
		GridLayout layout = new GridLayout();
		_shell.setLayout(layout);
		
		_content = new TabFolder(_shell, SWT.NONE);
		GridData data = new GridData(GridData.FILL_BOTH);
		_content.setLayoutData(data);

		SelectionAdapter tabItemSelectionAdapter = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent args) {
				switchView((TabItem)args.item);
			}
		};
		_content.addSelectionListener(tabItemSelectionAdapter);
		
		TabItem initializationTab = new TabItem(_content, SWT.NONE);
		initializationTab.setText("&Initialization  ");
		_initializationView = new InitializationView(_content);
		initializationTab.setControl(_initializationView);
		
		TabItem configurationTab = new TabItem(_content, SWT.NONE);
		configurationTab.setText("&Configuration  ");
		_configurationView = new ConfigurationView(_content);
		configurationTab.setControl(_configurationView);
		
		TabItem executionTab = new TabItem(_content, SWT.NONE);
		executionTab.setText("&Execution  ");
		_executionView = new ExecutionView(_content);
		executionTab.setControl(_executionView);

		TabItem resultsTab = new TabItem(_content, SWT.NONE);
		resultsTab.setText("&Results  ");
		_resultsView = new ResultsView(_content);
		resultsTab.setControl(_resultsView);
		
		TabItem environmentTab = new TabItem(_content, SWT.NONE);
		environmentTab.setText("E&nvironment  ");
		_environmentView = new EnvironmentView(_content);
		environmentTab.setControl(_environmentView);
		
		_statusLabel = new Label(_shell, SWT.BORDER);
		data = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		_statusLabel.setLayoutData(data);
		_statusLabel.setText("...");
		
		_viewTabItems[0] = initializationTab;
		_viewTabItems[1] = configurationTab;
		_viewTabItems[2] = executionTab;
		_viewTabItems[3] = resultsTab;
		_viewTabItems[4] = environmentTab;
	}
	
    private void switchView(MenuItem menuItem) {
        for (int index = 0; index < _viewMenuItems.length; index++)
        {
            if (_viewMenuItems[index] == menuItem) {
                _viewMenuItems[index].setSelection(true);
                _content.setSelection(_viewTabItems[index]);
            } else {
                _viewMenuItems[index].setSelection(false);
            }
        }
    }
	
    private void switchView(TabItem tabItem) {
        for (int index = 0; index < _viewTabItems.length; index++) {
            if (_viewTabItems[index] == tabItem) {
                _viewMenuItems[index].setSelection(true);
            } else {
                _viewMenuItems[index].setSelection(false);
            }
        }
    }
    
	/**************** IMainView Interface Implementation *****************/
	
    public Shell getHandler() {
    	return _shell;
    }
	
    public void setFormTitle(String title) {
    	_shell.setText(title);
    }
    
    public void setStatusMessage(String message) {
    	_statusLabel.setText(message);
    }
    
    public void setSelectedView(String viewName) {
    	if (viewName.equalsIgnoreCase("Initialization")) {
    		switchView(_viewMenuItems[0]);
    	} else if (viewName.equalsIgnoreCase("Configuration")) {
    		switchView(_viewMenuItems[1]);
    	} else if (viewName.equalsIgnoreCase("Execution")) {
    		switchView(_viewMenuItems[2]);
    	} else if (viewName.equalsIgnoreCase("Results")) {
    		switchView(_viewMenuItems[3]);
    	} else if (viewName.equalsIgnoreCase("Environment")) {
    		switchView(_viewMenuItems[4]);
    	} else {
    		switchView(_viewMenuItems[0]);
    	}
    }
    
    public IInitializationView getInitializationView() {
    	return _initializationView;
    }
    
    public IConfigurationView getConfigurationView() {
    	return _configurationView;
    }
    
    public IExecutionView getExecutionView() {
    	return _executionView;
    }
    
    public IResultsView getResultsView() {
    	return _resultsView;
    }
    
    public IEnvironmentView getEnvironmentView() {
    	return _environmentView;
    }
    
    public void setListener(IMainViewListener listener) {
    	_listener = listener;
    }
}
