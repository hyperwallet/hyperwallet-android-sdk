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
 * A callback interface to handle the submission of an authentication token or an error message in
 * the case of a failure.
 *
 * <p>Authentication tokens will be used until they expire.</p>
 */
public interface HyperwalletAuthenticationTokenListener {

    /**
     * Sets the authentication token to be used when contacting the Hyperwallet platform.
     *
     * The authentication token is a JSON web token that contains as part of its claim set the principal that will
     * be interacting with the Hyperwallet platform.
     *
     * @param authenticationToken a JWT token identifying a Hyperwallet User principal
     */
    void onSuccess(String authenticationToken);

    /**
     * Notifies the Hyperwallet SDK of a failure to generate an authentication token.
     *
     * The message along with the universally unique id are used to track the occurrence of authentication token
     * retrieval failures.
     *
     * @param uuid    a universally unique identifier
     * @param message indicating the cause of the authentication token retrieval error
     */
    void onFailure(UUID uuid, String message);
}
