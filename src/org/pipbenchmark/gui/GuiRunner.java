package org.pipbenchmark.gui;

import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.pipbenchmark.console.*;
import org.pipbenchmark.gui.shell.*;
import org.pipbenchmark.runner.*;

public class GuiRunner {

    private static void configureEngine(CommandLineArgs args, BenchmarkRunner runner) {
        try {
            // Load assemblies
            for (String libraryFile : args.getLibraries()) {
                runner.getBenchmarks().addSuitesFromLibrary(libraryFile);
            }

            // Load test suites classes
            for (String className : args.getClasses()) {
            	runner.getBenchmarks().addSuiteFromClass(className);
            }
            
            // Load configuration
            if (args.getConfigurationFile() != null) {
                runner.getParameters().loadFromFile(args.getConfigurationFile());
            }

            // Configure benchmarking
            runner.getConfiguration().setMeasurementType(args.getMeasurementType());
            runner.getConfiguration().setNominalRate(args.getNominalRate());
            runner.getConfiguration().setExecutionType(args.getExecutionType());
            runner.getConfiguration().setDuration((int)args.getDuration());

            // Benchmark the environment
            if (args.getBenchmarkEnvironment()) {
                System.out.println("Cannot benchmark environment in interactive mode");
            }
        }
        catch (Exception ex) {
        	System.out.printf("Error: %s\n", ex.getMessage());
        	ex.printStackTrace();
        }
    }
	
	/// <summary>
    /// The main entry point for the application.
    /// </summary>
    public static void main(String[] args) {    	
    	Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
			public void uncaughtException(Thread thread, Throwable ex) {
				processUnhandledException(thread, ex);
			}
		});
    	
    	CommandLineArgs processedArgs = new CommandLineArgs(args);
    	if (processedArgs.isBatchMode() || processedArgs.getShowHelp()) {
	    	ConsoleRunner utility = new ConsoleRunner();
	    	utility.run(processedArgs);
    	} else {
            Display display = new Display( );
            
            MainView view = new MainView(display);
            BenchmarkRunner model = new BenchmarkRunner();
            configureEngine(processedArgs, model);
            new MainController(view, model);
            
            Shell shell = view.getHandler();
            shell.open();
            while(!shell.isDisposed()) {
            	if(!display.readAndDispatch()) {
            		display.sleep();
            	}
            }
            display.dispose();
    	}
        System.exit(0);
    }
    
    private static void processUnhandledException(Thread thread, Throwable ex) {
		System.out.printf("Caught unhandled exception from thread %s: %s\n",
				thread.getName(), ex.getMessage());
			ex.printStackTrace();

		Display display = Display.getCurrent();
    	if (display != null) {
	    	ErrorDialog.openError(display.getActiveShell(), "Unhandled Exception",
	    		String.format("Caught unhandled exception from thread %s", thread.getName()),
	    		new Status(SWT.ERROR, "none", 0, ex.getMessage(), ex));
    	}
    }
}
