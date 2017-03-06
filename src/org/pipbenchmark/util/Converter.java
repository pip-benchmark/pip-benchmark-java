package org.pipbenchmark.util;

public class Converter {
	
    public static int stringToInteger(String value, int defaultValue) {
        return (int)stringToLong(value, defaultValue);
    }

    public static String integerToString(int value) {
        return Integer.toString(value);
    }

    public static long stringToLong(String value, long defaultValue)
    {
        // Check for null or empty strings
        if (value == null || value.length() == 0) {
            return defaultValue;
        }

        // Set initial values
        int position = 0;
        long resultValue = 0;

        // Check for leading minus
        boolean hasMinus = false;
        if (value.charAt(position) == '-') {
            hasMinus = true;
            position++;
        }

        // Process characters
        while (position < value.length()) {
            char currentChar = value.charAt(position++);

            if (currentChar >= '0' && currentChar <= '9') {
                // Process digits
                resultValue = resultValue * 10 + (currentChar - '0');
            } else if (currentChar == ',') {
                // Skip thousand separator
            } else {
                // Return default value if unexpected symbol found.
                return defaultValue;
            }
        }

        // Apply minus
        resultValue = hasMinus ? -resultValue : resultValue;

        return resultValue;
    }

    public static String longToString(long value) {
        return Long.toString(value);
    }

    public static float stringToFloat(String value, float defaultValue) {
        return (float)stringToDouble(value, defaultValue);
    }

    public static String floatToString(float value) {
        return Float.toString(value);
    }

    public static double stringToDouble(String value, double defaultValue) {
        // Check for null or empty strings
        if (value == null || value.length() == 0) {
            return defaultValue;
        }

        // Set initial values
        int position = 0;
        double resultValue = 0;
        boolean hasDecimalPart = false;

        // Check for leading minus
        boolean hasMinus = false;
        if (value.charAt(position) == '-') {
            hasMinus = true;
            position++;
        }

        // Process main part
        while (position < value.length()) {
            char currentChar = value.charAt(position++);

            if (currentChar >= '0' && currentChar <= '9') {
                // Process digits
                resultValue = resultValue * 10 + (currentChar - '0');
            } else if (currentChar == ',') {
                // Skip thousand separator
            } else if (currentChar == '.') {
                hasDecimalPart = true;
                break;
            } else {
                // Return default value if unexpected symbol found.
                return defaultValue;
            }
        }

        // Process decimal part
        if (hasDecimalPart) {
            double decimalPart = 0;
            long scaleDecimalPart = 1;

            while (position < value.length()) {
                char currentChar = value.charAt(position++);

                if (currentChar >= '0' && currentChar <= '9') {
                    // Process digits
                    decimalPart = decimalPart * 10 + (currentChar - '0');
                    scaleDecimalPart *= 10;
                } else {
                    // Return default value if unexpected symbol found.
                    return defaultValue;
                }
            }
            resultValue += decimalPart / scaleDecimalPart;
        }

        // Apply minus
        resultValue = hasMinus ? -resultValue : resultValue;

        return resultValue;
    }

    public static String doubleToString(double value) {
        return Double.toString(value);
    }

    public static boolean stringToBoolean(String value) {
        // Process nulls or empty strings
        if (value == null || value.length() == 0) {
            return false;
        }

        // Process single characters
        if (value.length() == 1) {
            return value.charAt(0) == '1' || value.charAt(0) == 'T' || value.charAt(0) == 'Y'
                || value.charAt(0) == 't' || value.charAt(0) == 'y';
        }

        // Process strings
        value = value.toUpperCase();
        return value.equals("TRUE") || value.equals("YES");
    }

    public static String booleanToString(boolean value) {
        return value ? "true" : "false";
    }
}
