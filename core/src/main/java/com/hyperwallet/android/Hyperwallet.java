/*
 * The MIT License (MIT)
 * Copyright (c) 2018 Hyperwallet Systems Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.hyperwallet.android;

import static com.hyperwallet.android.util.HttpMethod.GET;
import static com.hyperwallet.android.util.HttpMethod.POST;
import static com.hyperwallet.android.util.HttpMethod.PUT;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hyperwallet.android.exception.AuthenticationTokenProviderException;
import com.hyperwallet.android.exception.HyperwalletInitializationException;
import com.hyperwallet.android.listener.HyperwalletListener;
import com.hyperwallet.android.model.HyperwalletStatusTransition;
import com.hyperwallet.android.model.QueryParam;
import com.hyperwallet.android.model.TypeReference;
import com.hyperwallet.android.model.graphql.HyperwalletTransferMethodConfigurationField;
import com.hyperwallet.android.model.graphql.HyperwalletTransferMethodConfigurationKey;
import com.hyperwallet.android.model.graphql.field.HyperwalletTransferMethodConfigurationFieldResult;
import com.hyperwallet.android.model.graphql.keyed.HyperwalletTransferMethodConfigurationKeyResult;
import com.hyperwallet.android.model.graphql.query.HyperwalletTransferMethodConfigurationFieldQuery;
import com.hyperwallet.android.model.graphql.query.HyperwalletTransferMethodConfigurationKeysQuery;
import com.hyperwallet.android.model.paging.HyperwalletPageList;
import com.hyperwallet.android.model.receipt.Receipt;
import com.hyperwallet.android.model.receipt.ReceiptQueryParam;
import com.hyperwallet.android.model.transfermethod.HyperwalletBankAccount;
import com.hyperwallet.android.model.transfermethod.HyperwalletBankAccountQueryParam;
import com.hyperwallet.android.model.transfermethod.HyperwalletBankCard;
import com.hyperwallet.android.model.transfermethod.HyperwalletBankCardQueryParam;
import com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethod;
import com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethodQueryParam;
import com.hyperwallet.android.model.transfermethod.PayPalAccount;
import com.hyperwallet.android.model.transfermethod.PayPalAccountQueryParam;
import com.hyperwallet.android.model.user.HyperwalletUser;

import org.json.JSONException;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The {@code Hyperwallet} class is an Android specific implementation of the
 * <a href="https://portal.hyperwallet.com/docs">Hyperwallet platform User APIs.</a>
 *
 * <p>A single instance of the {@code Hyperwallet} class is maintained. Resetting the current instance by calling
 * {@link #getInstance(HyperwalletAuthenticationTokenProvider)} is critical when switching between authenticated Users.
 * Failure to do so will result in incorrect access and incorrect modifications to User data.</p>
 *
 * <p>Authentication with the Hyperwallet platform is accomplished through the usage of JSON Web Tokens. At
 * instantiation an {@link HyperwalletAuthenticationTokenProvider} is set as a member variable to provide the
 * {@code Hyperwallet} class with an authentication token upon request.</p>
 *
 * <p>Method invocations that require a {@link HyperwalletListener} will either process the request in the current
 * thread or in a background thread depending on if the {@link HyperwalletListener#getHandler()} returns null
 * or not.</p>
 */
public class Hyperwallet {

    private static final int EXECUTOR_POOL_SIZE = 2;
    private static final String TAG = Hyperwallet.class.getName();


    private static Hyperwallet sInstanceLast;

    private final ExecutorService mExecutor;
    private final HyperwalletAuthenticationTokenProvider mHyperwalletAuthenticationTokenProvider;

    private Configuration mConfiguration;

    private Hyperwallet(@NonNull final HyperwalletAuthenticationTokenProvider hyperwalletAuthenticationTokenProvider) {
        mExecutor = Executors.newFixedThreadPool(EXECUTOR_POOL_SIZE);
        mHyperwalletAuthenticationTokenProvider = hyperwalletAuthenticationTokenProvider;
    }

    /**
     * Creates a new instance of the Hyperwallet Core SDK interface object. If a previously created instance exists,
     * it will be replaced.
     *
     * @param hyperwalletAuthenticationTokenProvider a provider of Hyperwallet authentication tokens; must not be null
     * @return A {@code Hyperwallet} instance
     */
    public static Hyperwallet getInstance(
            @NonNull final HyperwalletAuthenticationTokenProvider hyperwalletAuthenticationTokenProvider) {
        sInstanceLast = new Hyperwallet(hyperwalletAuthenticationTokenProvider);
        return sInstanceLast;
    }

    /**
     * Returns the previously initialized instance of the Hyperwallet Core SDK interface object.
     *
     * @return A previously initialized instance of Hyperwallet; maintaining its
     * {@link HyperwalletAuthenticationTokenProvider}
     * @throws HyperwalletInitializationException if this Hyperwallet object was not initialized with
     *                                            {@link #getInstance(HyperwalletAuthenticationTokenProvider)}
     */
    public static Hyperwallet getDefault() {
        if (sInstanceLast == null) {
            throw new HyperwalletInitializationException();
        }
        return sInstanceLast;
    }

    public static void clearInstance() {
        sInstanceLast = null;
    }

