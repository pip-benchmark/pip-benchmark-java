package org.pipbenchmark.standardbenchmarks;

import java.util.*;

import org.pipbenchmark.*;

public class UtilityBenchmarkSuite extends BenchmarkSuite {
    private Random _randomGenerator = new Random();

    public UtilityBenchmarkSuite() {
        super("Utility", "Set of utility benchmark tests");

        createBenchmark("Empty", "Does nothing",
        	new Runnable() { public final void run() { executeEmpty(); } });
        createBenchmark("RandomDelay", "Introduces random delay to measuring thread",
        	new Runnable() { public final void run() { executeRandomDelay(); } });
    }

    @Override
    public final void setUp() {
    }

    @Override
    public final void tearDown() {
    }

    private void executeEmpty() {
    }

    private void executeRandomDelay() {
    	try {
    		Thread.sleep(_randomGenerator.nextInt(1000));
    	} catch (InterruptedException ex) {
    		// Ignore...
    	}
    }
}
