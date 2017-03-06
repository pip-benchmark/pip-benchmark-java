package org.pipbenchmark;

import org.pipbenchmark.util.Converter;

public class Parameter {
    private String _name;
    private String _description;
    private String _defaultValue;
    private String _value;

    public Parameter(String name, String description, String defaultValue) {
        _name = name;
        _description = description;
        _defaultValue = defaultValue;
        _value = defaultValue;
    }

    public final String getName() {
        return _name;
    }

    public final String getDescription() {
        return _description;
    }

    public final String getDefaultValue() {
        return _defaultValue;
    }

    public String getValue() {
        return _value;
    }
    
    public void setValue(String value) { 
        _value = value;
    }

    public final String getAsString() {
        return getValue();
    }
    
    public final void setAsString(String value) {
        setValue(value);
    }

    public final boolean getAsBoolean() {
        return Converter.stringToBoolean(getValue()); 
    }
    
    public final void setAsBoolean(boolean value) {
        setValue(Converter.booleanToString(value));
    }

    public final int getAsInteger() {
        return Converter.stringToInteger(getValue(), 0);
    }
    
    public final void setAsInteger(int value) {
        setValue(Converter.integerToString(value));
    }

    public final long getAsLong() {
        return Converter.stringToLong(getValue(), 0); 
    }
    
    public final void setAsLong(long value) {
        setValue(Converter.longToString(value));
    }

    public final float getAsFloat() {
        return Converter.stringToFloat(getValue(), 0); 
    }
    
    public final void setAsFloat(float value) {
        setValue(Converter.floatToString(value));
    }

    public final double getAsDouble() {
        return Converter.stringToDouble(getValue(), 0); 
    }
    
    public final void setAsDouble(double value) {
        setValue(Converter.doubleToString(value));
    }
}