    /**
     * Creates a {@link HyperwalletBankAccount} for the User associated with the authentication token returned from
     * {@link HyperwalletAuthenticationTokenProvider#retrieveAuthenticationToken(HyperwalletAuthenticationTokenListener)}.
     *
     * <p>The {@link HyperwalletListener} that is passed in to this method invocation will receive the responses from
     * processing the request.</p>
     *
     * <p>This function will request a new authentication token via {@link HyperwalletAuthenticationTokenProvider}
     * if the current one is expired or about to expire.</p>
     *
     * @param bankAccount the {@code HyperwalletBankAccount} to be created; must not be null
     * @param listener    the callback handler of responses from the Hyperwallet platform; must not be null
     */
    public void createBankAccount(@NonNull final HyperwalletBankAccount bankAccount,
            @NonNull final HyperwalletListener<HyperwalletBankAccount> listener) {
        PathFormatter pathFormatter = new PathFormatter("users/{0}/bank-accounts");

        RestTransaction.Builder builder = new RestTransaction.Builder<>(POST, pathFormatter,
                new TypeReference<HyperwalletBankAccount>() {
                }, listener).jsonModel(bankAccount);

        performRestTransaction(builder, listener);
    }

    /**
     * Returns the list of {@link HyperwalletBankAccount}s for the User associated with the authentication token
     * returned from
     * {@link HyperwalletAuthenticationTokenProvider#retrieveAuthenticationToken(HyperwalletAuthenticationTokenListener)},
     * or an empty {@code List} if non exist.
     *
     * <p>The ordering and filtering of {@code HyperwalletBankAccounts} will be based on the criteria specified within
     * the {@link HyperwalletBankAccountQueryParam} object, if it is not null. Otherwise the default ordering and
     * filtering will be applied:</p>
     *
     * <ul>
     * <li>Offset: 0</li>
     * <li>Limit: 10</li>
     * <li>Created Before: N/A</li>
     * <li>Created After: N/A</li>
     * <li>Type: Bank Account</li>
     * <li>Status: All</li>
     * <li>Sort By: Created On</li>
     * </ul>
     *
     * <p>The {@link HyperwalletListener} that is passed in to this method invocation will receive the responses from
     * * processing the request.</p>
     *
     * <p>This function will request a new authentication token via {@link HyperwalletAuthenticationTokenProvider}
     * if the current one is expired or about to expire.</p>
     *
     * @param queryParam the ordering and filtering criteria
     * @param listener   the callback handler of responses from the Hyperwallet platform; must not be null
     */
    public void listBankAccounts(@Nullable final HyperwalletBankAccountQueryParam queryParam,
            @NonNull final HyperwalletListener<HyperwalletPageList<HyperwalletBankAccount>> listener) {
        Map<String, String> urlQuery = buildUrlQueryIfRequired(queryParam);
        PathFormatter pathFormatter = new PathFormatter("users/{0}/bank-accounts");

        RestTransaction.Builder builder = new RestTransaction.Builder<>(GET, pathFormatter,
                new TypeReference<HyperwalletPageList<HyperwalletBankAccount>>() {
                }, listener).query(urlQuery);

        performRestTransaction(builder, listener);
    }

    /**
     * Creates a {@link HyperwalletBankCard} for the User associated with the authentication token returned from
     * {@link HyperwalletAuthenticationTokenProvider#retrieveAuthenticationToken(HyperwalletAuthenticationTokenListener)}.
     *
     * <p>The {@link HyperwalletListener} that is passed in to this method invocation will receive the responses from
     * * processing the request.</p>
     *
     * <p>This function will request a new authentication token via {@link HyperwalletAuthenticationTokenProvider}
     * if the current one is expired or about to expire.</p>
     *
     * @param bankCard the {@code HyperwalletBankCard} to be created; must not be null
     * @param listener the callback handler of responses from the Hyperwallet platform; must not be null
     */
    public void createBankCard(@NonNull final HyperwalletBankCard bankCard,
            @NonNull final HyperwalletListener<HyperwalletBankCard> listener) {
        PathFormatter pathFormatter = new PathFormatter("users/{0}/bank-cards");

        RestTransaction.Builder builder = new RestTransaction.Builder<>(POST, pathFormatter,
                new TypeReference<HyperwalletBankCard>() {
                }, listener).jsonModel(bankCard);

        performRestTransaction(builder, listener);
    }

    /**
     * Creates a {@link PayPalAccount} for the User associated with the authentication token returned from
     * {@link HyperwalletAuthenticationTokenProvider#retrieveAuthenticationToken(HyperwalletAuthenticationTokenListener)}.
     *
     * <p>The {@link HyperwalletListener} that is passed in to this method invocation will receive the responses from
     * processing the request.</p>
     *
     * <p>This function will request a new authentication token via {@link HyperwalletAuthenticationTokenProvider}
     * if the current one is expired or about to expire.</p>
     *
     * @param payPalAccount the {@code PayPalAccount} to be created; must not be null
     * @param listener      the callback handler of responses from the Hyperwallet platform; must not be null
     */
    public void createPayPalAccount(@NonNull final PayPalAccount payPalAccount,
            @NonNull final HyperwalletListener<PayPalAccount> listener) {
        PathFormatter pathFormatter = new PathFormatter("users/{0}/paypal-accounts");

        RestTransaction.Builder builder = new RestTransaction.Builder<>(POST, pathFormatter,
                new TypeReference<PayPalAccount>() {
                }, listener).jsonModel(payPalAccount);

        performRestTransaction(builder, listener);
    }

