package org.pipbenchmark.console;

import java.text.*;
import java.util.*;

import org.pipbenchmark.runner.*;
import org.pipbenchmark.runner.execution.ExecutionState;
import org.pipbenchmark.runner.execution.IExecutionListener;
import org.pipbenchmark.runner.results.BenchmarkResult;
import org.pipbenchmark.runner.results.IErrorListener;
import org.pipbenchmark.runner.results.IMessageListener;
import org.pipbenchmark.runner.results.IResultListener;

public class ConsoleEventPrinter {
    private final static DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

    public static void attach(BenchmarkRunner runner) {		
		runner.getExecution().addUpdatedListener(new IExecutionListener() {
			public void onStateUpdated(ExecutionState state) {
				ConsoleEventPrinter.onStateUpdated(state);
			}
		});

		runner.getResults().addErrorListener(new IErrorListener() {
			public void onError(Object error) {
				ConsoleEventPrinter.onErrorReported(error);
			}
		});
		
		runner.getResults().addMessageListener(new IMessageListener() {
			public void onMessage(String message) {
				ConsoleEventPrinter.onMessageSent(message);
			}
		});

		runner.getResults().addUpdatedListener(new IResultListener() {
			public void onResultUpdated(BenchmarkResult result) {
				ConsoleEventPrinter.onResultUpdated(result);
			}
		});
	}

	public static void onStateUpdated(ExecutionState state) {
        if (state == ExecutionState.Running) {
            System.out.println("Benchmarking...");
        } else if (state == ExecutionState.Completed) {
            System.out.println("Completed benchmarking.");
        }
    }

	public static void onResultUpdated(BenchmarkResult result) {
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

	public static void onMessageSent(String message) {
		System.out.println(message);
	}

	public static void onErrorReported(Object error) {
		System.err.println("Error: " + error);
	}
}
