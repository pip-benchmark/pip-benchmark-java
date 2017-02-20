package org.pipbenchmark.runner.console;

import java.text.*;
import java.util.*;

import org.pipbenchmark.runner.*;

public class ConsoleEventPrinter {
    private final static DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

    public static void add(BenchmarkRunner runner) {		
		runner.addErrorReportedListener(new IMessageListener() {
			public void onMessage(String message) {
				ConsoleEventPrinter.onErrorReported(message);
			}
		});
		
		runner.addMessageSentListener(new IMessageListener() {
			public void onMessage(String message) {
				ConsoleEventPrinter.onMessageSent(message);
			}
		});

		runner.addResultUpdatedListener(new IResultListener() {
			public void onResultUpdated(ExecutionState status, BenchmarkResult result) {
				ConsoleEventPrinter.onResultUpdated(status, result);
			}
		});
	}
	
	public static void onResultUpdated(ExecutionState status, BenchmarkResult result) {
        if (status == ExecutionState.Starting) {
            System.out.println("Benchmarking...");
        } else if (status == ExecutionState.Running) {
            if (result != null) {
                System.out.printf("%s Performance: %.2f %.2f>%.2f>%.2f CPU Load: %.2f %.2f>%.2f>%.2f Errors: %d\n",
                	timeFormat.format(new Date()),
                    result.getPerformanceMeasurement().getCurrentValue(),
                    result.getPerformanceMeasurement().getMinValue(),
                    result.getPerformanceMeasurement().getAverageValue(),
                    result.getPerformanceMeasurement().getMaxValue(),
                    result.getCpuLoadMeasurement().getCurrentValue(),
                    result.getCpuLoadMeasurement().getMinValue(),
                    result.getCpuLoadMeasurement().getAverageValue(),
                    result.getCpuLoadMeasurement().getMaxValue(),
                    result.getErrors().size()
                );
            }
        } else if (status == ExecutionState.Completed) {
            System.out.println("Completed Benchmark.");
        }
    }

	public static void onMessageSent(String message) {
		System.out.println(message);
	}

	public static void onErrorReported(String message) {
		System.out.println("Error: " + message);
	}
}
