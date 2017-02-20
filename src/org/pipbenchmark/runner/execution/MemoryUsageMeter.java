package org.pipbenchmark.runner.execution;

import java.lang.management.*;

public class MemoryUsageMeter extends BenchmarkMeter {
	private MemoryMXBean memoryUsage; 
	
    public MemoryUsageMeter() {
    	super();
    	
    	try {
    		memoryUsage = ManagementFactory.getMemoryMXBean();
    	} catch (Throwable t) {
    		// Ignore...
    	}
    }

    @Override
    public double measure() {
        double memoryMeasureInterval 
        	= (double)(System.currentTimeMillis() - getLastMeasuredTime()) / 1000;
        if (memoryMeasureInterval > 0.5) {
            return super.measure();
        }
        return getCurrentValue();
    }

    @Override
    protected double performMeasurement() {
        return getUsedMemory();
    }

    private double getUsedMemory() {
    	if (memoryUsage != null) {
    		return (memoryUsage.getHeapMemoryUsage().getUsed() 
    				+ memoryUsage.getNonHeapMemoryUsage().getUsed()) / 1024 / 1024;
    	}
        return 0;
    }

}