    /**
     * Returns the {@link HyperwalletBankAccount} linked to the transfer method token specified, or null if none exists.
     *
     * <p>The {@link HyperwalletListener} that is passed in to this method invocation will receive the responses from
     * processing the request.</p>
     *
     * <p>This function will request a new authentication token via {@link HyperwalletAuthenticationTokenProvider}
     * if the current one is expired or about to expire.</p>
     *
     * @param transferMethodToken the Hyperwallet specific unique identifier for the {@code HyperwalletBankAccount}
     *                            being requested; must not be null
     * @param listener            the callback handler of responses from the Hyperwallet platform; must not be null
     */
    public void getBankAccount(@NonNull final String transferMethodToken,
            @NonNull final HyperwalletListener<HyperwalletBankAccount> listener) {
        PathFormatter pathFormatter = new PathFormatter("users/{0}/bank-accounts/{1}", transferMethodToken);

        RestTransaction.Builder builder = new RestTransaction.Builder<>(GET, pathFormatter,
                new TypeReference<HyperwalletBankAccount>() {
                }, listener);

        performRestTransaction(builder, listener);
    }

    /**
     * Returns the {@link HyperwalletBankCard} linked to the transfer method token specified, or null if none exists.
     *
     * <p>The {@link HyperwalletListener} that is passed in to this method invocation will receive the responses from
     * processing the request.</p>
     *
     * <p>This function will request a new authentication token via {@link HyperwalletAuthenticationTokenProvider}
     * if the current one is expired or about to expire.</p>
     *
     * @param transferMethodToken the Hyperwallet specific unique identifier for the {@code HyperwalletBankCard}
     *                            being requested; must not be null
     * @param listener            the callback handler of responses from the Hyperwallet platform; must not be null
     */
    public void getBankCard(@NonNull final String transferMethodToken,
            @NonNull final HyperwalletListener<HyperwalletBankCard> listener) {
        PathFormatter pathFormatter = new PathFormatter("users/{0}/bank-cards/{1}", transferMethodToken);

        RestTransaction.Builder builder = new RestTransaction.Builder<>(GET, pathFormatter,
                new TypeReference<HyperwalletBankCard>() {
                }, listener);
        performRestTransaction(builder, listener);
    }

    /**
     * Returns the {@link HyperwalletUser} linked to the token specified, or null if none exists.
     *
     * <p>The {@link HyperwalletListener} that is passed in to this method invocation will receive the responses from
     * processing the request.</p>
     *
     * <p>This function will request a new authentication token via {@link HyperwalletAuthenticationTokenProvider}
     * if the current one is expired or about to expire.</p>
     *
     * @param listener the callback handler of responses from the Hyperwallet platform; must not be null
     */
    public void getUser(@NonNull final HyperwalletListener<HyperwalletUser> listener) {
        PathFormatter pathFormatter = new PathFormatter("users/{0}");

        RestTransaction.Builder builder = new RestTransaction.Builder<>(GET, pathFormatter,
                new TypeReference<HyperwalletUser>() {
                }, listener);

        performRestTransaction(builder, listener);
    }

    /**
     * Updates the {@link HyperwalletBankAccount} for the User associated with the authentication token returned from
     * {@link HyperwalletAuthenticationTokenProvider#retrieveAuthenticationToken(HyperwalletAuthenticationTokenListener)}.
     *
     * <p>To identify the {@code HyperwalletBankAccount} that is going to be updated, the transfer method token must be
     * set as part of the {@code HyperwalletBankAccount} object passed in.</p>
     *
     * <p>The {@link HyperwalletListener} that is passed in to this method invocation will receive the responses from
     * processing the request.</p>
     *
     * <p>This function will request a new authentication token via {@link HyperwalletAuthenticationTokenProvider}
     * if the current one is expired or about to expire.</p>
     *
     * @param bankAccount the {@code HyperwalletBankAccount} to be created; must not be null
     * @param listener    the callback handler of responses from the Hyperwallet platform; must not be null
     */
    public void updateBankAccount(@NonNull final HyperwalletBankAccount bankAccount,
            @NonNull final HyperwalletListener<HyperwalletBankAccount> listener) {
        PathFormatter pathFormatter = new PathFormatter("users/{0}/bank-accounts/{1}",
                bankAccount.getField(HyperwalletTransferMethod.TransferMethodFields.TOKEN));

        RestTransaction.Builder builder = new RestTransaction.Builder<>(PUT, pathFormatter,
                new TypeReference<HyperwalletBankAccount>() {
                }, listener).jsonModel(bankAccount);

        performRestTransaction(builder, listener);
    }

