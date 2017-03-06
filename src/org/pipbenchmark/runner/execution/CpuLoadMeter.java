package org.pipbenchmark.runner.execution;

import com.jezhumble.javasysmon.*;

public class CpuLoadMeter extends BenchmarkMeter {
    private JavaSysMon _javaSysMonitor;
    private CpuTimes _lastCupTimes;
    
    public CpuLoadMeter() {
    	super();
    	
    	try {
    		_javaSysMonitor = new JavaSysMon();
    	}
    	catch (Throwable t) {
    		// Ignore...
    	}
    	
        // First request to CPU load is very slow. So we do it during initialization
        performMeasurement();
    }

    public void setLastCpuTimes(CpuTimes cpuTimes) {
    	_lastCupTimes = cpuTimes;
    }
    
    public CpuTimes getLastCpuTimes() {
    	return _lastCupTimes;
    }
    
    @Override
    protected double performMeasurement() {
    	double result = 50;
    	
    	CpuTimes currentCpuTimes = getCpuTimes();
    	
    	if (getLastCpuTimes() != null && currentCpuTimes != null) {
    		result = currentCpuTimes.getCpuUsage(getLastCpuTimes()) * 100;
    	}
		setLastCpuTimes(currentCpuTimes);

		return result;
    }

    private CpuTimes getCpuTimes() {
    	if (_javaSysMonitor != null) {
    		return _javaSysMonitor.cpuTimes();
    	}
    	return null;
    }
    /*
    private OperatingSystemMXBean operatingSystem;

    public CpuLoadMeter() {
    	super();
    	
    	try {
    		operatingSystem = ManagementFactory.getOperatingSystemMXBean();
    	}
    	catch (Throwable t) {
    		// Ignore...
    	}
    	
        // First request to CPU load is very slow. So we do it during initialization
        performMeasurement();
    }

    @Override
    public double measure() {
        double cpuLoadMeasureInterval = 
        	(double)(System.currentTimeMillis() - getLastMeasuredTime()) / 1000;
        if (cpuLoadMeasureInterval > 0.5) {
            return super.measure();
        }
        return getCurrentValue();
    }

    @Override
    protected double performMeasurement() {
    	if (operatingSystem != null) {
    		return operatingSystem.getSystemLoadAverage();
    	}
        return 50;
    }
     */
}
