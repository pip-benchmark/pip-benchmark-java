package org.pipbenchmark.runner.config;

import java.io.*;

public class BenchmarkingProperties extends Properties {
	private static final long serialVersionUID = -349845640899893647L;

	public BenchmarkingProperties() {
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

    private String getFilePath() {
        return "BenchmarkSettings.properties";
    }
}
