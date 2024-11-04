package org.assignment;

import static org.junit.jupiter.api.Assertions.fail;

public class TestUtils {
    public static void assertInCauseChain(Class<? extends Throwable> expectedExceptionClass, ThrowingRunnable runnable) {
        try {
            runnable.run();
            fail("Expected exception of type " + expectedExceptionClass.getName() + " but no exception was thrown.");
        } catch (Throwable thrown) {
            Throwable cause = thrown;
            while (cause != null) {
                if (expectedExceptionClass.equals(cause.getClass())) {
                    return;
                }
                cause = cause.getCause();
            }
            fail("Expected " + expectedExceptionClass.getName() + " but got " + thrown.getClass().getName());
        }
    }

}