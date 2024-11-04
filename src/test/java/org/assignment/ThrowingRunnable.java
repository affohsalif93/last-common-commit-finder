package org.assignment;

@FunctionalInterface
public interface ThrowingRunnable {
    void run() throws Throwable;
}