[![Maven Central](https://img.shields.io/maven-central/v/com.hyperwallet.android/core-sdk.svg?color=0FA81E)]()

# Hyperwallet Android Core SDK

**NOTE: This is a beta product available for use in your mobile app. If you are interested in using this product, please notify your Relationship Manager and / or Project Manager to support you during the integration process.**

Welcome to Hyperwallet's Android Core SDK. This library will help you create transfer methods in your Android app, such as bank account, PayPal account, etc.

Note that this SDK is geared towards those who only require back end data, which means you will have to build your own UI.

We also provide an out-of-the-box  [Hyperwallet Android UI SDK](https://github.com/hyperwallet/hyperwallet-android-ui-sdk) for you if you decide not to build your own UI.

## Prerequisites
* A Hyperwallet merchant account
* Set Up your server to manage the user's authentication process on the Hyperwallet platform
* An Android IDE
* Android version >= 21

## Installation

To install Hyperwallet Core SDK, you just need to add the dependency into your build.gradle file in Android Studio (or Gradle). For example:

```bash
api 'com.hyperwallet.android:core-sdk:1.0.0-beta09'
```

### Proguard

When enabling Proguard, please add a rule in `proguard-rules.pro` file in your main app

```properties
-keep public class com.hyperwallet.android.** { *; }
```

## Initialization

After you're done installing the SDK, you need to initialize an instance in order to utilize core SDK functions. Also you need to provide a [HyperwalletAuthenticationTokenProvider](#Authentication) object to retrieve an authentication token.

```
// initialize core SDK
Hyperwallet.getInstance(hyperwalletAuthenticationTokenProvider)

// use core SDK functions
Hyperwallet.getDefault().createBankAccount(bankAccount, listener);
```

## Authentication
First of all, your server side should be able to send a POST request to Hyperwallet endpoint via Basic Authentication to retrieve an [authentication token](https://jwt.io/). For example:

```
curl -X "POST" "https://localhost:8181/rest/v3/users/{user-token}/authentication-token" \
-u userName:password \
-H "Content-type: application/json" \
-H "Accept: application/json"
```

Then, you need to provide a class (an authentication provider) which implements HyperwalletAuthenticationTokenProvider to retrieve an authentication token from your server.

```java

public class TestAuthenticationProvider implements HyperwalletAuthenticationTokenProvider {

    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");
    private static final String mBaseUrl = "https://your/server/to/retrieve/authenticationToken";

    @Override
    public void retrieveAuthenticationToken(final HyperwalletAuthenticationTokenListener listener) {

        OkHttpClient client = new OkHttpClient();

        String payload = "{}";
        RequestBody body = RequestBody.create(JSON, payload);
        Request request = new Request.Builder()
                .url(baseUrl)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                listener.onFailure(UUID.randomUUID(), e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                listener.onSuccess(response.body().string());
            }
        });
    }
}

```
## Usage
The functions in core SDK are available to use once the authentication is done.

### Get User
```java
Hyperwallet.getDefault().getUser(mListener);
// onSuccess: response (User) will contain information about the current user
// onFailure: error (ErrorType) will contain Errors containing information about what caused the failure

result.getFirstName();
result.getLastName();
```

### Create PayPal Account
```java
final PayPalAccount payPalAccount = new PayPalAccount.Builder()
        .transferMethodCountry("US")
        .transferMethodCurrency("USD")
        .email("user@domain.com")
        .build();
Hyperwallet.getDefault().createPayPalAccount(payPalAccount, listener);
// onSuccess: response (PayPalAccount) payload will contain information about the PayPal created
// onFailure: error (ErrorType) will contain Errors containing information about what caused the failure
```

### Get PayPal Account
```java
Hyperwallet.getDefault().getPayPalAccount("trm-12345", listener);
// onSuccess: response (PayPalAccount) will contain information about the inputted PayPal account or null if not exists
// onFailure: error (ErrorType) will contain Errors containing information about what caused the failure
```

### Update PayPal Account
```java
final PayPalAccount payPalAccount = new PayPalAccount.Builder()
        .token("trm-12345")
        .email("user2@domain.com")
        .build();
Hyperwallet.getDefault().updatePayPalAccount(payPalAccount, listener);
// onSuccess: response (PayPalAccount) will contain information about the updated PayPal account
// onFailure: error (ErrorType) will contain Errors containing information about what caused the failure

// Note: the transfer method token must be set as part of the code PayPalAccount object passed in
```

### Deactivate PayPal Account
```java
Hyperwallet.getDefault().deactivatePayPalAccount("trm-12345", "deactivate PayPal account", listener);
// onSuccess: response (StatusTransition) will contain information about the status transition
// onFailure: error (ErrorType) will contain Errors containing information about what caused the failure
```

### List PayPal Account
```java
PayPalAccountQueryParam payPalAccountQueryParam = new PayPalAccountQueryParam.Builder()
        .status(ACTIVATED)
        .sortByCreatedOnAsc()
        .build();
Hyperwallet.getDefault().listPayPalAccounts(payPalAccountQueryParam, listener);
// onSuccess: response (PageList<PayPalAccount>) will contain a PageList of PayPalAccount or null if not exists
// onFailure: error (ErrorType) will contain Errors containing information about what caused the failure
```

### Create Venmo Account
```java
final VenmoAccount venmoAccount = new VenmoAccount.Builder()
         .transferMethodCountry("US")
         .transferMethodCurrency("USD")
         .accountId("9876543210")
         .build();

Hyperwallet.getDefault().createVenmoAccount(venmoAccount, listener);
// onSuccess: response (VenmoAccount in this case) will contain information about the user’s Venmo account
// onFailure: error (ErrorType) will contain Errors containing information about what caused the failure
```

### Get Venmo Account
```java
Hyperwallet.getDefault().getVenmoAccount("trm-fake-token", listener);
// onSuccess: response (VenmoAccount in this case) will contain information about the user’s Venmo account or null if not exist.
// onFailure: error (ErrorType) will contain Errors containing information about what caused the failure
```

### Update Venmo Account
```java
final VenmoAccount venmoAccount = new VenmoAccount
         .Builder()
         .accountId("9876543211")
         .token("trm-fake-token")
         .build();

Hyperwallet.getDefault().updateVenmoAccount(venmoAccount, mListener);
// onSuccess: response (VenmoAccount in this case) will contain information about the user’s Venmo account
// onFailure: error (ErrorType) will contain Errors containing information about what caused the failure of Venmo account updating
```

### Deactivate Venmo Account
```java
Hyperwallet.getDefault().deactivateVenmoAccount("trm-fake-token", "deactivate Venmo account", mListener);
// onSuccess: response (StatusTransition in this case) will contain information about the status transition
// onFailure: error (ErrorType) will contain Errors containing information about what caused the failure of Venmo account deactivation
```

### List Venmo Account
```java
VenmoAccountQueryParam queryParam = new VenmoAccountQueryParam.Builder().status(ACTIVATED).build();
Hyperwallet.getDefault().listVenmoAccounts(queryParam, mListener);
// onSuccess: response (PageList<VenmoAccount>) will contain a PageList of VenmoAccount or null if not exists
// onFailure: error (ErrorType) will contain Errors containing information about what caused the failure
```

### Create Paper Check
```java
final PaperCheck paperCheck = new PaperCheck.Builder()
         .transferMethodCountry("US")
         .transferMethodCurrency("USD")
         .build();

Hyperwallet.getDefault().createPaperCheck(paperCheck, listener);
// onSuccess: response (PaperCheck in this case) will contain information about the Paper Check created
// onFailure: error (ErrorType) will contain Errors containing information about what caused the failure
```

### Get Paper Check
```java
Hyperwallet.getDefault().getPaperCheck("trm-fake-token", listener);
// onSuccess: response (PaperCheck in this case) will contain information about the user’s Paper Check or null if not exist.
// onFailure: error (ErrorType) will contain Errors containing information about what caused the failure
```

### Update Paper Check
```java
 final PaperCheck paperCheck = new PaperCheck
                .Builder()
                .token("trm-fake-token")
                .shippingMethod("EXPEDITED")
                .build();

Hyperwallet.getDefault().updatePaperCheck(paperCheck, listener);
// onSuccess: response (PaperCheck in this case) will contain information about the user’s PaperCheck
// onFailure: error (ErrorType) will contain Errors containing information about what caused the failure of PaperCheck updating
```

### Deactivate Paper Check
```java
Hyperwallet.getDefault().deactivatePaperCheck("trm-fake-token", "deactivate Paper Check", listener);
// onSuccess: response (StatusTransition in this case) will contain information about the status transition
// onFailure: error (ErrorType) will contain Errors containing information about what caused the failure of Paper Check deactivation
```

### List Paper Check
```java
PaperCheckQueryParam queryParam = new PaperCheckQueryParam.Builder()
                .status(ACTIVATED)
                .build();
Hyperwallet.getDefault().listPaperChecks(queryParam, listener);
// onSuccess: response (PageList<PaperCheck>) will contain a PageList of PaperCheck or null if not exists
// onFailure: error (ErrorType) will contain Errors containing information about what caused the failure
```

### Create Bank Account
```java
final BankAccount bankAccount = new BankAccount.Builder()
        .transferMethodCountry("US")
        .transferMethodCurrency("USD")
        .bankAccountId("12345")
        .branchId("123456")
        .bankAccountPurpose(BankAccount.Purpose.CHECKING)
        .build();
Hyperwallet.getDefault().createBankAccount(bankAccount, listener);
// onSuccess: response (BankAccount) payload will contain information about the account created
// onFailure: error (ErrorType) will contain Errors containing information about what caused the failure
```

### Get Bank Account
```java
Hyperwallet.getDefault().getBankAccount("trm-12345", listener);
// onSuccess: response (BankAccount) will contain information about the inputted bank account or null if not exists
// onFailure: error (ErrorType) will contain Errors containing information about what caused the failure
```

### Update Bank Account
```java
final BankAccount updatedBankAccount = new BankAccount.Builder()
        .token("trm-12345")
        .bankAccountId("67890")
        .build();
Hyperwallet.getDefault().updateBankAccount(updatedBankAccount, listener);
// onSuccess: response (BankAccount) will contain information about the updated Bank Account
// onFailure: error (ErrorType) will contain Errors containing information about what caused the failure

// Note: the transfer method token must be set as part of the BankAccount object passed in
```

### Deactivate Bank Account
```java
Hyperwallet.getDefault().deactivateBankAccount("trm-12345", "deactivate bank account", listener);
// onSuccess: response (StatusTransition) will contain information about the status transition
// onFailure: error (ErrorType) will contain Errors containing information about what caused the failure
```

### List Bank Account
```java
BankAccountQueryParam bankAccountQueryParam = new BankAccountQueryParam.Builder()
                .status(ACTIVATED)
                .sortByCreatedOnAsc()
                .build();
Hyperwallet.getDefault().listBankAccounts(bankAccountQueryParam, listener);
// onSuccess: response (PageList<BankAccount>) will contain a PageList of BankAccount or null if not exists
// onFailure: error (ErrorType) will contain Errors containing information about what caused the failure
```

### Create Bank Card
```java
final BankCard bankCard = new BankCard.Builder()
        .transferMethodCountry("US")
        .transferMethodCurrency("USD")
        .cardNumber("1234123412341234")
        .dateOfExpiry("2022-12")
        .cvv("123")
        .build();
Hyperwallet.getDefault().createBankCard(bankCard, listener);
// onSuccess: response (BankCard) payload will contain information about the Bank Card created
// onFailure: error (ErrorType) will contain Errors containing information about what caused the failure
```

### Get Bank Card
```java
Hyperwallet.getDefault().getBankCard("trm-12345", listener);
// onSuccess: response (BankCard) will contain information about the inputted bank card or null if not exists
// onFailure: error (ErrorType) will contain Errors containing information about what caused the failure
```

### Update Bank Card
```java
final BankCard updatedBankCard = new BankCard.Builder()
        .token("trm-12345")
        .dateOfExpiry("2023-12")
        .build();
Hyperwallet.getDefault().updateBankCard(updatedBankCard, listener);
// onSuccess: response (BankCard) will contain information about the updated Bank Card
// onFailure: error (ErrorType) will contain Errors containing information about what caused the failure

// Note: the transfer method token must be set as part of the BankCard object passed in
```

### Deactivate Bank Card
```java
Hyperwallet.getDefault().deactivateBankCard("trm-12345", "deactivate bank card", listener);
// onSuccess: response (StatusTransition) will contain information about the status transition
// onFailure: error (ErrorType) will contain Errors containing information about what caused the failure
```

### List Bank Card
```java
BankCardQueryParam bankCardQueryParam = new BankCardQueryParam.Builder()
        .status(ACTIVATED)
        .sortByCreatedOnAsc()
        .build();
Hyperwallet.getDefault().listBankCards(bankCardQueryParam, listener);
// onSuccess: response (PageList<BankCard>) will contain a PageList of BankCard or null if not exists
// onFailure: error (ErrorType) will contain Errors containing information about what caused the failure
```

### List Prepaid Card
```java
PrepaidCardQueryParam prepaidCardQueryParam = new PrepaidCardQueryParam.Builder()
        .status(ACTIVATED)
        .sortByCreatedOnAsc()
        .build();
Hyperwallet.getDefault().listPrepaidCards(prepaidCardQueryParam, listener);
// onSuccess: response (PageList<PrepaidCard>) will contain a PageList of PrepaidCard or null if not exists
// onFailure: error (ErrorType) will contain Errors containing information about what caused the failure
```

### List Prepaid Card Receipts
```java
Calendar calendar = Calendar.getInstance();
calendar.set(2019, 1, 1, 0, 0, 0);
calendar.set(Calendar.MILLISECOND, 0);

ReceiptQueryParam receiptQueryParam = new ReceiptQueryParam.Builder()
        .createdAfter(calendar.getTime())
        .limit(100)
        .sortByCreatedOnDesc()
        .build();
Hyperwallet.getDefault().listPrepaidCardReceipts("trm-12345", receiptQueryParam), listener);
// onSuccess: response (PageList<Receipt>) will contain a PageList of Receipts or null if not exists
// onFailure: error (ErrorType) will contain Errors containing information about what caused the failure
```

### List User Receipts
```java
Calendar calendar = Calendar.getInstance();
calendar.set(2019, 1, 1, 0,0,0);
calendar.set(Calendar.MILLISECOND, 0);

ReceiptQueryParam receiptQueryParam = new ReceiptQueryParam.Builder()
        .createdAfter(calendar.getTime())
        .limit(100)
        .sortByCreatedOnDesc()
        .build();
Hyperwallet.getDefault().listUserReceipts(receiptQueryParam, listener);
// onSuccess: response (PageList<Receipt>) will contain a PageList of Receipts or null if not exists
// onFailure: error (ErrorType) will contain Errors containing information about what caused the failure
```

### List Transfer Methods
```java
TransferMethodQueryParam transferMethodQueryParam = new TransferMethodQueryParam.Builder()
        .status(ACTIVATED)
        .limit(100)
        .sortByCreatedOnDesc()
        .build();
Hyperwallet.getDefault().listTransferMethods(transferMethodQueryParam, listener);
// onSuccess: response (PageList<TransferMethod>) will contain a PageList of Transfer Methods or null if not exists
// onFailure: error (ErrorType) will contain Errors containing information about what caused the failure
```

### List Balances
```java
BalanceQueryParam balanceQueryParam = new BalanceQueryParam.Builder()
        .currency("USD")
        .sortByCurrencyAsc()
        .build();
Hyperwallet.getDefault().listUserBalances(balanceQueryParam, listener);
// onSuccess: response (PageList<Balance>) will contain a PageList of Balance or null if not exists
// onFailure: error (ErrorType) will contain Errors containing information about what caused the failure
```

### List Prepaid Card Balances
```java
PrepaidCardBalanceQueryParam prepaidCardBalanceQueryParam = new PrepaidCardBalanceQueryParam.Builder()
        .sortByCurrencyAsc()
        .build();
Hyperwallet.getDefault().listPrepaidCardBalances("trm-12345", prepaidCardBalanceQueryParam, listener);
// onSuccess: response (PageList<Balance>) will contain a PageList of Balance or null if not exists
// onFailure: error (ErrorType) will contain Errors containing information about what caused the failure
```

### Create Transfer
```java
final Transfer transfer = new Transfer.Builder()
        .clientTransferID("1234567890123")
        .sourceToken("usr-4321")
        .destinationToken("trm-246")
        .sourceAmount("100")
        .sourceCurrency("CAD")
        .destinationAmount("62.29")
        .destinationCurrency("USD")
        .memo("TransferClientId56387")
        .notes("Partial-Balance Transfer")
        .build();
Hyperwallet.getDefault().createTransfer(transfer, listener);
// onSuccess: response (Transfer) payload will contain information about the Transfer created
// onFailure: error (ErrorType) will contain Errors containing information about what caused the failure
```

### Schedule Transfer
```java
Hyperwallet.getDefault().scheduleTransfer("trf-123456", "schedule transfer notes", listener);
// onSuccess: response (StatusTransition) will contain a StatusTransition of the transfer or null if not exists
// onFailure: error (ErrorType) will contain Errors containing information about what caused the failure
```

### Get Transfer
```java
Hyperwallet.getDefault().getTransfer("trf-123456", listener);
// onSuccess: response (Transfer) will contain information about the inputted transfer or null if not exists
// onFailure: error (ErrorType) will contain Errors containing information about what caused the failure
```

### List Transfers
```java
TransferQueryParam transferQueryParam = new TransferQueryParam.Builder()
        .limit(100)
        .build();
Hyperwallet.getDefault().listTransfers(transferQueryParam, listener);
// onSuccess: response (PageList<Transfer>) will contain a PageList of Transfers or null if not exists
// onFailure: error (ErrorType) will contain Errors containing information about what caused the failure
```

## Transfer Method Configurations

### Get countries, currencies
```java
TransferMethodConfigurationKeysQuery query = new TransferMethodConfigurationKeysQuery();
Hyperwallet.getDefault().retrieveTransferMethodConfigurationKeys(query, listener);
// onSuccess: response (TransferMethodConfigurationKey)
//         will contain a dataset of available Transfer Method Configurations for the program
// onFailure: error (ErrorType) will contain Errors containing information about what caused the failure

// Get Countries/Country
Set<Country> countrySet = result.getCountries();
Country firstCountry = countrySet.iterator().next();

// Get currencies based on first country
Set<Currency> currencySet =  result.getCurrencies(firstCountry.getCode());
Currency firstCurrency = currencySet.iterator().next();

```
### Get transfer method types, fees and processing times for Country and Currency
```java
TransferMethodConfigurationTransferTypesFeeAndProcessingTimeQuery query = new TransferMethodConfigurationTransferTypesFeeAndProcessingTimeQuery("US", "USD");
Hyperwallet.getDefault().retrieveTransferMethodTypesFeesAndProcessingTimes(query, listener);
// onSuccess: response (TransferMethodConfigurationKey)
//         will contain a dataset of available Transfer Method Configurations for the program
// onFailure: error (ErrorType) will contain Errors containing information about what caused the failure


// Get Transfer Method Types for Country/Currency combination
Set<TransferMethodType> transferMethodTypeSet =
        result.getTransferMethodType(firstCountry.getCode(), firstCurrency.getCode());

```
### Get fields for a transfer method type
```java
TransferMethodConfigurationFieldQuery fieldQuery = new TransferMethodConfigurationFieldQuery(
        "US", "USD", TransferMethod.TransferMethodTypes.BANK_ACCOUNT, "INDIVIDUAL");
Hyperwallet.getDefault().retrieveTransferMethodConfigurationFields(fieldQuery, listener);
// onSuccess: response (TransferMethodConfigurationFieldResult)
//         will contain a dataset of available Transfer Method Configuration Fields for the specified query
// onFailure: error (ErrorType) will contain Errors containing information about what caused the failure

result.getFees();
result.getProcessingTime();
result.getFields();
result.getFields().getTransferMethodType();
```
### Update fields for a transfer method type
```java
TransferMethodUpdateConfigurationFieldQuery fieldQuery = new TransferMethodUpdateConfigurationFieldQuery(
        "trmToken");
Hyperwallet.getDefault().retrieveTransferMethodUpdateConfigurationFields(fieldQuery, listener);
// onSuccess: response (TransferMethodUpdateConfigurationFieldResult)
//         will contain a dataset of available Transfer Method Configuration Fields for the specified query
// onFailure: error (ErrorType) will contain Errors containing information about what caused the failure

result.getFields();
```
## License
The Hyperwallet Android Core SDK is open source and available under the [MIT](https://github.com/hyperwallet/hyperwallet-android-sdk/blob/master/LICENSE) license.