    /**
     * Updates the {@link HyperwalletBankCard} for the User associated with the authentication token returned from
     * {@link HyperwalletAuthenticationTokenProvider#retrieveAuthenticationToken(HyperwalletAuthenticationTokenListener)}.
     *
     * <p>To identify the {@code HyperwalletBankCard} that is going to be updated, the transfer method token must be
     * set as part of the
     * {@code HyperwalletBankCard} object passed in.</p>
     *
     * <p>The {@link HyperwalletListener} that is passed in to this method invocation will receive the responses from
     * processing the request.</p>
     *
     * <p>This function will request a new authentication token via {@link HyperwalletAuthenticationTokenProvider}
     * if the current one is expired or about to expire.</p>
     *
     * @param bankCard the {@code HyperwalletBankCard} to be created; must not be null
     * @param listener the callback handler of responses from the Hyperwallet platform; must not be null
     */
    public void updateBankCard(@NonNull final HyperwalletBankCard bankCard,
            @NonNull final HyperwalletListener<HyperwalletBankCard> listener) {
        PathFormatter pathFormatter = new PathFormatter("users/{0}/bank-cards/{1}",
                bankCard.getField(HyperwalletTransferMethod.TransferMethodFields.TOKEN));

        RestTransaction.Builder builder = new RestTransaction.Builder<>(PUT, pathFormatter,
                new TypeReference<HyperwalletBankCard>() {
                }, listener).jsonModel(bankCard);

        performRestTransaction(builder, listener);
    }

    /**
     * Updates the {@link PayPalAccount} for the User associated with the authentication token returned from
     * {@link HyperwalletAuthenticationTokenProvider#retrieveAuthenticationToken(HyperwalletAuthenticationTokenListener)}.
     *
     * <p>To identify the {@code PayPalAccount} that is going to be updated, the transfer method token must be
     * set as part of the {@code PayPalAccount} object passed in.</p>
     *
     * <p>The {@link HyperwalletListener} that is passed in to this method invocation will receive the responses from
     * processing the request.</p>
     *
     * <p>This function will request a new authentication token via {@link HyperwalletAuthenticationTokenProvider}
     * if the current one is expired or about to expire.</p>
     *
     * @param payPalAccount the {@code PayPalAccount} to be created; must not be null
     * @param listener      the callback handler of responses from the Hyperwallet platform; must not be null
     */
    public void updatePayPalAccount(@NonNull final PayPalAccount payPalAccount,
            @NonNull final HyperwalletListener<PayPalAccount> listener) {
        PathFormatter pathFormatter = new PathFormatter("users/{0}/paypal-accounts/{1}",
                payPalAccount.getField(HyperwalletTransferMethod.TransferMethodFields.TOKEN));

        RestTransaction.Builder builder = new RestTransaction.Builder<>(PUT, pathFormatter,
                new TypeReference<PayPalAccount>() {
                }, listener).jsonModel(payPalAccount);

        performRestTransaction(builder, listener);
    }

    /**
     * Deactivates the {@link HyperwalletBankAccount} linked to the transfer method token specified. The
     * {@code HyperwalletBankAccount} being deactivated must belong to the User that is associated with the
     * authentication token returned from
     * {@link HyperwalletAuthenticationTokenProvider#retrieveAuthenticationToken(HyperwalletAuthenticationTokenListener)}.
     *
     * <p>The {@link HyperwalletListener} that is passed in to this method invocation will receive the responses from
     * processing the request.</p>
     *
     * <p>This function will request a new authentication token via {@link HyperwalletAuthenticationTokenProvider}
     * if the current one is expired or about to expire.</p>
     *
     * @param transferMethodToken the Hyperwallet specific unique identifier for the {@code HyperwalletBankAccount}
     *                            being deactivated; must not be null
     * @param notes               a note regarding the status change
     * @param listener            the callback handler of responses from the Hyperwallet platform; must not be null
     */
    public void deactivateBankAccount(@NonNull final String transferMethodToken, @Nullable final String notes,
            @NonNull final HyperwalletListener<HyperwalletStatusTransition> listener) {
        PathFormatter pathFormatter = new PathFormatter("users/{0}/bank-accounts/{1}/status-transitions",
                transferMethodToken);

        final HyperwalletStatusTransition deactivatedStatusTransition = new HyperwalletStatusTransition(
                HyperwalletStatusTransition.StatusDefinition.DE_ACTIVATED);
        deactivatedStatusTransition.setNotes(notes);
        RestTransaction.Builder builder = new RestTransaction.Builder<>(POST, pathFormatter,
                new TypeReference<HyperwalletStatusTransition>() {
                }, listener).jsonModel(deactivatedStatusTransition);

        performRestTransaction(builder, listener);
    }

