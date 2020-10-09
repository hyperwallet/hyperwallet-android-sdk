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

import static com.hyperwallet.android.model.StatusTransition.StatusDefinition.DE_ACTIVATED;
import static com.hyperwallet.android.model.StatusTransition.StatusDefinition.SCHEDULED;
import static com.hyperwallet.android.util.HttpMethod.GET;
import static com.hyperwallet.android.util.HttpMethod.POST;
import static com.hyperwallet.android.util.HttpMethod.PUT;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hyperwallet.android.exception.HyperwalletAuthenticationTokenProviderException;
import com.hyperwallet.android.exception.HyperwalletException;
import com.hyperwallet.android.exception.HyperwalletInitializationException;
import com.hyperwallet.android.listener.HyperwalletListener;
import com.hyperwallet.android.model.QueryParam;
import com.hyperwallet.android.model.StatusTransition;
import com.hyperwallet.android.model.TypeReference;
import com.hyperwallet.android.model.balance.PrepaidCardBalanceQueryParam;
import com.hyperwallet.android.model.graphql.HyperwalletTransferMethodConfigurationField;
import com.hyperwallet.android.model.graphql.HyperwalletTransferMethodConfigurationKey;
import com.hyperwallet.android.model.graphql.field.TransferMethodConfigurationFieldResult;
import com.hyperwallet.android.model.graphql.keyed.TransferMethodConfigurationKeyResult;
import com.hyperwallet.android.model.graphql.query.TransferMethodConfigurationFieldQuery;
import com.hyperwallet.android.model.graphql.query.TransferMethodConfigurationKeysQuery;
import com.hyperwallet.android.model.paging.PageList;
import com.hyperwallet.android.model.receipt.Receipt;
import com.hyperwallet.android.model.receipt.ReceiptQueryParam;
import com.hyperwallet.android.model.transfer.Transfer;
import com.hyperwallet.android.model.transfer.TransferQueryParam;
import com.hyperwallet.android.model.balance.Balance;
import com.hyperwallet.android.model.balance.BalanceQueryParam;
import com.hyperwallet.android.model.transfermethod.BankAccount;
import com.hyperwallet.android.model.transfermethod.BankAccountQueryParam;
import com.hyperwallet.android.model.transfermethod.BankCard;
import com.hyperwallet.android.model.transfermethod.BankCardQueryParam;
import com.hyperwallet.android.model.transfermethod.PayPalAccount;
import com.hyperwallet.android.model.transfermethod.PayPalAccountQueryParam;
import com.hyperwallet.android.model.transfermethod.PrepaidCard;
import com.hyperwallet.android.model.transfermethod.PrepaidCardQueryParam;
import com.hyperwallet.android.model.transfermethod.TransferMethod;
import com.hyperwallet.android.model.transfermethod.TransferMethodQueryParam;
import com.hyperwallet.android.model.transfermethod.VenmoAccount;
import com.hyperwallet.android.model.transfermethod.VenmoAccountQueryParam;
import com.hyperwallet.android.model.user.User;

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
    public static synchronized Hyperwallet getInstance(
            @NonNull final HyperwalletAuthenticationTokenProvider hyperwalletAuthenticationTokenProvider) {
        if (sInstanceLast == null) {
            sInstanceLast = new Hyperwallet(hyperwalletAuthenticationTokenProvider);
        }
        return sInstanceLast;
    }

    /**
     * Creates a new instance of the Hyperwallet Core SDK interface object. If a previously created instance exists,
     * it will be replaced. In addition to {@link Hyperwallet#getInstance(HyperwalletAuthenticationTokenProvider)},
     * is a listener object {@link HyperwalletListener<Configuration>} this means that this method will eagerly
     * authenticate.
     * if it does not fit your use case please use the former.
     *
     * Moreover a callback is provided to listen if authentication is successful or not,
     * if successful, a {@link Configuration} object is passed over the listener through
     * {@link HyperwalletListener#onSuccess(Object)};
     * otherwise {@link HyperwalletListener#onFailure(HyperwalletException)} is invoked with details on the error.
     *
     * @param hyperwalletAuthenticationTokenProvider a provider of Hyperwallet authentication tokens; must not be null
     * @param listener                               the callback handler of responses from the Hyperwallet platform;
     *                                               must not be null
     */
    public static synchronized Hyperwallet getInstance(
            @NonNull final HyperwalletAuthenticationTokenProvider hyperwalletAuthenticationTokenProvider,
            @NonNull final HyperwalletListener<Configuration> listener) {
        if (sInstanceLast == null) {
            sInstanceLast = new Hyperwallet(hyperwalletAuthenticationTokenProvider);
            sInstanceLast.getConfiguration(listener);
        }
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

    /**
     * Resets class {@link Hyperwallet} instance reference to self
     */
    public static void clearInstance() {
        sInstanceLast = null;
    }

    /**
     * Retrieves the Configuration based on the values from the Authentication Token Provider. Please be aware that this
     * method will also authenticate, if for instance there's a previous authentication that is still valid then the
     * valid
     * {@link Configuration} will be provided in {@link HyperwalletListener#onSuccess(Object)}
     *
     * @param listener the callback handler of responses from the Hyperwallet platform; must not be null
     */
    public synchronized void getConfiguration(@NonNull final HyperwalletListener<Configuration> listener) {
        if (mConfiguration == null || mConfiguration.isStale()) {
            mHyperwalletAuthenticationTokenProvider.retrieveAuthenticationToken(
                    new HyperwalletAuthenticationTokenListener() {
                        @Override
                        public void onSuccess(String authenticationToken) {
                            try {
                                mConfiguration = new Configuration(authenticationToken);
                                listener.onSuccess(mConfiguration);
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
                                        new HyperwalletAuthenticationTokenProviderException(logMessage)));
                            } else {
                                listener.getHandler().post(new Runnable() {
                                    @Override
                                    public void run() {
                                        listener.onFailure(ExceptionMapper.toHyperwalletException(
                                                new HyperwalletAuthenticationTokenProviderException(logMessage)));
                                    }
                                });
                            }
                        }
                    });
        } else {
            listener.onSuccess(mConfiguration);
        }
    }

    /**
     * Creates a {@link BankAccount} for the User associated with the authentication token returned from
     * {@link HyperwalletAuthenticationTokenProvider#retrieveAuthenticationToken(HyperwalletAuthenticationTokenListener)}.
     *
     * <p>The {@link HyperwalletListener} that is passed in to this method invocation will receive the responses from
     * processing the request.</p>
     *
     * <p>This function will request a new authentication token via {@link HyperwalletAuthenticationTokenProvider}
     * if the current one is expired or about to expire.</p>
     *
     * @param bankAccount the {@code BankAccount} to be created; must not be null
     * @param listener    the callback handler of responses from the Hyperwallet platform; must not be null
     */
    public void createBankAccount(@NonNull final BankAccount bankAccount,
            @NonNull final HyperwalletListener<BankAccount> listener) {
        PathFormatter pathFormatter = new PathFormatter("users/{0}/bank-accounts");

        RestTransaction.Builder builder = new RestTransaction.Builder<>(POST, pathFormatter,
                new TypeReference<BankAccount>() {
                }, listener).jsonModel(bankAccount);

        performRestTransaction(builder, listener);
    }

    /**
     * Returns the list of {@link BankAccount}s for the User associated with the authentication token
     * returned from
     * {@link HyperwalletAuthenticationTokenProvider#retrieveAuthenticationToken(HyperwalletAuthenticationTokenListener)},
     * or an empty {@code List} if non exist.
     *
     * <p>The ordering and filtering of {@code BankAccounts} will be based on the criteria specified within
     * the {@link BankAccountQueryParam} object, if it is not null. Otherwise the default ordering and
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
    public void listBankAccounts(@Nullable final BankAccountQueryParam queryParam,
            @NonNull final HyperwalletListener<PageList<BankAccount>> listener) {
        Map<String, String> urlQuery = buildUrlQueryIfRequired(queryParam);
        PathFormatter pathFormatter = new PathFormatter("users/{0}/bank-accounts");

        RestTransaction.Builder builder = new RestTransaction.Builder<>(GET, pathFormatter,
                new TypeReference<PageList<BankAccount>>() {
                }, listener).query(urlQuery);

        performRestTransaction(builder, listener);
    }

    /**
     * Creates a {@link BankCard} for the User associated with the authentication token returned from
     * {@link HyperwalletAuthenticationTokenProvider#retrieveAuthenticationToken(HyperwalletAuthenticationTokenListener)}.
     *
     * <p>The {@link HyperwalletListener} that is passed in to this method invocation will receive the responses from
     * * processing the request.</p>
     *
     * <p>This function will request a new authentication token via {@link HyperwalletAuthenticationTokenProvider}
     * if the current one is expired or about to expire.</p>
     *
     * @param bankCard the {@code BankCard} to be created; must not be null
     * @param listener the callback handler of responses from the Hyperwallet platform; must not be null
     */
    public void createBankCard(@NonNull final BankCard bankCard,
            @NonNull final HyperwalletListener<BankCard> listener) {
        PathFormatter pathFormatter = new PathFormatter("users/{0}/bank-cards");

        RestTransaction.Builder builder = new RestTransaction.Builder<>(POST, pathFormatter,
                new TypeReference<BankCard>() {
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
     * Creates a {@link VenmoAccount} for the User associated with the authentication token returned from
     * {@link HyperwalletAuthenticationTokenProvider#retrieveAuthenticationToken(HyperwalletAuthenticationTokenListener)}.
     *
     * <p>The {@link HyperwalletListener} that is passed in to this method invocation will receive the responses from
     * processing the request.</p>
     *
     * <p>This function will request a new authentication token via {@link HyperwalletAuthenticationTokenProvider}
     * if the current one is expired or about to expire.</p>
     *
     * @param venmoAccount the {@code VenmoAccount} to be created; must not be null
     * @param listener     the callback handler of responses from the Hyperwallet platform; must not be null
     */
    public void createVenmoAccount(@NonNull final VenmoAccount venmoAccount,
            @NonNull final HyperwalletListener<VenmoAccount> listener) {
        PathFormatter pathFormatter = new PathFormatter("users/{0}/venmo-accounts");

        RestTransaction.Builder builder = new RestTransaction.Builder<>(POST, pathFormatter,
                new TypeReference<VenmoAccount>() {
                }, listener).jsonModel(venmoAccount);

        performRestTransaction(builder, listener);
    }

    /**
     * Creates a {@link Transfer} for the User associated with the authentication token returned from
     * {@link HyperwalletAuthenticationTokenProvider#retrieveAuthenticationToken(HyperwalletAuthenticationTokenListener)}.
     *
     * <p>The {@link HyperwalletListener} that is passed in to this method invocation will receive the responses from
     * processing the request.</p>
     *
     * <p>This function will request a new authentication token via {@link HyperwalletAuthenticationTokenProvider}
     * if the current one is expired or about to expire.</p>
     *
     * @param transfer the {@code Transfer} to be created; must not be null
     * @param listener the callback handler of responses from the Hyperwallet platform; must not be null
     */
    public void createTransfer(@NonNull final Transfer transfer,
            @NonNull final HyperwalletListener<Transfer> listener) {
        PathFormatter pathFormatter = new PathFormatter("transfers");

        RestTransaction.Builder builder = new RestTransaction.Builder<>(POST, pathFormatter,
                new TypeReference<Transfer>() {
                }, listener).jsonModel(transfer);

        performRestTransaction(builder, listener);
    }

    /**
     * Returns the {@link BankAccount} linked to the transfer method token specified, or null if none exists.
     *
     * <p>The {@link HyperwalletListener} that is passed in to this method invocation will receive the responses from
     * processing the request.</p>
     *
     * <p>This function will request a new authentication token via {@link HyperwalletAuthenticationTokenProvider}
     * if the current one is expired or about to expire.</p>
     *
     * @param transferMethodToken the Hyperwallet specific unique identifier for the {@code BankAccount}
     *                            being requested; must not be null
     * @param listener            the callback handler of responses from the Hyperwallet platform; must not be null
     */
    public void getBankAccount(@NonNull final String transferMethodToken,
            @NonNull final HyperwalletListener<BankAccount> listener) {
        PathFormatter pathFormatter = new PathFormatter("users/{0}/bank-accounts/{1}", transferMethodToken);

        RestTransaction.Builder builder = new RestTransaction.Builder<>(GET, pathFormatter,
                new TypeReference<BankAccount>() {
                }, listener);

        performRestTransaction(builder, listener);
    }

    /**
     * Returns the {@link BankCard} linked to the transfer method token specified, or null if none exists.
     *
     * <p>The {@link HyperwalletListener} that is passed in to this method invocation will receive the responses from
     * processing the request.</p>
     *
     * <p>This function will request a new authentication token via {@link HyperwalletAuthenticationTokenProvider}
     * if the current one is expired or about to expire.</p>
     *
     * @param transferMethodToken the Hyperwallet specific unique identifier for the {@code BankCard}
     *                            being requested; must not be null
     * @param listener            the callback handler of responses from the Hyperwallet platform; must not be null
     */
    public void getBankCard(@NonNull final String transferMethodToken,
            @NonNull final HyperwalletListener<BankCard> listener) {
        PathFormatter pathFormatter = new PathFormatter("users/{0}/bank-cards/{1}", transferMethodToken);

        RestTransaction.Builder builder = new RestTransaction.Builder<>(GET, pathFormatter,
                new TypeReference<BankCard>() {
                }, listener);
        performRestTransaction(builder, listener);
    }

    /**
     * Returns the {@link User} linked to the token specified, or null if none exists.
     *
     * <p>The {@link HyperwalletListener} that is passed in to this method invocation will receive the responses from
     * processing the request.</p>
     *
     * <p>This function will request a new authentication token via {@link HyperwalletAuthenticationTokenProvider}
     * if the current one is expired or about to expire.</p>
     *
     * @param listener the callback handler of responses from the Hyperwallet platform; must not be null
     */
    public void getUser(@NonNull final HyperwalletListener<User> listener) {
        PathFormatter pathFormatter = new PathFormatter("users/{0}");

        RestTransaction.Builder builder = new RestTransaction.Builder<>(GET, pathFormatter,
                new TypeReference<User>() {
                }, listener);

        performRestTransaction(builder, listener);
    }

    /**
     * Returns the {@link Transfer} linked to the specified token, or null if none exists.
     *
     * <p>The {@link HyperwalletListener} that is passed in to this method invocation will receive the responses from
     * processing the request.</p>
     *
     * <p>This function will request a new authentication token via {@link HyperwalletAuthenticationTokenProvider}
     * if the current one is expired or about to expire.</p>
     *
     * @param transferToken the Hyperwallet specific unique identifier for the {@code Transfer}
     *                      being requested; must not be null
     * @param listener      the callback handler of responses from the Hyperwallet platform; must not be null
     */
    public void getTransfer(@NonNull final String transferToken,
            @NonNull final HyperwalletListener<Transfer> listener) {
        PathFormatter pathFormatter = new PathFormatter("transfers/{1}", transferToken);

        RestTransaction.Builder builder = new RestTransaction.Builder<>(GET, pathFormatter,
                new TypeReference<Transfer>() {
                }, listener);

        performRestTransaction(builder, listener);
    }

    /**
     * Updates the {@link BankAccount} for the User associated with the authentication token returned from
     * {@link HyperwalletAuthenticationTokenProvider#retrieveAuthenticationToken(HyperwalletAuthenticationTokenListener)}.
     *
     * <p>To identify the {@code BankAccount} that is going to be updated, the transfer method token must be
     * set as part of the {@code BankAccount} object passed in.</p>
     *
     * <p>The {@link HyperwalletListener} that is passed in to this method invocation will receive the responses from
     * processing the request.</p>
     *
     * <p>This function will request a new authentication token via {@link HyperwalletAuthenticationTokenProvider}
     * if the current one is expired or about to expire.</p>
     *
     * @param bankAccount the {@code BankAccount} to be created; must not be null
     * @param listener    the callback handler of responses from the Hyperwallet platform; must not be null
     */
    public void updateBankAccount(@NonNull final BankAccount bankAccount,
            @NonNull final HyperwalletListener<BankAccount> listener) {
        PathFormatter pathFormatter = new PathFormatter("users/{0}/bank-accounts/{1}",
                bankAccount.getField(TransferMethod.TransferMethodFields.TOKEN));

        RestTransaction.Builder builder = new RestTransaction.Builder<>(PUT, pathFormatter,
                new TypeReference<BankAccount>() {
                }, listener).jsonModel(bankAccount);

        performRestTransaction(builder, listener);
    }

    /**
     * Updates the {@link BankCard} for the User associated with the authentication token returned from
     * {@link HyperwalletAuthenticationTokenProvider#retrieveAuthenticationToken(HyperwalletAuthenticationTokenListener)}.
     *
     * <p>To identify the {@code BankCard} that is going to be updated, the transfer method token must be
     * set as part of the
     * {@code BankCard} object passed in.</p>
     *
     * <p>The {@link HyperwalletListener} that is passed in to this method invocation will receive the responses from
     * processing the request.</p>
     *
     * <p>This function will request a new authentication token via {@link HyperwalletAuthenticationTokenProvider}
     * if the current one is expired or about to expire.</p>
     *
     * @param bankCard the {@code BankCard} to be created; must not be null
     * @param listener the callback handler of responses from the Hyperwallet platform; must not be null
     */
    public void updateBankCard(@NonNull final BankCard bankCard,
            @NonNull final HyperwalletListener<BankCard> listener) {
        PathFormatter pathFormatter = new PathFormatter("users/{0}/bank-cards/{1}",
                bankCard.getField(TransferMethod.TransferMethodFields.TOKEN));

        RestTransaction.Builder builder = new RestTransaction.Builder<>(PUT, pathFormatter,
                new TypeReference<BankCard>() {
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
                payPalAccount.getField(TransferMethod.TransferMethodFields.TOKEN));

        RestTransaction.Builder builder = new RestTransaction.Builder<>(PUT, pathFormatter,
                new TypeReference<PayPalAccount>() {
                }, listener).jsonModel(payPalAccount);

        performRestTransaction(builder, listener);
    }

    /**
     * Updates the {@link VenmoAccount} for the User associated with the authentication token returned from
     * {@link HyperwalletAuthenticationTokenProvider#retrieveAuthenticationToken(HyperwalletAuthenticationTokenListener)}.
     *
     * <p>To identify the {@code VenmoAccount} that is going to be updated, the transfer method token must be
     * set as part of the {@code VenmoAccount} object passed in.</p>
     *
     * <p>The {@link HyperwalletListener} that is passed in to this method invocation will receive the responses from
     * processing the request.</p>
     *
     * <p>This function will request a new authentication token via {@link HyperwalletAuthenticationTokenProvider}
     * if the current one is expired or about to expire.</p>
     *
     * @param venmoAccount the {@code VenmoAccount} to be created; must not be null
     * @param listener     the callback handler of responses from the Hyperwallet platform; must not be null
     */
    public void updateVenmoAccount(@NonNull final VenmoAccount venmoAccount,
            @NonNull final HyperwalletListener<VenmoAccount> listener) {
        PathFormatter pathFormatter = new PathFormatter("users/{0}/venmo-accounts/{1}",
                venmoAccount.getField(TransferMethod.TransferMethodFields.TOKEN));

        RestTransaction.Builder builder = new RestTransaction.Builder<>(PUT, pathFormatter,
                new TypeReference<VenmoAccount>() {
                }, listener).jsonModel(venmoAccount);

        performRestTransaction(builder, listener);
    }

    /**
     * Deactivates the {@link BankAccount} linked to the transfer method token specified. The
     * {@code BankAccount} being deactivated must belong to the User that is associated with the
     * authentication token returned from
     * {@link HyperwalletAuthenticationTokenProvider#retrieveAuthenticationToken(HyperwalletAuthenticationTokenListener)}.
     *
     * <p>The {@link HyperwalletListener} that is passed in to this method invocation will receive the responses from
     * processing the request.</p>
     *
     * <p>This function will request a new authentication token via {@link HyperwalletAuthenticationTokenProvider}
     * if the current one is expired or about to expire.</p>
     *
     * @param transferMethodToken the Hyperwallet specific unique identifier for the {@code BankAccount}
     *                            being deactivated; must not be null
     * @param notes               a note regarding the status change
     * @param listener            the callback handler of responses from the Hyperwallet platform; must not be null
     */
    public void deactivateBankAccount(@NonNull final String transferMethodToken, @Nullable final String notes,
            @NonNull final HyperwalletListener<StatusTransition> listener) {
        PathFormatter pathFormatter = new PathFormatter("users/{0}/bank-accounts/{1}/status-transitions",
                transferMethodToken);

        final StatusTransition deactivatedStatusTransition = new StatusTransition.Builder()
                .transition(DE_ACTIVATED)
                .notes(notes)
                .build();
        RestTransaction.Builder builder = new RestTransaction.Builder<>(POST, pathFormatter,
                new TypeReference<StatusTransition>() {
                }, listener).jsonModel(deactivatedStatusTransition);

        performRestTransaction(builder, listener);
    }

    /**
     * Deactivates the {@link BankCard} linked to the transfer method token specified. The
     * {@code BankCard} being deactivated must\ belong to the User that is associated with the
     * authentication token returned from
     * {@link HyperwalletAuthenticationTokenProvider#retrieveAuthenticationToken(HyperwalletAuthenticationTokenListener)}.
     *
     * <p>The {@link HyperwalletListener} that is passed in to this method invocation will receive the responses from
     * processing the request.</p>
     *
     * <p>This function will request a new authentication token via {@link HyperwalletAuthenticationTokenProvider}
     * if the current one is expired or about to expire.</p>
     *
     * @param transferMethodToken the Hyperwallet specific unique identifier for the {@code BankCard} being
     *                            deactivated; must not be null
     * @param notes               a note regarding the status change
     * @param listener            the callback handler of responses from the Hyperwallet platform; must not be null
     */
    public void deactivateBankCard(@NonNull final String transferMethodToken, @Nullable final String notes,
            @NonNull final HyperwalletListener<StatusTransition> listener) {
        PathFormatter pathFormatter = new PathFormatter("users/{0}/bank-cards/{1}/status-transitions",
                transferMethodToken);

        final StatusTransition deactivatedStatusTransition = new StatusTransition.Builder()
                .transition(DE_ACTIVATED)
                .notes(notes)
                .build();
        RestTransaction.Builder builder = new RestTransaction.Builder<>(POST, pathFormatter,
                new TypeReference<StatusTransition>() {
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
            @NonNull final HyperwalletListener<StatusTransition> listener) {
        PathFormatter pathFormatter = new PathFormatter("users/{0}/paypal-accounts/{1}/status-transitions",
                transferMethodToken);

        final StatusTransition deactivatedStatusTransition = new StatusTransition.Builder()
                .transition(DE_ACTIVATED)
                .notes(notes)
                .build();
        RestTransaction.Builder builder = new RestTransaction.Builder<>(POST, pathFormatter,
                new TypeReference<StatusTransition>() {
                }, listener).jsonModel(deactivatedStatusTransition);

        performRestTransaction(builder, listener);
    }

    /**
     * Deactivates the {@link VenmoAccount} linked to the transfer method token specified. The
     * {@code VenmoAccount} being deactivated must belong to the User that is associated with the
     * authentication token returned from
     * {@link HyperwalletAuthenticationTokenProvider#retrieveAuthenticationToken(HyperwalletAuthenticationTokenListener)}.
     *
     * <p>The {@link HyperwalletListener} that is passed in to this method invocation will receive the responses from
     * processing the request.</p>
     *
     * <p>This function will request a new authentication token via {@link HyperwalletAuthenticationTokenProvider}
     * if the current one is expired or about to expire.</p>
     *
     * @param transferMethodToken the Hyperwallet specific unique identifier for the {@code VenmoAccount}
     *                            being deactivated; must not be null
     * @param notes               a note regarding the status change
     * @param listener            the callback handler of responses from the Hyperwallet platform; must not be null
     */
    public void deactivateVenmoAccount(@NonNull final String transferMethodToken, @Nullable final String notes,
            @NonNull final HyperwalletListener<StatusTransition> listener) {
        PathFormatter pathFormatter = new PathFormatter("users/{0}/venmo-accounts/{1}/status-transitions",
                transferMethodToken);

        final StatusTransition deactivatedStatusTransition = new StatusTransition.Builder()
                .transition(DE_ACTIVATED)
                .notes(notes)
                .build();
        RestTransaction.Builder builder = new RestTransaction.Builder<>(POST, pathFormatter,
                new TypeReference<StatusTransition>() {
                }, listener).jsonModel(deactivatedStatusTransition);

        performRestTransaction(builder, listener);
    }

    /**
     * Returns the {@link TransferMethod} (Bank Account, Bank Card, PayPay Account, Prepaid Card,
     * Paper Checks) for the User associated with the authentication token returned from
     * {@link HyperwalletAuthenticationTokenProvider#retrieveAuthenticationToken(HyperwalletAuthenticationTokenListener)},
     * or an empty {@code List} if non exist.
     *
     * <p>The ordering and filtering of {@code TransferMethod}s will be based on the criteria specified
     * within the {@link TransferMethodQueryParam} object, if it is not null. Otherwise the default
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
    public void listTransferMethods(@Nullable final TransferMethodQueryParam queryParam,
            @NonNull final HyperwalletListener<PageList<TransferMethod>> listener) {
        Map<String, String> urlQuery = buildUrlQueryIfRequired(queryParam);
        PathFormatter pathFormatter = new PathFormatter("users/{0}/transfer-methods");

        RestTransaction.Builder builder = new RestTransaction.Builder<>(GET, pathFormatter,
                new TypeReference<PageList<TransferMethod>>() {
                }, listener).query(urlQuery);

        performRestTransaction(builder, listener);
    }

    /**
     * Returns the {@link BankCard} for the User associated with the authentication token returned from
     * {@link HyperwalletAuthenticationTokenProvider#retrieveAuthenticationToken(HyperwalletAuthenticationTokenListener)},
     * or an empty {@code List} if non exist.
     *
     * <p>The ordering and filtering of {@code BankCard} will be based on the criteria specified within the
     * {@link BankAccountQueryParam} object, if it is not null. Otherwise the default ordering and
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
    public void listBankCards(@Nullable final BankCardQueryParam queryParam,
            @NonNull final HyperwalletListener<PageList<BankCard>> listener) {
        Map<String, String> urlQuery = buildUrlQueryIfRequired(queryParam);
        PathFormatter pathFormatter = new PathFormatter("users/{0}/bank-cards");
        RestTransaction.Builder builder = new RestTransaction.Builder<>(GET, pathFormatter,
                new TypeReference<PageList<BankCard>>() {
                }, listener).query(urlQuery);

        performRestTransaction(builder, listener);
    }

    /**
     * Returns the {@link PrepaidCard} for the User associated with the authentication token returned from
     * {@link HyperwalletAuthenticationTokenProvider#retrieveAuthenticationToken(HyperwalletAuthenticationTokenListener)},
     * or an empty {@code List} if non exist.
     *
     * <p>The ordering and filtering of {@code PrepaidCard} will be based on the criteria specified within
     * the
     * {@link PrepaidCardQueryParam} object, if it is not null. Otherwise the default ordering and
     * filtering will be applied.</p>
     *
     * <ul>
     * <li>Offset: 0</li>
     * <li>Limit: 10</li>
     * <li>Created Before: N/A</li>
     * <li>Created After: N/A</li>
     * <li>Type: Prepaid Card</li>
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
    public void listPrepaidCards(@Nullable final PrepaidCardQueryParam queryParam,
            @NonNull final HyperwalletListener<PageList<PrepaidCard>> listener) {
        Map<String, String> urlQuery = buildUrlQueryIfRequired(queryParam);
        PathFormatter pathFormatter = new PathFormatter("users/{0}/prepaid-cards");
        RestTransaction.Builder builder = new RestTransaction.Builder<>(GET, pathFormatter,
                new TypeReference<PageList<PrepaidCard>>() {
                }, listener).query(urlQuery);

        performRestTransaction(builder, listener);
    }

    /**
     * Returns the {@link Balance} for the User associated with the authentication token returned from
     * {@link HyperwalletAuthenticationTokenProvider#retrieveAuthenticationToken(HyperwalletAuthenticationTokenListener)},
     * or an empty {@code List} if non exist.
     *
     * <p>The ordering and filtering of {@code Balance} will be based on the criteria specified within
     * the
     * {@link BalanceQueryParam} object, if it is not null. Otherwise the default ordering and
     * filtering will be applied.</p>
     *
     * <ul>
     * <li>Offset: 0</li>
     * <li>Limit: 10</li>
     * <li>Currency: N/A</li>
     * <li>Sort By: Currency</li>
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
    public void listUserBalances(@Nullable final BalanceQueryParam queryParam,
            @NonNull final HyperwalletListener<PageList<Balance>> listener) {
        Map<String, String> urlQuery = buildUrlQueryIfRequired(queryParam);
        PathFormatter pathFormatter = new PathFormatter("users/{0}/balances");
        RestTransaction.Builder builder = new RestTransaction.Builder<>(GET, pathFormatter,
                new TypeReference<PageList<Balance>>() {
                }, listener).query(urlQuery);

        performRestTransaction(builder, listener);
    }

    /**
     * Returns the list of prepaid card {@link Balance}s for the User associated with the authentication token
     * returned from
     * {@link HyperwalletAuthenticationTokenProvider#retrieveAuthenticationToken(HyperwalletAuthenticationTokenListener)},
     * or an empty {@code List} if non exist.
     *
     * <p>The ordering of {@code Balance}s will be based on the criteria specified within
     * the {@link PrepaidCardBalanceQueryParam} object, if it is not null.
     *
     * <p>The {@link HyperwalletListener} that is passed in to this method invocation will receive the responses from
     * processing the request.</p>
     *
     * <p>This function will request a new authentication token via {@link HyperwalletAuthenticationTokenProvider}
     * if the current one is expired or about to expire.</p>
     *
     * @param prepaidCardToken             the token for prepaid card
     * @param prepaidCardBalanceQueryParam the ordering criteria
     * @param listener                     the callback handler of responses from the Hyperwallet platform; must not be
     *                                     null
     */
    public void listPrepaidCardBalances(@NonNull final String prepaidCardToken,
            @Nullable final PrepaidCardBalanceQueryParam prepaidCardBalanceQueryParam,
            @NonNull final HyperwalletListener<PageList<Balance>> listener) {
        Map<String, String> urlQuery = buildUrlQueryIfRequired(prepaidCardBalanceQueryParam);
        PathFormatter pathFormatter = new PathFormatter("users/{0}/prepaid-cards/{1}/balances", prepaidCardToken);

        RestTransaction.Builder builder = new RestTransaction.Builder<>(GET, pathFormatter,
                new TypeReference<PageList<Balance>>() {
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
            @NonNull final HyperwalletListener<PageList<PayPalAccount>> listener) {
        Map<String, String> urlQuery = buildUrlQueryIfRequired(queryParam);
        PathFormatter pathFormatter = new PathFormatter("users/{0}/paypal-accounts");
        RestTransaction.Builder builder = new RestTransaction.Builder<>(GET, pathFormatter,
                new TypeReference<PageList<PayPalAccount>>() {
                }, listener).query(urlQuery);

        performRestTransaction(builder, listener);
    }

    /**
     * Returns the {@link VenmoAccount} for the User associated with the authentication token returned from
     * {@link HyperwalletAuthenticationTokenProvider#retrieveAuthenticationToken(HyperwalletAuthenticationTokenListener)},
     * or an empty {@code List} if non exist.
     *
     * <p>The ordering and filtering of {@code VenmoAccounts} will be based on the criteria specified within the
     * {@link VenmoAccountQueryParam} object, if it is not null. Otherwise the default ordering and
     * filtering will be applied.</p>
     *
     * <ul>
     * <li>Offset: 0</li>
     * <li>Limit: 10</li>
     * <li>Created Before: N/A</li>
     * <li>Created After: N/A</li>
     * <li>Type: VENMO_ACCOUNT</li>
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
    public void listVenmoAccounts(
            @Nullable final VenmoAccountQueryParam queryParam,
            @NonNull final HyperwalletListener<PageList<VenmoAccount>> listener) {
        Map<String, String> urlQuery = buildUrlQueryIfRequired(queryParam);
        PathFormatter pathFormatter = new PathFormatter("users/{0}/venmo-accounts");
        RestTransaction.Builder builder = new RestTransaction.Builder<>(GET, pathFormatter,
                new TypeReference<PageList<VenmoAccount>>() {
                }, listener).query(urlQuery);

        performRestTransaction(builder, listener);
    }

    /**
     * Returns the {@link PrepaidCard} linked to the transfer method token specified, or null if none exists.
     *
     * <p>The {@link HyperwalletListener} that is passed in to this method invocation will receive the responses from
     * processing the request.</p>
     *
     * <p>This function will request a new authentication token via {@link HyperwalletAuthenticationTokenProvider}
     * if the current one is expired or about to expire.</p>
     *
     * @param transferMethodToken the Hyperwallet specific unique identifier for the {@code PrepaidCard}
     *                            being requested; must not be null
     * @param listener            the callback handler of responses from the Hyperwallet platform; must not be null
     */
    public void getPrepaidCard(@NonNull final String transferMethodToken,
            @NonNull final HyperwalletListener<PrepaidCard> listener) {
        PathFormatter pathFormatter = new PathFormatter("users/{0}/prepaid-cards/{1}", transferMethodToken);

        RestTransaction.Builder builder = new RestTransaction.Builder<>(GET, pathFormatter,
                new TypeReference<PrepaidCard>() {
                }, listener);

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
     * Returns the {@link VenmoAccount} linked to the transfer method token specified, or null if none exists.
     *
     * <p>The {@link HyperwalletListener} that is passed in to this method invocation will receive the responses from
     * processing the request.</p>
     *
     * <p>This function will request a new authentication token via {@link HyperwalletAuthenticationTokenProvider}
     * if the current one is expired or about to expire.</p>
     *
     * @param transferMethodToken the Hyperwallet specific unique identifier for the {@code VenmoAccount}
     *                            being requested; must not be null
     * @param listener            the callback handler of responses from the Hyperwallet platform; must not be null
     */

    public void getVenmoAccount(@NonNull final String transferMethodToken,
            @NonNull final HyperwalletListener<VenmoAccount> listener) {
        PathFormatter pathFormatter = new PathFormatter("users/{0}/venmo-accounts/{1}", transferMethodToken);

        RestTransaction.Builder builder = new RestTransaction.Builder<>(GET, pathFormatter,
                new TypeReference<VenmoAccount>() {
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
            @NonNull final TransferMethodConfigurationKeysQuery query,
            @NonNull final HyperwalletListener<HyperwalletTransferMethodConfigurationKey> listener) {
        GqlTransaction.Builder<TransferMethodConfigurationKeyResult> builder = new GqlTransaction.Builder<>(
                query, new TypeReference<TransferMethodConfigurationKeyResult>() {
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
            @NonNull final TransferMethodConfigurationFieldQuery query,
            @NonNull final HyperwalletListener<HyperwalletTransferMethodConfigurationField> listener) {

        GqlTransaction.Builder<TransferMethodConfigurationFieldResult> builder =
                new GqlTransaction.Builder<>(query,
                        new TypeReference<TransferMethodConfigurationFieldResult>() {
                        }, listener);
        performGqlTransaction(builder, listener);
    }


    /**
     * Returns the list of {@link Receipt}s for the User associated with the authentication token
     * returned from
     * {@link HyperwalletAuthenticationTokenProvider#retrieveAuthenticationToken(HyperwalletAuthenticationTokenListener)},
     * or an empty {@code List} if non exist.
     *
     * <p>The ordering and filtering of {@code Receipt}s will be based on the criteria specified within
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
    public void listUserReceipts(@Nullable final ReceiptQueryParam receiptQueryParam,
            @NonNull final HyperwalletListener<PageList<Receipt>> listener) {
        Map<String, String> urlQuery = buildUrlQueryIfRequired(receiptQueryParam);
        PathFormatter pathFormatter = new PathFormatter("users/{0}/receipts");

        RestTransaction.Builder builder = new RestTransaction.Builder<>(GET, pathFormatter,
                new TypeReference<PageList<Receipt>>() {
                }, listener).query(urlQuery);

        performRestTransaction(builder, listener);
    }

    /**
     * Returns the list of prepaid card {@link Receipt}s for the User associated with the authentication token
     * returned from
     * {@link HyperwalletAuthenticationTokenProvider#retrieveAuthenticationToken(HyperwalletAuthenticationTokenListener)},
     * or an empty {@code List} if non exist.
     *
     * <p>The ordering and filtering of {@code Receipt}s will be based on the criteria specified within
     * the {@link ReceiptQueryParam} object, if it is not null.  Filters that is accepted in Prepaid card
     * receipts are the following: Other filter settings will be discarded</p>
     *
     * <ul>
     * <li>Created Before: date before, based on <code>createdOn</code> </li>
     * <li>Created After: date after, based on <code>createdOn</code></li>
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
            @Nullable final ReceiptQueryParam receiptQueryParam,
            @NonNull final HyperwalletListener<PageList<Receipt>> listener) {
        Map<String, String> urlQuery = buildUrlQueryIfRequired(receiptQueryParam);
        PathFormatter pathFormatter = new PathFormatter("users/{0}/prepaid-cards/{1}/receipts", prepaidCardToken);

        RestTransaction.Builder builder = new RestTransaction.Builder<>(GET, pathFormatter,
                new TypeReference<PageList<Receipt>>() {
                }, listener).query(urlQuery);

        performRestTransaction(builder, listener);
    }

    /**
     * Returns the list of transfer {@link Transfer}s for the User associated with the authentication token
     * returned from
     * {@link HyperwalletAuthenticationTokenProvider#retrieveAuthenticationToken(HyperwalletAuthenticationTokenListener)},
     * or an empty {@code List} if non exist.
     *
     * <p>The ordering and filtering of {@code Transfer} will be based on the criteria specified within
     * the {@link TransferQueryParam} object, if it is not null.  Filters that is accepted in transfer are
     * the following: Other filter settings will be discarded</p>
     *
     * <ul>
     * <li>Offset: 0</li>
     * <li>Limit: 10</li>
     * <li>Created Before: N/A</li>
     * <li>Created After: N/A</li>
     * <li>clientTransferId: N/A</li>
     * <li>sourceToken: N/A</li>
     * <li>destinationToken: N/A</li>
     * </ul>
     *
     * <p>The {@link HyperwalletListener} that is passed in to this method invocation will receive the responses from
     * * processing the request.</p>
     *
     * <p>This function will request a new authentication token via {@link HyperwalletAuthenticationTokenProvider}
     * if the current one is expired or about to expire.</p>
     *
     * @param transferQueryParam the filtering criteria
     * @param listener           the callback handler of responses from the Hyperwallet platform; must not be null
     */
    public void listTransfers(@Nullable final TransferQueryParam transferQueryParam,
            @NonNull final HyperwalletListener<PageList<Transfer>> listener) {
        Map<String, String> urlQuery = buildUrlQueryIfRequired(transferQueryParam);
        PathFormatter pathFormatter = new PathFormatter("transfers");

        RestTransaction.Builder builder = new RestTransaction.Builder<>(GET, pathFormatter,
                new TypeReference<PageList<Transfer>>() {
                }, listener).query(urlQuery);

        performRestTransaction(builder, listener);
    }

    /**
     * Commit the transfer recently created, identified by the transfer token, referenced from create transfer response
     *
     * @param transferToken transfer token generated when transfer is created
     * @param notes         additional information for committing transfer
     * @param listener      the callback handler of responses from the Hyperwallet platform; must not be null
     */
    public void scheduleTransfer(@NonNull final String transferToken, @Nullable final String notes,
            @NonNull final HyperwalletListener<StatusTransition> listener) {
        PathFormatter pathFormatter = new PathFormatter("transfers/{1}/status-transitions",
                transferToken);

        final StatusTransition statusTransition = new StatusTransition.Builder()
                .transition(SCHEDULED)
                .notes(notes)
                .build();

        RestTransaction.Builder builder = new RestTransaction.Builder<>(POST, pathFormatter,
                new TypeReference<StatusTransition>() {
                }, listener).jsonModel(statusTransition);

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
                                        builder.build(mConfiguration.getGraphQlUri(), mConfiguration.getUserToken(),
                                                mConfiguration.getAuthenticationToken());
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
                                        .toHyperwalletException(
                                                new HyperwalletAuthenticationTokenProviderException(logMessage)));
                            } else {
                                listener.getHandler().post(new Runnable() {
                                    @Override
                                    public void run() {
                                        listener.onFailure(ExceptionMapper
                                                .toHyperwalletException(
                                                        new HyperwalletAuthenticationTokenProviderException(
                                                                logMessage)));
                                    }
                                });
                            }
                        }
                    });
        } else {
            GqlTransaction transaction = builder.build(mConfiguration.getGraphQlUri(),
                    mConfiguration.getUserToken(), mConfiguration.getAuthenticationToken());
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
                                        new HyperwalletAuthenticationTokenProviderException(logMessage)));
                            } else {
                                listener.getHandler().post(new Runnable() {
                                    @Override
                                    public void run() {
                                        listener.onFailure(ExceptionMapper.toHyperwalletException(
                                                new HyperwalletAuthenticationTokenProviderException(logMessage)));
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

