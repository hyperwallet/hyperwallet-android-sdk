package com.hyperwallet.android.util;

import android.os.SystemClock;

import org.robolectric.annotation.Implements;

/**
 *
 */
@Implements(value = SystemClock.class, callThroughByDefault = true)
public final class MockShadowSystemClock {
    private MockShadowSystemClock() {
    }

    public static long elapsedRealtime() {
        return 0;
    }
}