    /**
     * Deactivates the {@link HyperwalletBankCard} linked to the transfer method token specified. The
     * {@code HyperwalletBankCard} being deactivated must\ belong to the User that is associated with the
     * authentication token returned from
     * {@link HyperwalletAuthenticationTokenProvider#retrieveAuthenticationToken(HyperwalletAuthenticationTokenListener)}.
     *
     * <p>The {@link HyperwalletListener} that is passed in to this method invocation will receive the responses from
     * processing the request.</p>
     *
     * <p>This function will request a new authentication token via {@link HyperwalletAuthenticationTokenProvider}
     * if the current one is expired or about to expire.</p>
     *
     * @param transferMethodToken the Hyperwallet specific unique identifier for the {@code HyperwalletBankCard} being
     *                            deactivated; must not be null
     * @param notes               a note regarding the status change
     * @param listener            the callback handler of responses from the Hyperwallet platform; must not be null
     */
    public void deactivateBankCard(@NonNull final String transferMethodToken, @Nullable final String notes,
            @NonNull final HyperwalletListener<HyperwalletStatusTransition> listener) {
        PathFormatter pathFormatter = new PathFormatter("users/{0}/bank-cards/{1}/status-transitions",
                transferMethodToken);

        final HyperwalletStatusTransition deactivatedStatusTransition = new HyperwalletStatusTransition(
                HyperwalletStatusTransition.StatusDefinition.DE_ACTIVATED);
        deactivatedStatusTransition.setNotes(notes);
        RestTransaction.Builder builder = new RestTransaction.Builder<>(POST, pathFormatter,
                new TypeReference<HyperwalletStatusTransition>() {
                }, listener).jsonModel(deactivatedStatusTransition);

        performRestTransaction(builder, listener);
    }

    /**
     * Deactivates the {@link PayPalAccount} linked to the transfer method token specified. The
     * {@code PayPalAccount} being deactivated must belong to the User that is associated with the
     * authentication token returned from
     * {@link HyperwalletAuthenticationTokenProvider#retrieveAuthenticationToken(HyperwalletAuthenticationTokenListener)}.
     *
     * <p>The {@link HyperwalletListener} that is passed in to this method invocation will receive the responses from
     * processing the request.</p>
     *
     * <p>This function will request a new authentication token via {@link HyperwalletAuthenticationTokenProvider}
     * if the current one is expired or about to expire.</p>
     *
     * @param transferMethodToken the Hyperwallet specific unique identifier for the {@code PayPalAccount}
     *                            being deactivated; must not be null
     * @param notes               a note regarding the status change
     * @param listener            the callback handler of responses from the Hyperwallet platform; must not be null
     */
    public void deactivatePayPalAccount(@NonNull final String transferMethodToken, @Nullable final String notes,
            @NonNull final HyperwalletListener<HyperwalletStatusTransition> listener) {
        PathFormatter pathFormatter = new PathFormatter("users/{0}/paypal-accounts/{1}/status-transitions",
                transferMethodToken);

        final HyperwalletStatusTransition deactivatedStatusTransition = new HyperwalletStatusTransition(
                HyperwalletStatusTransition.StatusDefinition.DE_ACTIVATED);
        deactivatedStatusTransition.setNotes(notes);
        RestTransaction.Builder builder = new RestTransaction.Builder<>(POST, pathFormatter,
                new TypeReference<HyperwalletStatusTransition>() {
                }, listener).jsonModel(deactivatedStatusTransition);

        performRestTransaction(builder, listener);
    }

    /**
     * Returns the {@link HyperwalletTransferMethod} (Bank Account, Bank Card, PayPay Account, Prepaid Card,
     * Paper Checks) for the User associated with the authentication token returned from
     * {@link HyperwalletAuthenticationTokenProvider#retrieveAuthenticationToken(HyperwalletAuthenticationTokenListener)},
     * or an empty {@code List} if non exist.
     *
     * <p>The ordering and filtering of {@code HyperwalletTransferMethod}s will be based on the criteria specified
     * within the {@link HyperwalletTransferMethodQueryParam} object, if it is not null. Otherwise the default
     * ordering and filtering will be applied.</p>
     *
     * <ul>
     * <li>Offset: 0</li>
     * <li>Limit: 10</li>
     * <li>Created Before: N/A</li>
     * <li>Created After: N/A</li>
     * <li>Type: Bank Account</li>
     * <li>Status: All</li>
     * <li>Sort By: Created On</li>
     * </ul>
     *
     * <p>The {@link HyperwalletListener} that is passed in to this method invocation will receive the responses from
     * processing the request.</p>
     *
     * <p>This function will request a new authentication token via {@link HyperwalletAuthenticationTokenProvider}
     * if the current one is expired or about to expire.</p>
     *
     * @param queryParam the ordering and filtering criteria
     * @param listener   the callback handler of responses from the Hyperwallet platform; must not be null
     */
    public void listTransferMethods(@Nullable final HyperwalletTransferMethodQueryParam queryParam,
            @NonNull final HyperwalletListener<HyperwalletPageList<HyperwalletTransferMethod>> listener) {
        Map<String, String> urlQuery = buildUrlQueryIfRequired(queryParam);
        PathFormatter pathFormatter = new PathFormatter("users/{0}/transfer-methods");

        RestTransaction.Builder builder = new RestTransaction.Builder<>(GET, pathFormatter,
                new TypeReference<HyperwalletPageList<HyperwalletTransferMethod>>() {
                }, listener).query(urlQuery);

        performRestTransaction(builder, listener);
    }

