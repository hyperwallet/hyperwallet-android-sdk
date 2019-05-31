/*
 * Copyright 2018 Hyperwallet
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software
 * and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including
 * without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and
 * to permit persons to whom the Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of
 * the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING
 * BUT NOT LIMITED TO
 * THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO
 * EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN
 * AN ACTION OF
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE
 * USE OR OTHER DEALINGS
 * IN THE SOFTWARE.
 */
package com.hyperwallet.android;


import java.util.UUID;

/**
 * The {@code HyperwalletAuthenticationTokenProvider} interface provides the Hyperwallet Android Core SDK with an
 * abstraction to retrieve an authentication token. An authentication token is a JSON Web Token that will be used
 * to authenticate the HyperwalletUser to the Hyperwallet platform.
 *
 * Implementations of {@code HyperwalletAuthenticationTokenProvider} are expected to be non-blocking and thread safe.
 */
public interface HyperwalletAuthenticationTokenProvider {

    /**
     * Invoked when the Hyperwallet Android Core SDK requires an authentication token.
     *
     * <p>Implementations of this function are expected to call the
     * {@link HyperwalletAuthenticationTokenListener#onSuccess(String)} method when an authentication token is
     * generated and the {@link HyperwalletAuthenticationTokenListener#onFailure(UUID, String)} when an authentication
     * token is not generated.</p>
     *
     * @param authenticationTokenListener A listener (handler) for authentication tokens
     */
    void retrieveAuthenticationToken(HyperwalletAuthenticationTokenListener authenticationTokenListener);
}
