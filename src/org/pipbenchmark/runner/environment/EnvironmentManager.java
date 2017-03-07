package org.pipbenchmark.runner.environment;

import java.io.*;
import java.util.*;

import org.pipbenchmark.runner.benchmarks.*;
import org.pipbenchmark.runner.config.*;
import org.pipbenchmark.runner.execution.*;
import org.pipbenchmark.runner.results.*;

public class EnvironmentManager extends ExecutionManager {
    private final static int Duration = 5;

    private double _cpuMeasurement;
    private double _videoMeasurement;
    private double _diskMeasurement;

    public EnvironmentManager() {
        super(new ConfigurationManager(), new ResultsManager());
        
        this._configuration.setDuration(Duration);
        
        try {
        	load();
        } catch (IOException ex) {
        	// Ignore. it shall never happen here...
        }
    }

    public Map<String, String> getSystemInfo() {
        return new SystemInfo();
    }

    public double getCpuMeasurement() {
        return _cpuMeasurement;
    }

    public double getVideoMeasurement() {
        return _videoMeasurement;
    }

    public double getDiskMeasurement() {
        return _diskMeasurement;
    }

    public void measure(boolean cpu, boolean disk, boolean video) {
        try {
        	if (cpu)
        		_cpuMeasurement = measureCpu();
            
        	if (video)
        		_videoMeasurement = measureVideo();
            
        	if (disk)
        		_diskMeasurement = measureDisk();

            save();
        } catch (Throwable t) {
        	t.printStackTrace();
            stop();
        }
    }

    private void load() throws IOException {
        EnvironmentProperties properties = new EnvironmentProperties();
        properties.load();

        _cpuMeasurement = properties.getAsDouble("CpuMeasurement", 0);
        _videoMeasurement = properties.getAsDouble("VideoMeasurement", 0);
        _diskMeasurement = properties.getAsDouble("DiskMeasurement", 0);
    }

    private void save() throws IOException {
        EnvironmentProperties properties = new EnvironmentProperties();

        properties.setAsDouble("CpuMeasurement", _cpuMeasurement);
        properties.setAsDouble("VideoMeasurement", _videoMeasurement);
        properties.setAsDouble("DiskMeasurement", _diskMeasurement);

        properties.save();
    }

    private double measureCpu() throws InterruptedException {
        StandardBenchmarkSuite suite = new StandardBenchmarkSuite();
        BenchmarkSuiteInstance instance = new BenchmarkSuiteInstance(suite);
        
        instance.unselectAll();
        instance.selectByName(suite.getCpuBenchmark().getName());

        super.start(instance.getSelected());
        Thread.sleep(Duration);
        super.stop();

        return _results.getAll().get(0).getPerformanceMeasurement().getAverageValue();
    }

    private double measureVideo() throws InterruptedException {
        StandardBenchmarkSuite suite = new StandardBenchmarkSuite();
        BenchmarkSuiteInstance instance = new BenchmarkSuiteInstance(suite);

        instance.unselectAll();
        instance.selectByName(suite.getVideoBenchmark().getName());

        super.run(instance.getSelected());
        Thread.sleep(Duration);
        super.stop();

    	return _results.getAll().get(0).getPerformanceMeasurement().getAverageValue();
    }

    private double measureDisk() throws InterruptedException {
        StandardBenchmarkSuite suite = new StandardBenchmarkSuite();
        BenchmarkSuiteInstance instance = new BenchmarkSuiteInstance(suite);

        instance.unselectAll();
        instance.selectByName(suite.getDiskBenchmark().getName());

        super.start(instance.getSelected());
        Thread.sleep(Duration);
        super.stop();

    	return _results.getAll().get(0).getPerformanceMeasurement().getAverageValue();
    }
    
}