    /**
     * Returns the {@link HyperwalletBankCard} for the User associated with the authentication token returned from
     * {@link HyperwalletAuthenticationTokenProvider#retrieveAuthenticationToken(HyperwalletAuthenticationTokenListener)},
     * or an empty {@code List} if non exist.
     *
     * <p>The ordering and filtering of {@code HyperwalletBankCard} will be based on the criteria specified within the
     * {@link HyperwalletBankAccountQueryParam} object, if it is not null. Otherwise the default ordering and
     * filtering will be applied.</p>
     *
     * <ul>
     * <li>Offset: 0</li>
     * <li>Limit: 10</li>
     * <li>Created Before: N/A</li>
     * <li>Created After: N/A</li>
     * <li>Type: Bank Card</li>
     * <li>Status: All</li>
     * <li>Sort By: Created On</li>
     * </ul>
     *
     * <p>The {@link HyperwalletListener} that is passed in to this method invocation will receive the responses from
     * processing the request.</p>
     *
     * <p>This function will request a new authentication token via {@link HyperwalletAuthenticationTokenProvider}
     * if the current one is expired or about to expire.</p>
     *
     * @param queryParam the ordering and filtering criteria
     * @param listener   the callback handler of responses from the Hyperwallet platform; must not be null
     */
    public void listBankCards(@Nullable final HyperwalletBankCardQueryParam queryParam,
            @NonNull final HyperwalletListener<HyperwalletPageList<HyperwalletBankCard>> listener) {
        Map<String, String> urlQuery = buildUrlQueryIfRequired(queryParam);
        PathFormatter pathFormatter = new PathFormatter("users/{0}/bank-cards");
        RestTransaction.Builder builder = new RestTransaction.Builder<>(GET, pathFormatter,
                new TypeReference<HyperwalletPageList<HyperwalletBankCard>>() {
                }, listener).query(urlQuery);

        performRestTransaction(builder, listener);
    }

    /**
     * Returns the {@link PayPalAccount} for the User associated with the authentication token returned from
     * {@link HyperwalletAuthenticationTokenProvider#retrieveAuthenticationToken(HyperwalletAuthenticationTokenListener)},
     * or an empty {@code List} if non exist.
     *
     * <p>The ordering and filtering of {@code PayPalAccount} will be based on the criteria specified within the
     * {@link PayPalAccountQueryParam} object, if it is not null. Otherwise the default ordering and
     * filtering will be applied.</p>
     *
     * <ul>
     * <li>Offset: 0</li>
     * <li>Limit: 10</li>
     * <li>Created Before: N/A</li>
     * <li>Created After: N/A</li>
     * <li>Type: PAYPAL_ACCOUNT</li>
     * <li>Status: All</li>
     * <li>Sort By: Created On</li>
     * </ul>
     *
     * <p>The {@link HyperwalletListener} that is passed in to this method invocation will receive the responses from
     * processing the request.</p>
     *
     * <p>This function will request a new authentication token via {@link HyperwalletAuthenticationTokenProvider}
     * if the current one is expired or about to expire.</p>
     *
     * @param queryParam the ordering and filtering criteria
     * @param listener   the callback handler of responses from the Hyperwallet platform; must
     *                   not be null
     */
    public void listPayPalAccounts(
            @Nullable final PayPalAccountQueryParam queryParam,
            @NonNull final HyperwalletListener<HyperwalletPageList<PayPalAccount>> listener) {
        Map<String, String> urlQuery = buildUrlQueryIfRequired(queryParam);
        PathFormatter pathFormatter = new PathFormatter("users/{0}/paypal-accounts");
        RestTransaction.Builder builder = new RestTransaction.Builder<>(GET, pathFormatter,
                new TypeReference<HyperwalletPageList<PayPalAccount>>() {
                }, listener).query(urlQuery);

        performRestTransaction(builder, listener);
    }

    /**
     * Returns the {@link PayPalAccount} linked to the transfer method token specified, or null if none exists.
     *
     * <p>The {@link HyperwalletListener} that is passed in to this method invocation will receive the responses from
     * processing the request.</p>
     *
     * <p>This function will request a new authentication token via {@link HyperwalletAuthenticationTokenProvider}
     * if the current one is expired or about to expire.</p>
     *
     * @param transferMethodToken the Hyperwallet specific unique identifier for the {@code PayPalAccount}
     *                            being requested; must not be null
     * @param listener            the callback handler of responses from the Hyperwallet platform; must not be null
     */
    public void getPayPalAccount(@NonNull final String transferMethodToken,
            @NonNull final HyperwalletListener<PayPalAccount> listener) {
        PathFormatter pathFormatter = new PathFormatter("users/{0}/paypal-accounts/{1}", transferMethodToken);

        RestTransaction.Builder builder = new RestTransaction.Builder<>(GET, pathFormatter,
                new TypeReference<PayPalAccount>() {
                }, listener);

        performRestTransaction(builder, listener);
    }

