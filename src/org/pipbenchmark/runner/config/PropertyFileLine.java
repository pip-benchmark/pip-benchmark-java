package org.pipbenchmark.runner.config;

public class PropertyFileLine {
    private String _line;
    private String _key;
    private String _value;
    private String _comment;

    public PropertyFileLine(String key, String value, String comment) {
        _key = key;
        _value = value;
        _comment = comment;
        composeNewLine();
    }

    public PropertyFileLine(String line) {
        parseLine(line);
    }

    public String getKey() {
        return _key;
    }

    public String getValue() {
        return _value;
    }
        
    public void setValue(String value) {
        _value = value;
        composeNewLine();
    }

    public String getComment() {
        return _comment; 
    }
    
    public void setComment(String value) {
        _comment = value;
        composeNewLine();
    }

    public String getLine() {
        return _line;
    }

    private void composeNewLine() {
        StringBuilder builder = new StringBuilder(255);
        if (_key != null && _key.length() > 0) {
            builder.append(encodeValue(_key));
            builder.append('=');
            builder.append(encodeValue(_value));
        }
        if (_comment != null && _comment.length() > 0) {
            builder.append(" ;");
            builder.append(_comment);
        }
        _line = builder.toString();
    }

    private void parseLine(String line) {
        _line = line;

        // Parse comment
        int commentIndex = indexOfComment(line);
        if (commentIndex >= 0) {
            _comment = line.substring(commentIndex + 1);
            line = line.substring(0, commentIndex);
        }

        // Parse key and value
        int assignmentIndex = line.indexOf('=');
        if (assignmentIndex >= 0) {
            _value = line.substring(assignmentIndex + 1);
            _value = decodeValue(_value);
            _key = line.substring(0, assignmentIndex);
            _key = decodeValue(_key);
        } else {
            _key = decodeValue(line);
            _value = "";
        }
    }

    private int indexOfComment(String value) {
        boolean partOfString = false;
        char stringDelimiter = ' ';
        for (int index = 0; index < value.length(); index++) {
            char chr = value.charAt(index);
            if (partOfString == false && chr == ';') {
                return index;
            } else if (partOfString == true && chr == stringDelimiter) {
                partOfString = false;
            } else if (partOfString == false && (chr == '\'' || chr == '\"')) {
                partOfString = true;
                stringDelimiter = chr;
            }
        }
        return -1;
    }

    private String decodeValue(String value) {
        value = value.trim();
        if (value.startsWith("'") && value.endsWith("'")) {
            value = value.substring(1, value.length() - 1);
            value = value.replace("''", "'");
        }
        if (value.startsWith("\"") && value.endsWith("\"")) {
            value = value.substring(1, value.length() - 1);
            value = value.replace("\"\"", "\"");
        }
        return value;
    }

    private String encodeValue(String value) {
        if (value == null) {
            return value;
        }

        if (value.startsWith(" ") || value.endsWith(" ") || value.indexOf(';') >= 0) {
            value = value.replace("\"", "\"\"");
            value = "\"" + value + "\"";
        }
        return value;
    }

    @Override
    public String toString() {
        return _line;
    }
}
