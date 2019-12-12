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
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.hyperwallet.android.model.transfermethod;

import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodTypes.BANK_ACCOUNT;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodTypes.WIRE_ACCOUNT;

import androidx.annotation.NonNull;
import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Represents the bank account query params fields.
 */
public class BankAccountQueryParam extends TransferMethodQueryParam {

    /**
     * Construct a {@code BankAccountQueryParam} object
     * from {@link BankAccountQueryParam.Builder}
     *
     * @param builder specified to builder this query param
     */
    private BankAccountQueryParam(@NonNull final Builder builder) {
        super(builder);
    }

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({
            BANK_ACCOUNT,
            WIRE_ACCOUNT,
    })
    public @interface BankAccountTypeQuery {
    }

    /**
     * Builder Class for the {@link BankAccountQueryParam}
     */
    public static class Builder extends HyperwalletTransferMethodBuilder<Builder> {

        @Override
        public Builder type(@NonNull @BankAccountTypeQuery String type) {
            mType = type;
            return self();
        }

        @Override
        public BankAccountQueryParam build() {
            if (mType == null) {
                type(BANK_ACCOUNT);
            }
            return new BankAccountQueryParam(this);
        }
    }
}