    /**
     * Returns the transfer method configuration key set, processing times, and fees for the User that is associated
     * with the authentication token returned from
     * {@link HyperwalletAuthenticationTokenProvider#retrieveAuthenticationToken(HyperwalletAuthenticationTokenListener)}.
     *
     * <p>The {@link HyperwalletListener} that is passed in to this method invocation will receive the responses from
     * processing the request.</p>
     *
     * <p>This function will request a new authentication token via {@link HyperwalletAuthenticationTokenProvider}
     * if the current one is expired or about to expire.</p>
     *
     * @param query    containing the transfer method configuration key query, must not be null
     * @param listener the callback handler of responses from the Hyperwallet platform; must not be null
     */
    public void retrieveTransferMethodConfigurationKeys(
            @NonNull final HyperwalletTransferMethodConfigurationKeysQuery query,
            @NonNull final HyperwalletListener<HyperwalletTransferMethodConfigurationKey> listener) {
        GqlTransaction.Builder<HyperwalletTransferMethodConfigurationKeyResult> builder = new GqlTransaction.Builder<>(
                query, new TypeReference<HyperwalletTransferMethodConfigurationKeyResult>() {
        }, listener);

        performGqlTransaction(builder, listener);
    }

    /**
     * Returns the transfer method configuration field set for the User that is associated with the authentication
     * token returned from
     * {@link HyperwalletAuthenticationTokenProvider#retrieveAuthenticationToken(HyperwalletAuthenticationTokenListener)}.
     *
     * <p>The {@link HyperwalletListener} that is passed in to this method invocation will receive the responses from
     * processing the request.</p>
     *
     * <p>This function will request a new authentication token via {@link HyperwalletAuthenticationTokenProvider}
     * if the current one is expired or about to expire.</p>
     *
     * @param query    containing a transfer method configuration key tuple of
     *                 country, currency, transfer method type and profile,
     *                 must not be null
     * @param listener the callback handler of responses from the Hyperwallet platform; must not be null
     */
    public void retrieveTransferMethodConfigurationFields(
            @NonNull final HyperwalletTransferMethodConfigurationFieldQuery query,
            @NonNull final HyperwalletListener<HyperwalletTransferMethodConfigurationField> listener) {

        GqlTransaction.Builder<HyperwalletTransferMethodConfigurationFieldResult> builder =
                new GqlTransaction.Builder<>(query,
                        new TypeReference<HyperwalletTransferMethodConfigurationFieldResult>() {
                        }, listener);
        performGqlTransaction(builder, listener);
    }


    /**
     * Returns the list of {@link Receipt}s for the User associated with the authentication token
     * returned from
     * {@link HyperwalletAuthenticationTokenProvider#retrieveAuthenticationToken(HyperwalletAuthenticationTokenListener)},
     * or an empty {@code List} if non exist.
     *
     * <p>The ordering and filtering of {@code HyperwalletReceipts} will be based on the criteria specified within
     * the {@link ReceiptQueryParam} object, if it is not null. Otherwise the default ordering and
     * filtering will be applied:</p>
     *
     * <ul>
     * <li>Offset: 0</li>
     * <li>Limit: 10</li>
     * <li>Created Before: N/A</li>
     * <li>Created After: N/A</li>
     * <li>currency: N/A</li>
     * <li>Sort By: Created On, Type, Amount, Currency</li>
     * </ul>
     *
     * <p>The {@link HyperwalletListener} that is passed in to this method invocation will receive the responses from
     * * processing the request.</p>
     *
     * <p>This function will request a new authentication token via {@link HyperwalletAuthenticationTokenProvider}
     * if the current one is expired or about to expire.</p>
     *
     * @param receiptQueryParam the ordering and filtering criteria
     * @param listener          the callback handler of responses from the Hyperwallet platform; must not be null
     */
    public void listUserReceipts(@NonNull final ReceiptQueryParam receiptQueryParam,
            @NonNull final HyperwalletListener<HyperwalletPageList<Receipt>> listener) {
        Map<String, String> urlQuery = receiptQueryParam.buildQuery();
        PathFormatter pathFormatter = new PathFormatter("users/{0}/receipts");

        RestTransaction.Builder builder = new RestTransaction.Builder<>(GET, pathFormatter,
                new TypeReference<HyperwalletPageList<Receipt>>() {
                }, listener).query(urlQuery);

        performRestTransaction(builder, listener);
    }

    /**
     * Returns the list of prepaid card {@link Receipt}s for the User associated with the authentication token
     * returned from
     * {@link HyperwalletAuthenticationTokenProvider#retrieveAuthenticationToken(HyperwalletAuthenticationTokenListener)},
     * or an empty {@code List} if non exist.
     *
     * <p>The ordering and filtering of {@code HyperwalletReceipts} will be based on the criteria specified within
     * the {@link ReceiptQueryParam} object, if it is not null. Otherwise the default ordering and
     * filtering will be applied:</p>
     *
     * <ul>
     * <li>Created Before: N/A</li>
     * <li>Created After: N/A</li>
     * </ul>
     *
     * <p>The {@link HyperwalletListener} that is passed in to this method invocation will receive the responses from
     * * processing the request.</p>
     *
     * <p>This function will request a new authentication token via {@link HyperwalletAuthenticationTokenProvider}
     * if the current one is expired or about to expire.</p>
     *
     * @param prepaidCardToken  the token for prepaid card
     * @param receiptQueryParam the filtering criteria
     * @param listener          the callback handler of responses from the Hyperwallet platform; must not be null
     */
    public void listPrepaidCardReceipts(@NonNull final String prepaidCardToken,
            @NonNull final ReceiptQueryParam receiptQueryParam,
            @NonNull final HyperwalletListener<HyperwalletPageList<Receipt>> listener) {
        Map<String, String> urlQuery = receiptQueryParam.buildQuery();
        PathFormatter pathFormatter = new PathFormatter("users/{0}/prepaid-cards/{1}/receipts", prepaidCardToken);

        RestTransaction.Builder builder = new RestTransaction.Builder<>(GET, pathFormatter,
                new TypeReference<HyperwalletPageList<Receipt>>() {
                }, listener).query(urlQuery);

        performRestTransaction(builder, listener);
    }

