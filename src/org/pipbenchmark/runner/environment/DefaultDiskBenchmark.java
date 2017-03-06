package org.pipbenchmark.runner.environment;

import java.io.*;
import java.util.*;

import org.pipbenchmark.*;

public class DefaultDiskBenchmark extends Benchmark {
    private final static int BufferSize = 512;

    private Object _syncRoot = new Object();
    private Random _randomGenerator = new Random();
    private String _fileName;
    private RandomAccessFile _fileStream;
    private int _fileSize;
    private int _chunkSize;
    private boolean _testReads = true;
    private boolean _testWrites = true;
    private byte[] _buffer = new byte[BufferSize];

    public DefaultDiskBenchmark() {
        super("Disk", "Measures system disk performance");
    }

    @Override
    public final void setUp() {
        _fileSize = Math.max(getContext().getParameters().get("FileSize").getAsInteger(), 1024);
        _chunkSize = Math.max(getContext().getParameters().get("ChunkSize").getAsInteger(), 128);
        _chunkSize = Math.min(_chunkSize, _fileSize);

        _testReads = !getContext().getParameters().get("OperationTypes").getValue().equalsIgnoreCase("Write");
        _testWrites = !getContext().getParameters().get("OperationTypes").getValue().equalsIgnoreCase("Read");

        _fileName = getFileName();

    	try {
	        _fileStream = new RandomAccessFile(_fileName, "rw");
	        
	        // If we test only reads then file shall be prepared in advance
	        if (!_testWrites) {
	            _fileStream.seek(0);
	            int sizeToWrite = _fileSize;
	            while (sizeToWrite > 0) {
	                _fileStream.write(_buffer, 0, Math.min(BufferSize, sizeToWrite));
	                sizeToWrite -= BufferSize;
	            }
	//            _fileStream.flush();
	        }
    	} catch (IOException ex) {
    		throw new RuntimeException(ex);
    	}
    }

    private String getFileName() {
        String directoryPath = getContext().getParameters().get("FilePath").getValue();

        // Use default if directory path is not set
        if (directoryPath == null || directoryPath.length() == 0) {
            directoryPath = System.getProperty("user.dir");
        }

        return directoryPath + String.format("/DiskBenchmark-{0}.dat", UUID.randomUUID().toString());
    }

    @Override
    public final void execute() {
        if (_fileStream == null) {
            return;
        }

        synchronized (_syncRoot) {
        	try {
		        if (_fileStream.length() == 0 || (_testWrites
		        	&& (!_testReads || _randomGenerator.nextInt(2) == 0))) {
		            int position;
		
		            if (_fileStream.length() < _fileSize) {
		                position = Math.min(_fileSize - _chunkSize, (int)_fileStream.length());
		            } else {
		                position = _randomGenerator.nextInt(_fileSize - _chunkSize);
		            }
		
		            _fileStream.seek(position);
		            int sizeToWrite = _chunkSize;
		            while (sizeToWrite > 0) {
		                _fileStream.write(_buffer, 0, Math.min(BufferSize, sizeToWrite));
		                sizeToWrite -= BufferSize;
		            }
		//                _fileStream.flush();
		        } else {
		            int position = _randomGenerator.nextInt(Math.max(0, (int)_fileStream.length() - _chunkSize));
		            _fileStream.seek(position);
		
		            int sizeToRead = _chunkSize;
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
    public final void tearDown() {
        synchronized (_syncRoot) {
        	try {        	
	            _fileStream.close();
	            _fileStream = null;
	
	            File fileInfo = new File(_fileName);
	            if (fileInfo.exists()) {
	                fileInfo.delete();
	            }
            } catch (IOException ex) {
                // Ignore...
            }	            
        }
    }
}
