package com.hyperwallet.android.model.graphql.keyed;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hyperwallet.android.model.graphql.Connection;
import com.hyperwallet.android.model.graphql.HyperwalletFee;
import com.hyperwallet.android.model.graphql.ProcessingTime;
import com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethod.TransferMethodTypes;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Represents transfer method types available based on Country and Currency configuration
 */
public class HyperwalletTransferMethodType implements KeyedNode {

    private static final String TRANSFER_METHOD_CODE = NODE_CODE;
    private static final String TRANSFER_METHOD_NAME = NODE_NAME;
    private static final String TRANSFER_METHOD_FEES = "fees";
    private static final String TRANSFER_METHOD_PROCESSING_TIME = "processingTimes";

    private final Set<HyperwalletFee> mHyperwalletFees;
    private final String mCode;
    private final Connection<HyperwalletFee> mFeeConnection;
    private final String mName;
    private final Connection<ProcessingTime> mProcessingTimeConnection;
    private ProcessingTime mProcessingTime;

    /**
     * Constructor to build HyperwalletTransferMethodType based on json
     *
     * @param transferMethodType JSON object that represents transfer method type data
     */
    public HyperwalletTransferMethodType(@NonNull final JSONObject transferMethodType) throws JSONException,
            NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        mCode = transferMethodType.optString(TRANSFER_METHOD_CODE);
        mName = transferMethodType.optString(TRANSFER_METHOD_NAME);
        mHyperwalletFees = new LinkedHashSet<>(1);
        JSONObject fees = transferMethodType.optJSONObject(TRANSFER_METHOD_FEES);
        if (fees != null && fees.length() != 0) {
            mFeeConnection = new Connection<>(fees, HyperwalletFee.class);
        } else {
            mFeeConnection = null;
        }

        JSONObject processingTime = transferMethodType.optJSONObject(TRANSFER_METHOD_PROCESSING_TIME);
        if (processingTime != null && processingTime.length() != 0) {
            mProcessingTimeConnection = new Connection<>(processingTime, ProcessingTime.class);
        } else {
            mProcessingTimeConnection = null;
        }
    }

    /**
     * @return Transfer method type code @see {@link TransferMethodTypes}
     */
    @NonNull
    @Override
    public String getCode() {
        return mCode;
    }

    /**
     * @return Transfer method type name
     */
    @NonNull
    @Override
    public String getName() {
        return mName;
    }

    /**
     * @return Fees associated with this {@code HyperwalletTransferMethodType} instance
     */
    @NonNull
    public Set<HyperwalletFee> getFees() {
        if (mFeeConnection != null && mHyperwalletFees.isEmpty()) {
            mHyperwalletFees.addAll(mFeeConnection.getNodes());
            return mHyperwalletFees;
        }
        return mHyperwalletFees;
    }

    /**
     * Returns processing time of this {@code HyperwalletTransferMethodType} instance
     *
     * @return Processing time
     */
    @Nullable
    public ProcessingTime getProcessingTime() {
        if (mProcessingTimeConnection != null && mProcessingTime == null &&
                mProcessingTimeConnection.getNodes() != null && !mProcessingTimeConnection.getNodes().isEmpty()) {
            mProcessingTime = mProcessingTimeConnection.getNodes().get(0);
            return mProcessingTime;
        }
        return mProcessingTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HyperwalletTransferMethodType)) return false;
        HyperwalletTransferMethodType that = (HyperwalletTransferMethodType) o;
        return Objects.equals(getCode(), that.getCode()) &&
                Objects.equals(getName(), that.getName()) &&
                Objects.equals(getProcessingTime(), that.getProcessingTime());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCode(), getName(), getProcessingTime());
    }
}