    private void performGqlTransaction(@NonNull final GqlTransaction.Builder builder,
            @NonNull final HyperwalletListener listener) {
        if (mConfiguration == null || mConfiguration.isStale()) {
            mHyperwalletAuthenticationTokenProvider.retrieveAuthenticationToken(
                    new HyperwalletAuthenticationTokenListener() {
                        @Override
                        public void onSuccess(String authenticationToken) {
                            try {
                                mConfiguration = new Configuration(authenticationToken);
                                GqlTransaction transaction =
                                        builder.build(mConfiguration.getGraphQlUri(), mConfiguration.getUserToken());
                                mExecutor.submit(transaction);
                            } catch (final JSONException e) {
                                if (listener.getHandler() == null) {
                                    listener.onFailure(ExceptionMapper
                                            .toHyperwalletException(e));
                                } else {
                                    listener.getHandler().post(new Runnable() {
                                        @Override
                                        public void run() {
                                            listener.onFailure(ExceptionMapper.toHyperwalletException(e));
                                        }
                                    });
                                }
                            }
                        }

                        @Override
                        public void onFailure(UUID uuid, String message) {
                            final String logMessage = MessageFormat
                                    .format("Integrator was unable to provide an authentication token. \nId: {0} "
                                                    + "Message: {1}",
                                            uuid.toString(), message);
                            if (listener.getHandler() == null) {
                                listener.onFailure(ExceptionMapper
                                        .toHyperwalletException(new AuthenticationTokenProviderException(logMessage)));
                            } else {
                                listener.getHandler().post(new Runnable() {
                                    @Override
                                    public void run() {
                                        listener.onFailure(ExceptionMapper
                                                .toHyperwalletException(
                                                        new AuthenticationTokenProviderException(logMessage)));
                                    }
                                });
                            }
                        }
                    });
        } else {
            GqlTransaction transaction = builder.build(mConfiguration.getGraphQlUri(),
                    mConfiguration.getUserToken());
            mExecutor.submit(transaction);
        }
    }

    private void performRestTransaction(@NonNull final RestTransaction.Builder builder,
            @NonNull final HyperwalletListener listener) {

        if (mConfiguration == null || mConfiguration.isStale()) {
            mHyperwalletAuthenticationTokenProvider.retrieveAuthenticationToken(
                    new HyperwalletAuthenticationTokenListener() {
                        @Override
                        public void onSuccess(String authenticationToken) {
                            try {
                                mConfiguration = new Configuration(authenticationToken);
                                RestTransaction restTransaction = builder.build(mConfiguration.getRestUri(),
                                        mConfiguration.getAuthenticationToken(), mConfiguration.getUserToken());
                                mExecutor.submit(restTransaction);
                            } catch (final JSONException e) {
                                if (listener.getHandler() == null) {
                                    listener.onFailure(ExceptionMapper.toHyperwalletException(e));
                                } else {
                                    listener.getHandler().post(new Runnable() {
                                        @Override
                                        public void run() {
                                            listener.onFailure(ExceptionMapper.toHyperwalletException(e));
                                        }
                                    });
                                }
                            }
                        }

                        @Override
                        public void onFailure(UUID uuid, String message) {
                            final String logMessage = MessageFormat
                                    .format("Integrator was unable to provide an authentication token. \nId: {0} "
                                                    + "Message: {1}",
                                            uuid.toString(), message);

                            if (listener.getHandler() == null) {
                                listener.onFailure(ExceptionMapper.toHyperwalletException(
                                        new AuthenticationTokenProviderException(logMessage)));
                            } else {
                                listener.getHandler().post(new Runnable() {
                                    @Override
                                    public void run() {
                                        listener.onFailure(ExceptionMapper.toHyperwalletException(
                                                new AuthenticationTokenProviderException(logMessage)));
                                    }
                                });
                            }
                        }
                    });
        } else {
            try {
                RestTransaction restTransaction = builder.build(mConfiguration.getRestUri(),
                        mConfiguration.getAuthenticationToken(), mConfiguration.getUserToken());
                mExecutor.submit(restTransaction);
            } catch (final JSONException e) {

                if (listener.getHandler() == null) {
                    listener.onFailure(ExceptionMapper.toHyperwalletException(e));
                } else {
                    listener.getHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            listener.onFailure(ExceptionMapper.toHyperwalletException(e));
                        }
                    });
                }
            }
        }
    }

    @NonNull
    @SuppressWarnings("unchecked")
    private Map<String, String> buildUrlQueryIfRequired(@Nullable QueryParam queryParam) {
        Map<String, String> queryMap;
        if (queryParam == null) {
            queryMap = new HashMap<>();
        } else {
            queryMap = queryParam.buildQuery();
        }
        return queryMap;
    }
}

