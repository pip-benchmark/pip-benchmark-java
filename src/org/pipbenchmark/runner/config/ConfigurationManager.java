package org.pipbenchmark.runner.config;

import java.util.ArrayList;
import java.util.List;

public class ConfigurationManager {
    private int _numberOfThreads = 1;
    private MeasurementType _measurementType = MeasurementType.Peak;
    private double _nominalRate = 1;
    private ExecutionType _executionType = ExecutionType.Proportional;
    private int _duration = 60;
    private boolean _forceContinue = false;

    private List<IConfigurationListener> _changeListeners = new ArrayList<IConfigurationListener>();

    public ConfigurationManager() { }
    
    public int getNumberOfThreads() {
        return _numberOfThreads; 
    }
    
    public void setNumberOfThreads(int value) {
        _numberOfThreads = value;
    }

    public MeasurementType getMeasurementType() {
        return _measurementType;
    }
    
    public void setMeasurementType(MeasurementType value) {
        _measurementType = value; 
    }

    public double getNominalRate() {
        return _nominalRate; 
    }
    
    public void setNominalRate(double value) {
        _nominalRate = value;
    }

    public ExecutionType getExecutionType() {
        return _executionType; 
    }
    
    public void setExecutionType(ExecutionType value) {
        _executionType = value;
    }

    public int getDuration() {
        return _duration; 
    }
    
    public void setDuration(int value) {
        _duration = value;
    }
    
    public boolean isForceContinue() {
    	return _forceContinue;
    }
    
    public void setForceContinue(boolean value) {
    	_forceContinue = value;
    }
    
    public void addChangeListener(IConfigurationListener listener) {
    	_changeListeners.add(listener);
    }
    
    public void removeChangeListener(IConfigurationListener listener) {
    	_changeListeners.remove(listener);
    }
    
    public void notifyChanged() {
    	for (int index = 0; index < _changeListeners.size(); index++) {
    		try {
    			IConfigurationListener listener = _changeListeners.get(index);
    			listener.onConfigurationChanged();
    		} catch (Exception ex) {
    			// Ignore and send a message to the next listener.
    		}
    	}
    }
    
}
