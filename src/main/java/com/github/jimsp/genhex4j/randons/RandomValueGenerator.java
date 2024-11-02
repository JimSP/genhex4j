package com.github.jimsp.genhex4j.randons;

import java.util.Random;

public final class RandomValueGenerator {
	
	private RandomValueGenerator() {}

    private static final Random RANDOM = new Random();

    public static Long generateRandomLong() {
        return Math.abs(RANDOM.nextLong() % 100);
    }

    public static String generateRandomString() {
        return "Texto_" + RANDOM.nextInt(1000);
    }

    public static Double generateRandomDouble() {
        return RANDOM.nextDouble() * 100;
    }

    public static Integer generateRandomInteger() {
        return RANDOM.nextInt(100);
    }

    public static Boolean generateRandomBoolean() {
        return RANDOM.nextBoolean();
    }
}