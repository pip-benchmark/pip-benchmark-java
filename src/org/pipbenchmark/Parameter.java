package org.pipbenchmark;

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
        return SimpleTypeConverter.stringToBoolean(getValue()); 
    }
    
    public final void setAsBoolean(boolean value) {
        setValue(SimpleTypeConverter.booleanToString(value));
    }

    public final int getAsInteger() {
        return SimpleTypeConverter.stringToInteger(getValue(), 0);
    }
    
    public final void setAsInteger(int value) {
        setValue(SimpleTypeConverter.integerToString(value));
    }

    public final long getAsLong() {
        return SimpleTypeConverter.stringToLong(getValue(), 0); 
    }
    
    public final void setAsLong(long value) {
        setValue(SimpleTypeConverter.longToString(value));
    }

    public final float getAsFloat() {
        return SimpleTypeConverter.stringToFloat(getValue(), 0); 
    }
    
    public final void setAsFloat(float value) {
        setValue(SimpleTypeConverter.floatToString(value));
    }

    public final double getAsDouble() {
        return SimpleTypeConverter.stringToDouble(getValue(), 0); 
    }
    
    public final void setAsDouble(double value) {
        setValue(SimpleTypeConverter.doubleToString(value));
    }
}
