package org.pipbenchmark.runner.environment;

import java.io.*;
import java.util.*;

import org.pipbenchmark.runner.*;
import org.pipbenchmark.runner.benchmarks.*;
import org.pipbenchmark.runner.config.*;
import org.pipbenchmark.runner.execution.*;
import org.pipbenchmark.runner.params.*;

public class EnvironmentManager extends ExecutionManager {
    private final static int Duration = 5;

    private double _cpuBenchmark;
    private double _videoBenchmark;
    private double _diskBenchmark;

    public EnvironmentManager(BenchmarkRunner runner) {
        super(new ConfigurationManager(), runner);
        
        this._configuration.setDuration(Duration);
        
        try {
        	loadSystemBenchmarks();
        } catch (IOException ex) {
        	// Ignore. it shall never happen here...
        }
    }

    public Map<String, String> getSystemInformation() {
        return new SystemInformation();
    }

    public double getCpuBenchmark() {
        return _cpuBenchmark;
    }

    public double getVideoBenchmark() {
        return _videoBenchmark;
    }

    public double getDiskBenchmark() {
        return _diskBenchmark;
    }

    public void benchmarkEnvironment(boolean cpu, boolean disk, boolean video) {
        try {
        	if (cpu)
        		_cpuBenchmark = computeCpuBenchmark();
            
        	if (video)
        		_videoBenchmark = computeVideoBenchmark();
            
        	if (disk)
        		_diskBenchmark = computeDiskBenchmark();

            saveSystemBenchmarks();
        } catch (Throwable t) {
        	t.printStackTrace();
            stop();
        }
    }

    private void loadSystemBenchmarks() throws IOException {
        BenchmarkingProperties properties = new BenchmarkingProperties();
        properties.load();

        _cpuBenchmark = properties.getAsDouble("System.CpuBenchmark", 0);
        _videoBenchmark = properties.getAsDouble("System.VideoBenchmark", 0);
        _diskBenchmark = properties.getAsDouble("System.DiskBenchmark", 0);
    }

    private void saveSystemBenchmarks() throws IOException {
        BenchmarkingProperties properties = new BenchmarkingProperties();

        properties.setAsDouble("System.CpuBenchmark", _cpuBenchmark);
        properties.setAsDouble("System.VideoBenchmark", _videoBenchmark);
        properties.setAsDouble("System.DiskBenchmark", _diskBenchmark);

        properties.save();
    }

    private double computeCpuBenchmark() throws InterruptedException {
        StandardBenchmarkSuite suite = new StandardBenchmarkSuite();
        BenchmarkSuiteInstance instance = new BenchmarkSuiteInstance(suite);
        
        instance.unselectAllBenchmarks();
        instance.selectBenchmark(suite.getCpuBenchmark().getName());

        super.start(instance);
        Thread.sleep(Duration);
        super.stop();

        return super.getResults().get(0).getPerformanceMeasurement().getAverageValue();
    }

    private double computeVideoBenchmark() throws InterruptedException {
        StandardBenchmarkSuite suite = new StandardBenchmarkSuite();
        BenchmarkSuiteInstance instance = new BenchmarkSuiteInstance(suite);

        instance.unselectAllBenchmarks();
        instance.selectBenchmark(suite.getVideoBenchmark().getName());

        super.start(instance);
        Thread.sleep(Duration);
        super.stop();

    	return super.getResults().get(0).getPerformanceMeasurement().getAverageValue();
    }

    private double computeDiskBenchmark() throws InterruptedException {
        StandardBenchmarkSuite suite = new StandardBenchmarkSuite();
        BenchmarkSuiteInstance instance = new BenchmarkSuiteInstance(suite);

        instance.unselectAllBenchmarks();
        instance.selectBenchmark(suite.getDiskBenchmark().getName());

        super.start(instance);
        Thread.sleep(Duration);
        super.stop();

    	return super.getResults().get(0).getPerformanceMeasurement().getAverageValue();
    }
    
}
