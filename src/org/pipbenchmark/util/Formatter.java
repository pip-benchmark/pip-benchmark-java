package org.pipbenchmark.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Formatter {
    private final static DateFormat DateFormat = new SimpleDateFormat("EEE, MMMMM d, yyyy");
    private final static DateFormat TimeFormat = new SimpleDateFormat("HH:mm:ss");

    public static String padLeft(String value, int length, String padSymbol) {
        StringBuilder builder = new StringBuilder();
        builder.append(padSymbol);
        builder.append(value);
        builder.append(padSymbol);

        while (builder.length() < length + 2) {
            builder.insert(0, padSymbol);
        }

        return builder.toString();
    }

    public static String padRight(String value, int length, String padSymbol) {
        StringBuilder builder = new StringBuilder();
        builder.append(padSymbol);
        builder.append(value);
        builder.append(padSymbol);

        while (builder.length() < length + 2) {
            builder.append(padSymbol);
        }

        return builder.toString();
    }

	public static String formatNumber(double value) {
		return String.format("%.2f", value);
	}

	public static String formatDate(long ticks) {
		return DateFormat.format(new Date(ticks));
	}
	
	public static String formatTime(long ticks) {
		return TimeFormat.format(new Date(ticks));
	}
	
	public static String formatTimeSpan(long ticks) {
		long millis = ticks % 1000;
		long seconds = (ticks / 1000) % 60;
		long minutes = (ticks / 1000 / 60) % 60;
		long hours = ticks / 1000 / 60 / 60;
		return String.format("%02d:%02d:%02d.%d", hours, minutes, seconds, millis);
	}

}
