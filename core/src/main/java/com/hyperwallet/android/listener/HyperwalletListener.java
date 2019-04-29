package com.hyperwallet.android.listener;

import android.os.Handler;

import androidx.annotation.Nullable;

import com.hyperwallet.android.exception.HyperwalletException;

/**
 * The listener interface for receiving completion events from the Hyperwallet Core SDK.
 *
 * @param <T> the domain object type expected as a response to a successful request
 */
public interface HyperwalletListener<T> {

    /**
     * Notifies the {@code HyperwalletListener} that the result of the associated request was processed successfully.
     *
     * @param result the resulting domain object generated from the request
     */
    void onSuccess(@Nullable T result);

    /**
     * Notifies the {@code HyperwalletListener} that the result of the associated request was not processed
     * successfully.
     *
     * @param exception the resulting exception generated from the request
     */
    void onFailure(HyperwalletException exception);

    /**
     * The {@code Handler} to be used for processing the {@link com.hyperwallet.android.HttpTransaction}.
     *
     * <p>If no handler is specified, then the main application thread is used.</p>
     *
     * @return the {@code Handler} to be used for processing the {@code HttpTransaction}
     */
    Handler getHandler();
}
