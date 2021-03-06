package org.pipbenchmark.console;

import org.pipbenchmark.*;
import org.pipbenchmark.runner.*;
import org.pipbenchmark.runner.benchmarks.BenchmarkInstance;
import org.pipbenchmark.runner.benchmarks.BenchmarkSuiteInstance;
import org.pipbenchmark.runner.config.ExecutionType;

import java.io.*;

public class ConsoleRunner {
	
	public ConsoleRunner() { }
	
	public void run(CommandLineArgs processedArgs) {
        BenchmarkRunner runner = new BenchmarkRunner();
        
        ConsoleEventPrinter.attach(runner);

        executeBatchMode(processedArgs, runner);
	}
	
    private void executeBatchMode(CommandLineArgs args, BenchmarkRunner runner) {
        try {
            if (args.getShowHelp()) {
                HelpPrinter.print();
                return;
            }

            // Load jars
            for (String library : args.getLibraries()) 
                runner.getBenchmarks().addSuitesFromLibrary(library);

            // Load test suites classes
            for (String className : args.getClasses())
            	runner.getBenchmarks().addSuiteFromClass(className);
            
            // Load configuration
            if (args.getConfigurationFile() != null)
                runner.getParameters().loadFromFile(args.getConfigurationFile());

            // Set parameters
            if (args.getParameters().size() > 0)
            	runner.getParameters().set(args.getParameters());

            // Select benchmarks
            if (args.getBenchmarks().size() == 0)
            	runner.getBenchmarks().selectAll();
            else {
            	for (String benchmarkName : args.getBenchmarks())
            		runner.getBenchmarks().selectByName(new String[] { benchmarkName });
            }
            
            if (args.isShowParameters()) {
            	printParameters(runner);
            	return;
            }
            
            if (args.isShowBanchmarks()) {
            	printBenchmarks(runner);
            	return;
            }
                                    
            // Benchmark the environment
            if (args.getBenchmarkEnvironment()) {
                System.out.println("Benchmarking Environment (wait up to 2 mins)...");
                runner.getEnvironment().measure(true, true, true);
                System.out.printf("CPU: %.2f, Video: %.2f, Disk: %.2f\n",
                    runner.getEnvironment().getCpuMeasurement(), 
                    runner.getEnvironment().getVideoMeasurement(), 
                    runner.getEnvironment().getDiskMeasurement()
                );
            }

            // Configure benchmarking
            runner.getConfiguration().setMeasurementType(args.getMeasurementType());
            runner.getConfiguration().setNominalRate(args.getNominalRate());
            runner.getConfiguration().setExecutionType(args.getExecutionType());
            runner.getConfiguration().setDuration(args.getDuration());

            // Perform benchmarking
            runner.start();
            if (runner.getConfiguration().getExecutionType() == ExecutionType.Proportional) {
                Thread.sleep(args.getDuration() * 1000);
                runner.stop();
            }

            if (runner.getResults().getAll().size() > 0)
                System.out.printf("%f", runner.getResults().getAll().get(0).getPerformanceMeasurement().getAverageValue());

            // Generate report
            if (args.getReportFile() != null) {
            	FileOutputStream stream = new FileOutputStream(args.getReportFile());
            	try {
            		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(stream));
            		try {
            			writer.write(runner.getReport().generate());
            		} finally {
            			writer.close();
            		}
                } finally {
                	stream.close();
                }
            }
            
            System.out.println(runner.getReport().generate());
        }
        catch (Exception ex) {
        	System.out.printf("Error: %s\n", ex.getMessage());
        	ex.printStackTrace();
        }
    }

    private static void printBenchmarks(BenchmarkRunner runner) {
        System.out.println("Pip.Benchmark Console Runner. (c) Conceptual Vision Consulting LLC 2017");
        System.out.println();
        System.out.println("Benchmarks:");

        for (BenchmarkSuiteInstance suite : runner.getBenchmarks().getSuites()) {
            for (BenchmarkInstance benchmark : suite.getBenchmarks()) {
                System.out.printf("%s - %s\n", benchmark.getFullName(), benchmark.getDescription());
            }
        }
    }

    private static void printParameters(BenchmarkRunner runner) {
        System.out.println("Pip.Benchmark Console Runner. (c) Conceptual Vision Consulting LLC 2017");
        System.out.println();
        System.out.println("Parameters:");

        for (Parameter parameter : runner.getParameters().getUserDefined()) {
            String defaultValue = parameter.getDefaultValue();
            defaultValue = defaultValue == null || defaultValue.length() == 0 
            		? "" : " (Default: " + defaultValue + ")";
            System.out.printf("%s - %s%s\n", parameter.getName(), parameter.getDescription(), defaultValue);
        }
    }
    
	public void configurationUpdated() { }
		
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
    	
    	ConsoleRunner runner = new ConsoleRunner();
    	runner.run(processedArgs);
    }

    private static void processUnhandledException(Thread thread, Throwable ex) {
		System.out.printf("Caught unhandled exception from thread %s: %s\n",
			thread.getName(), ex.getMessage());
		ex.printStackTrace();
    }

}
