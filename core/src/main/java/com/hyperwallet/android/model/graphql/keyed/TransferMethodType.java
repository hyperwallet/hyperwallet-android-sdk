package com.hyperwallet.android.model.graphql.keyed;

import static com.hyperwallet.android.model.graphql.Connection.hasNodes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hyperwallet.android.model.graphql.Connection;
import com.hyperwallet.android.model.graphql.Fee;
import com.hyperwallet.android.model.graphql.ProcessingTime;
import com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodTypes;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Represents transfer method types available based on Country and Currency configuration
 */
public class TransferMethodType implements KeyedNode {

    private static final String TRANSFER_METHOD_CODE = NODE_CODE;
    private static final String TRANSFER_METHOD_NAME = NODE_NAME;
    private static final String TRANSFER_METHOD_FEES = "fees";
    private static final String TRANSFER_METHOD_PROCESSING_TIMES = "processingTimes";

    private final Set<Fee> mFees;
    private final String mCode;
    private final Connection<Fee> mFeeConnection;
    private final String mName;
    private final Connection<ProcessingTime> mProcessingTimeConnection;
    private ProcessingTime mProcessingTime;

    /**
     * Constructor to build {@code TransferMethodType} based on {@link JSONObject} representation
     *
     * @param transferMethodType JSON object that represents transfer method type data
     */
    public TransferMethodType(@NonNull final JSONObject transferMethodType) throws JSONException,
            NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        mCode = transferMethodType.optString(TRANSFER_METHOD_CODE);
        mName = transferMethodType.optString(TRANSFER_METHOD_NAME);
        mFees = new LinkedHashSet<>(1);
        JSONObject fees = transferMethodType.optJSONObject(TRANSFER_METHOD_FEES);
        if (fees != null && fees.length() != 0) {
            mFeeConnection = new Connection<>(fees, Fee.class);
        } else {
            mFeeConnection = null;
        }

        JSONObject processingTime = transferMethodType.optJSONObject(TRANSFER_METHOD_PROCESSING_TIMES);
        if (processingTime != null && processingTime.length() != 0) {
            mProcessingTimeConnection = new Connection<>(processingTime, ProcessingTime.class);
        } else {
            mProcessingTimeConnection = null;
        }
    }

    /**
     * @return Transfer method type code
     * @see TransferMethodTypes
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
     * @return Fees associated with this {@code TransferMethodType} instance
     */
    @NonNull
    public Set<Fee> getFees() {
        if (mFees.isEmpty() && hasNodes(mFeeConnection)) {
            mFees.addAll(mFeeConnection.getNodes());
            return mFees;
        }
        return mFees;
    }

    /**
     * Returns processing time of this {@code TransferMethodType} instance
     *
     * @return Processing time
     */
    @Nullable
    public ProcessingTime getProcessingTime() {
        if (mProcessingTime == null && hasNodes(mProcessingTimeConnection)) {
            mProcessingTime = mProcessingTimeConnection.getNodes().get(0);
            return mProcessingTime;
        }
        return mProcessingTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TransferMethodType)) return false;
        TransferMethodType that = (TransferMethodType) o;
        return Objects.equals(getCode(), that.getCode()) &&
                Objects.equals(getName(), that.getName()) &&
                Objects.equals(getProcessingTime(), that.getProcessingTime());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCode(), getName(), getProcessingTime());
    }
}
