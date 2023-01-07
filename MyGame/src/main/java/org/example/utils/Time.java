package org.example.utils;

public class Time {

    public static final long SECOND = 1_000_000_000L;

    public static long get() {
        return System.nanoTime();
    }
}
