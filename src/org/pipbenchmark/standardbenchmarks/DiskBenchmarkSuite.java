package org.pipbenchmark.standardbenchmarks;

import java.io.*;
import java.util.*;

import org.pipbenchmark.*;

public class DiskBenchmarkSuite extends BenchmarkSuite {
    private final static int BufferSize = 512;

    private Parameter _filePath;
    private Parameter _fileSize;
    private Parameter _chunkSize;

    private Object _syncRoot = new Object();
    private Random _randomGenerator = new Random();
    private RandomAccessFile _fileStream;
    private byte[] _buffer = new byte[BufferSize];

    public DiskBenchmarkSuite() {
        super("Disk", "Benchmark for disk I/O operations");

        initializeConfigurationParameters();
        initializeTests();
    }

    private void initializeConfigurationParameters() {
        _filePath = addParameter("FilePath", "Path where test file is located on disk", "");
        _fileSize = addParameter("FileSize", "Size of the test file", "102400000");
        _chunkSize = addParameter("ChunkSize", "Size of a chunk that read or writter from/to test file", "1024000");
    }

    private void initializeTests() {
        addBenchmark("ReadsAndWrites", "Measures read and write operations",
        	new Runnable() {
        		public void run() {
        			executeReadsAndWrites();
        		}
        	});
        addBenchmark("Reads", "Measures only read operations",
	    	new Runnable() {
	    		public void run() {
	    			executeReads();
	    		}
	    	});
        addBenchmark("Writes", "Measures only write operations",
        	new Runnable() {
        		public void run() {
        			executeWrites();
        		}
        	});
    }

    public int getFileSize() {
        return Math.max(_fileSize.getAsInteger(), 1024); 
    }

    public int getChunkSize() {
        return Math.min(getFileSize(), Math.max(_chunkSize.getAsInteger(), 128));
    }

    public String getFileName() {
		String directoryPath = _filePath.getValue();
		
		// Use default if directory path is not set
        if (directoryPath == null || directoryPath.length() == 0) {
            directoryPath = System.getProperty("user.dir");
        }

        return directoryPath + String.format("\\DiskBenchmark-%s.dat", UUID.randomUUID().toString());
    }

    @Override
    public void setUp() {
    	try {
	        _fileStream = new RandomAccessFile(getFileName(), "rw");
	
	        // If we test only reads then file shall be prepared in advance
            _fileStream.seek(0);
            int sizeToWrite = getFileSize();
            while (sizeToWrite > 0) {
                _fileStream.write(_buffer, 0, Math.min(BufferSize, sizeToWrite));
                sizeToWrite -= BufferSize;
            }
//            _fileStream.flush();
    	} catch (IOException ex) {
    		throw new RuntimeException(ex);
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

    public void executeReadsAndWrites() {
    	try {
	        if (_fileStream.length() == 0 || _randomGenerator.nextInt(2) == 0) {
	            executeWrites();
	        } else {
	            executeReads();
	        }
    	} catch (IOException ex) {
    		throw new RuntimeException(ex);
    	}
    }

    public void executeWrites() {
        synchronized (_syncRoot) {
        	try {
	            int position;
	
	            if (_fileStream.length() < getFileSize()) {
	                position = Math.min(getFileSize() - getChunkSize(), (int)_fileStream.length());
	            } else {
	                position = _randomGenerator.nextInt(getFileSize() - getChunkSize());
	            }
	
	            _fileStream.seek(position);
	            int sizeToWrite = getChunkSize();
	            while (sizeToWrite > 0) {
	                _fileStream.write(_buffer, 0, Math.min(BufferSize, sizeToWrite));
	                sizeToWrite -= BufferSize;
	            }
	//            _fileStream.flush();
        	} catch (IOException ex) {
        		throw new RuntimeException(ex);
        	}
        }
    }

    public void executeReads() {
        synchronized (_syncRoot) {
        	try {
	            if (_fileStream.length() > 0) {
	                int position = _randomGenerator.nextInt((int)_fileStream.length() - getChunkSize());
	                _fileStream.seek(position);
	
	                int sizeToRead = getChunkSize();
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
}
