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
 * AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN
 * THE SOFTWARE.
 */

package com.hyperwallet.android.model.transfermethod;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hyperwallet.android.util.DateUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.Map;

/**
 * Represents the prepaid card fields
 */

public class PrepaidCard extends TransferMethod {

    private static final String DEFAULT_CARD_PACKAGE = "DEFAULT";

    /**
     * Construct a {@code PrepaidCard}
     */
    public PrepaidCard() {
        super();
        setField(TransferMethodFields.TYPE, TransferMethodTypes.PREPAID_CARD);
        setField(TransferMethodFields.CARD_PACKAGE, DEFAULT_CARD_PACKAGE);
    }

    /**
     * Construct a {@code PrepaidCard} with card package information
     *
     * @param cardPackage card package information
     */
    public PrepaidCard(@NonNull String cardPackage) {
        this();
        setField(TransferMethodFields.CARD_PACKAGE, cardPackage);
    }

    /**
     * Construct a {@code PrepaidCard} from Map of key-value pair representation
     *
     * @param fields map of key-value pair raw data
     */
    private PrepaidCard(@NonNull Map<String, Object> fields) {
        this();
        setFields(fields);
    }

    /**
     * Construct a {@code PrepaidCard} object from {@link JSONObject} representation
     *
     * @param jsonObject raw data information
     */
    public PrepaidCard(@NonNull JSONObject jsonObject) throws JSONException {
        super(jsonObject);
        setField(TransferMethodFields.TYPE, TransferMethodTypes.PREPAID_CARD);
    }

    @Nullable
    public String getType() {
        return getField(TransferMethodFields.TYPE);
    }

    @Nullable
    public String getStatus() {
        return getField(TransferMethodFields.STATUS);
    }

    @Nullable
    public Date getCreatedOn() {
        return DateUtil.fromDateTimeString(getField(TransferMethodFields.CREATED_ON));
    }

    @Nullable
    public String getTransferMethodCountry() {
        return getField(TransferMethodFields.TRANSFER_METHOD_COUNTRY);
    }

    @Nullable
    public String getTransferMethodCurrency() {
        return getField(TransferMethodFields.TRANSFER_METHOD_CURRENCY);
    }

    @Nullable
    public String getCardType() {
        return getField(TransferMethodFields.CARD_TYPE);
    }

    @Nullable
    public String getCardPackage() {
        return getField(TransferMethodFields.CARD_PACKAGE);
    }

    @Nullable
    public String getCardNumber() {
        return getField(TransferMethodFields.CARD_NUMBER);
    }

    @Nullable
    public String getCardBrand() {
        return getField(TransferMethodFields.CARD_BRAND);
    }

    @Nullable
    public String getDateOfExpiry() {
        return getField(TransferMethodFields.DATE_OF_EXPIRY);
    }

    @Nullable
    public String getPrimaryCardToken() {
        return getField(TransferMethodFields.PRIMARY_CARD_TOKEN);
    }
}