package org.pipbenchmark.util;

import java.util.*;

import java.io.*;

public class Properties extends LinkedHashMap<String, String> {
	private static final long serialVersionUID = -7370462619547811937L;
	
	private List<PropertyFileLine> _lines = new ArrayList<PropertyFileLine>();

    public Properties() {
    }

    public List<PropertyFileLine> getLines() {
        return _lines;
    }

    public void loadFromStream(InputStream stream) throws IOException {
        _lines.clear();

        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        String readLine = reader.readLine();
        while (readLine != null) {
            PropertyFileLine line = new PropertyFileLine(readLine);
            _lines.add(line);
            readLine = reader.readLine();
        }

        populateItems();
    }

    private void populateItems() {
        clear();

        for (PropertyFileLine line : _lines) {
            if (line.getKey() != null && line.getKey().length() > 0) {
                put(line.getKey(), line.getValue());
            }
        }
    }

    public void saveToStream(OutputStream stream) throws IOException {
        synchronizeItems();

        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(stream));
        try {
	        for (PropertyFileLine line : _lines) {
	            writer.write(line.getLine());
	            // Todo: replace with OS dependent line endings
	            writer.write("\r\n");
	        }
	        writer.flush();
        } finally {
        	writer.close();
        }
    }

    private PropertyFileLine findLine(String key) {
        for (PropertyFileLine line : _lines) {
            if (key.equals(line.getKey())) {
                return line;
            }
        }
        return null;
    }

    private void synchronizeItems() {
        // Update existing values and create missing lines
        for (Map.Entry<String, String> pair : this.entrySet()) {
            PropertyFileLine line = findLine(pair.getKey());
            if (line != null) {
                line.setValue(pair.getValue());
            } else {
                line = new PropertyFileLine(pair.getKey(), pair.getValue(), null);
                _lines.add(line);
            }
        }

        // Remove lines mismatched with listed keys
        for (int index = _lines.size() - 1; index >= 0; index--) {
            PropertyFileLine line = _lines.get(index);
            if (line.getKey() != null
            	&& this.containsKey(line.getKey()) == false) {
                _lines.remove(index);
            }
        }
    }

    public String getAsString(String key, String defaultValue) {
        if (this.containsKey(key)) {
            return this.get(key);
        }
        return defaultValue;
    }

    public void setAsString(String key, String value) {
        this.put(key, value);
    }

    public int getAsInteger(String key, int defaultValue) {
        if (this.containsKey(key)) {
            return Converter.stringToInteger(this.get(key), defaultValue);
        }
        return defaultValue;
    }

    public void setAsInteger(String key, int value) {
        this.put(key, Converter.integerToString(value));
    }

    public long getAsLong(String key, long defaultValue) {
        if (this.containsKey(key)) {
            return Converter.stringToLong(this.get(key), defaultValue);
        }
        return defaultValue;
    }

    public void setAsLong(String key, long value) {
        this.put(key, Converter.longToString(value));
    }

    public double getAsDouble(String key, double defaultValue) {
        if (this.containsKey(key)) {
            return Converter.stringToDouble(this.get(key), defaultValue);
        }
        return defaultValue;
    }

    public void setAsDouble(String key, double value) {
        this.put(key, Converter.doubleToString(value));
    }

    public boolean getAsBoolean(String key, boolean defaultValue) {
        if (this.containsKey(key)) {
            return Converter.stringToBoolean(this.get(key));
        }
        return defaultValue;
    }

    public void setAsBoolean(String key, boolean value) {
        this.put(key, Converter.booleanToString(value));
    }

}
