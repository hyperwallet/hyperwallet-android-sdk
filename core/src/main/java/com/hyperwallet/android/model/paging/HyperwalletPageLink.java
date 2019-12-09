/*
 * Copyright 2018 Hyperwallet
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and
 * to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of
 * the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO
 * THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 * IN THE SOFTWARE.
 */
package com.hyperwallet.android.model.paging;

import androidx.annotation.NonNull;
import androidx.annotation.RestrictTo;

import org.json.JSONObject;

/**
 * {@code HyperwalletPageLink} representation
 */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
public class HyperwalletPageLink {
    private static final String PAGE_HREF = "href";
    private static final String PAGE_PARAMS = "params";

    private HyperwalletPageParameter mPageParameter;
    private String mPageRef;

    /**
     * Constructs a {@code HyperwalletPageLink} object from {@link JSONObject} representation
     *
     * @param fields raw data representation
     */
    public HyperwalletPageLink(@NonNull final JSONObject fields) {
        mPageRef = fields.optString(PAGE_HREF);
        mPageParameter = new HyperwalletPageParameter(fields.optJSONObject(PAGE_PARAMS));
    }

    /**
     * @return Page reference link information
     */
    public String getPageHref() {
        return mPageRef;
    }

    /**
     * @return {@link HyperwalletPageParameter}
     */
    public HyperwalletPageParameter getPageParameter() {
        return mPageParameter;
    }
}
