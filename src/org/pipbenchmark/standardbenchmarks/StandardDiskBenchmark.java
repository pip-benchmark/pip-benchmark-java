package org.pipbenchmark.standardbenchmarks;

import java.io.*;
import java.util.*;

import org.pipbenchmark.*;

public class StandardDiskBenchmark extends Benchmark {
    private final static String NameText = "Disk";
    private final static String DescriptionText = "Measures disk read and write operations";
    private final static int BufferSize = 512;
    private final static int ChunkSize = 1024000;
    private final static int FileSize = 102400000;

    private Object _syncRoot = new Object();
    private Random _randomGenerator = new Random();
    private RandomAccessFile _fileStream;
    private byte[] _buffer = new byte[BufferSize];

    public StandardDiskBenchmark() {
        super(NameText, DescriptionText);
    }

    @Override
    public void setUp() {
    	try {
    		_fileStream = new RandomAccessFile(getFileName(), "rw");
    	} catch (IOException ex) {
    		throw new RuntimeException(ex);
    	}
    }

    private String getFileName() {
        String directoryPath = System.getProperty("user.dir");
        return directoryPath + String.format("/DiskBenchmark-%s.dat", UUID.randomUUID().toString());
    }

    @Override
    public void execute() {
        if (_fileStream == null) {
            return;
        }

        synchronized (_syncRoot) {
        	try {
	            if (_fileStream.length() == 0 || _randomGenerator.nextInt(2) == 0) {
	                int position;
	
	                if (_fileStream.length() < FileSize) {
	                    position = Math.min(FileSize - ChunkSize, (int)_fileStream.length());
	                } else {
	                    position = _randomGenerator.nextInt(FileSize - ChunkSize);
	                }
	
	                _fileStream.seek(position);
	                int sizeToWrite = ChunkSize;
	                while (sizeToWrite > 0) {
	                    _fileStream.write(_buffer, 0, Math.min(BufferSize, sizeToWrite));
	                    sizeToWrite -= BufferSize;
	                }
	//                _fileStream.flush();
	            } else {
	                int position = _randomGenerator.nextInt((int)_fileStream.length() - ChunkSize);
	                _fileStream.seek(position);
	
	                int sizeToRead = ChunkSize;
	                while (sizeToRead > 0) {
	                    _fileStream.read(_buffer, 0, Math.min(BufferSize, sizeToRead));
	                    sizeToRead -= BufferSize;
	                }
	            }
	        } catch (IOException ex) {
	        	throw new RuntimeException(ex);
	        }
        }
    }

    @Override
    public void tearDown() {
        synchronized (_syncRoot) {
        	try {
	            _fileStream.close();
	            _fileStream = null;
	
	            File fileInfo = new File(getFileName());
	            if (fileInfo.exists()) {
                    fileInfo.delete();
	            }
        	} catch (IOException ex) {
        		// Ignore...
        	}
        }
    }
}
