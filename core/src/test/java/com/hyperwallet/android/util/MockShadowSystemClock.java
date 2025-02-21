package com.hyperwallet.android.util;

import android.os.SystemClock;

import org.robolectric.annotation.Implements;

/**
 * Simple SystemClock Shadow representation
 * <p>
 * Since Gradle 7, the Shadows SystemClock no longer supported
 * by Android Shadow provided by Robolectric
 */
@Implements(value = SystemClock.class, callThroughByDefault = true)
public final class MockShadowSystemClock {
    private MockShadowSystemClock() {
    }

    public static long elapsedRealtime() {
        return 0;
    }
}