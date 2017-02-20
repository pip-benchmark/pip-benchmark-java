package com.digitalliving.nbenchmark;

import java.util.*;

public class RandomDataGenerator {
    private final static String AllowStringChars 
    	= "ABCDEFGHIGKLMNOPQRSTUVWXYZabcdefghigklmnopqrstuvwxyz -";

    private Random _random = new Random();

    public RandomDataGenerator() {
    }

    public List<String> randomStringList(int minCount, int maxCount, int itemSize)
    {
        List<String> result = new ArrayList<String>();
        int count = Math.max(0, minCount + _random.nextInt(maxCount - minCount));

        for (int index = 0; index < count; index++) {
            result.add(randomString(itemSize));
        }

        return result;
    }

    public String randomString(int size) {
        StringBuilder text = new StringBuilder();
        for (int index = 0; index < size; index++) {
            text.append(AllowStringChars.charAt(_random.nextInt(AllowStringChars.length())));
        }
        return text.toString();
    }

    public byte[] randomByteArray(int size) {
        byte[] result = new byte[size];
        for (int index = 0; index < size; index++) {
            result[index] = (byte)randomInteger(0, 256);
        }
        return result;
    }

    public int randomInteger(int minValue, int maxValue) {
        return (int)randomDouble(minValue, maxValue);
    }

    public double randomDouble(double minValue, double maxValue) {
        return minValue + (double)_random.nextDouble() * (maxValue - minValue);
    }

    public boolean randomBoolean()
    {
        return randomInteger(0, 2) == 1;
    }
}
