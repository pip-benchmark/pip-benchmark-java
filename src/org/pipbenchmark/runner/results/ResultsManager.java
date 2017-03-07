package org.pipbenchmark.runner.results;

import java.util.*;

public class ResultsManager {
    private List<IResultListener> _updatedListeners = new ArrayList<IResultListener>();
    private List<IMessageListener> _messageListeners = new ArrayList<IMessageListener>();
    private List<IErrorListener> _errorListeners = new ArrayList<IErrorListener>();

    private List<BenchmarkResult> _results = new ArrayList<BenchmarkResult>();
    
    public ResultsManager() {    	
    }
    
    public List<BenchmarkResult> getAll() {
    	return _results;
    }
    
    public void add(BenchmarkResult result) {
    	_results.add(result);
    }
    
    public void clear() {
    	_results.clear();
    }

    public void addUpdatedListener(IResultListener listener) {
    	_updatedListeners.add(listener);
    }
    
    public void removeUpdatedListener(IResultListener listener) {
    	_updatedListeners.remove(listener);
    }

    public void notifyUpdated(BenchmarkResult result) {
    	for (int index = 0; index < _updatedListeners.size(); index++) {
    		try {
    			IResultListener listener = _updatedListeners.get(index);
    			listener.onResultUpdated(result);
    		} catch (Exception ex) {
    			// Ignore and send a message to the next listener.
    		}
    	}
    }

    public void addMessageListener(IMessageListener listener) {
    	_messageListeners.add(listener);
    }
    
    public void removeMessageListener(IMessageListener listener) {
    	_messageListeners.remove(listener);
    }

    public void notifyMessage(String message) {
    	for (int index = 0; index < _messageListeners.size(); index++) {
    		try {
    			IMessageListener listener = _messageListeners.get(index);
    			listener.onMessage(message);
    		} catch (Exception ex) {
    			// Ignore and send a message to the next listener.
    		}
    	}
    }

    public void addErrorListener(IErrorListener listener) {
    	_errorListeners.add(listener);
    }
    
    public void removeErrorListener(IErrorListener listener) {
    	_errorListeners.remove(listener);
    }

    public void notifyError(Object error) {
    	for (int index = 0; index < _errorListeners.size(); index++) {
    		try {
    			IErrorListener listener = _errorListeners.get(index);
    			listener.onError(error);
    		} catch (Exception ex) {
    			// Ignore and send a message to the next listener.
    		}
    	}
    }
}
