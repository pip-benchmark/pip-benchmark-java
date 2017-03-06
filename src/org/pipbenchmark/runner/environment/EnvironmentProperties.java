package org.pipbenchmark.runner.environment;

import java.io.*;

import org.pipbenchmark.util.*;

public class EnvironmentProperties extends Properties {
	private static final long serialVersionUID = -349845640899893647L;

	public EnvironmentProperties() {
    }

    private String getFilePath() {
        return "BenchmarkEnvironment.properties";
    }
	
    public double getCpuBenchmark() {
        return super.getAsDouble("CpuBenchmark", 0);
    }

    public void setCpuBenchmark(double value) {
        super.setAsDouble("CpuBenchmark", value);
    }

    public double getDiskBenchmark() {
        return super.getAsDouble("DiskBenchmark", 0);
    }

    public void setDiskBenchmark(double value) {
        super.setAsDouble("DiskBenchmark", value);
    }

    public double getVideoBenchmark() {
        return super.getAsDouble("VideoBenchmark", 0);
    }

    public void setVideoBenchmark(double value) {
        super.setAsDouble("VideoBenchmark", value);
    }
	
    public void load() throws IOException {
        File file = new File(getFilePath());
        if (file.exists()) {
        	FileInputStream stream = new FileInputStream(file);
        	try {
               loadFromStream(stream);
            } finally {
            	stream.close();
            }
        }
    }

    public void save() throws IOException {
        File file = new File(getFilePath());
        FileOutputStream stream = new FileOutputStream(file);
        try {
            saveToStream(stream);
        } finally {
        	stream.close();
        }
    }

}
