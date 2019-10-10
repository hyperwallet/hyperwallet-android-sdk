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
api 'com.hyperwallet.android:core-sdk:1.0.0-beta04'
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
curl -X "POST" "https://api.sandbox.hyperwallet.com/rest/v3/users/{user-token}/authentication-token" \
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
// onSuccess: response (HyperwalletUser) will contain information about the current user 
// onFailure: error (HyperwalletErrorType) will contain HyperwalletErrors containing information about what caused the failure

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
// onFailure: error (HyperwalletErrorType) will contain HyperwalletErrors containing information about what caused the failure
```

### Get PayPal Account
```java
Hyperwallet.getDefault().getPayPalAccount("trm-12345", listener);
// onSuccess: response (PayPalAccount) will contain information about the inputted PayPal account or null if not exists 
// onFailure: error (HyperwalletErrorType) will contain HyperwalletErrors containing information about what caused the failure
```

### Update PayPal Account
```java
final PayPalAccount payPalAccount = new PayPalAccount.Builder()
        .token("trm-12345")
        .email("user2@domain.com")
        .build();
Hyperwallet.getDefault().updatePayPalAccount(payPalAccount, listener);
// onSuccess: response (PayPalAccount) will contain information about the updated PayPal account 
// onFailure: error (HyperwalletErrorType) will contain HyperwalletErrors containing information about what caused the failure

// Note: the transfer method token must be set as part of the code PayPalAccount object passed in 
```

### Deactivate PayPal Account
```java
Hyperwallet.getDefault().deactivatePayPalAccount("trm-12345", "deactivate PayPal account", listener);
// onSuccess: response (HyperwalletStatusTransition) will contain information about the status transition 
// onFailure: error (HyperwalletErrorType) will contain HyperwalletErrors containing information about what caused the failure
```

### List PayPal Account
```java
PayPalAccountQueryParam payPalAccountQueryParam = new PayPalAccountQueryParam.Builder()
        .status(ACTIVATED)
        .sortByCreatedOnAsc()
        .build();
Hyperwallet.getDefault().listPayPalAccounts(payPalAccountQueryParam, listener);
// onSuccess: response (HyperwalletPageList<PayPalAccount>) will contain a PageList of PayPalAccount or null if not exists 
// onFailure: error (HyperwalletErrorType) will contain HyperwalletErrors containing information about what caused the failure
```

### Create Bank Account
```java
final HyperwalletBankAccount bankAccount = new HyperwalletBankAccount.Builder()
        .transferMethodCountry("US")
        .transferMethodCurrency("USD")
        .bankAccountId("12345")
        .branchId("123456")
        .bankAccountPurpose(HyperwalletBankAccount.Purpose.CHECKING)
        .build();
Hyperwallet.getDefault().createBankAccount(bankAccount, listener);
// onSuccess: response (HyperwalletBankAccount) payload will contain information about the account created 
// onFailure: error (HyperwalletErrorType) will contain HyperwalletErrors containing information about what caused the failure
```

### Get Bank Account
```java
Hyperwallet.getDefault().getBankAccount("trm-12345", listener);
// onSuccess: response (HyperwalletBankAccount) will contain information about the inputted bank account or null if not exists 
// onFailure: error (HyperwalletErrorType) will contain HyperwalletErrors containing information about what caused the failure
```

### Update Bank Account
```java
final HyperwalletBankAccount updatedBankAccount = new HyperwalletBankAccount.Builder()
        .token("trm-12345")
        .bankAccountId("67890")
        .build();
Hyperwallet.getDefault().updateBankAccount(updatedBankAccount, listener);
// onSuccess: response (HyperwalletBankAccount) will contain information about the updated Bank Account 
// onFailure: error (HyperwalletErrorType) will contain HyperwalletErrors containing information about what caused the failure

// Note: the transfer method token must be set as part of the HyperwalletBankAccount object passed in
```

### Deactivate Bank Account
```java
Hyperwallet.getDefault().deactivateBankAccount("trm-12345", "deactivate bank account", listener);
// onSuccess: response (HyperwalletStatusTransition) will contain information about the status transition 
// onFailure: error (HyperwalletErrorType) will contain HyperwalletErrors containing information about what caused the failure
```

### List Bank Account
```java
HyperwalletBankAccountQueryParam hyperwalletBankAccountQueryParam = new HyperwalletBankAccountQueryParam.Builder()
                .status(ACTIVATED)
                .sortByCreatedOnAsc()
                .build();
Hyperwallet.getDefault().listBankAccounts(hyperwalletBankAccountQueryParam, listener);
// onSuccess: response (HyperwalletPageList<HyperwalletBankAccount>) will contain a PageList of HyperwalletBankAccount or null if not exists 
// onFailure: error (HyperwalletErrorType) will contain HyperwalletErrors containing information about what caused the failure
```

### Create Bank Card
```java
final HyperwalletBankCard bankCard = new HyperwalletBankCard.Builder()
        .transferMethodCountry("US")
        .transferMethodCurrency("USD")
        .cardNumber("1234123412341234")
        .dateOfExpiry("2022-12")
        .cvv("123")
        .build();
Hyperwallet.getDefault().createBankCard(bankCard, listener);
// onSuccess: response (HyperwalletBankCard) payload will contain information about the Bank Card created 
// onFailure: error (HyperwalletErrorType) will contain HyperwalletErrors containing information about what caused the failure
```

### Get Bank Card
```java
Hyperwallet.getDefault().getBankCard("trm-12345", listener);
// onSuccess: response (HyperwalletBankCard) will contain information about the inputted bank card or null if not exists 
// onFailure: error (HyperwalletErrorType) will contain HyperwalletErrors containing information about what caused the failure  
```

### Update Bank Card
```java
final HyperwalletBankCard updatedBankCard = new HyperwalletBankCard.Builder()
        .token("trm-12345")
        .dateOfExpiry("2023-12")
        .build();
Hyperwallet.getDefault().updateBankCard(updatedBankCard, listener);
// onSuccess: response (HyperwalletBankCard) will contain information about the updated Bank Card 
// onFailure: error (HyperwalletErrorType) will contain HyperwalletErrors containing information about what caused the failure

// Note: the transfer method token must be set as part of the HyperwalletBankCard object passed in
```

### Deactivate Bank Card
```java
Hyperwallet.getDefault().deactivateBankCard("trm-12345", "deactivate bank card", listener);
// onSuccess: response (HyperwalletStatusTransition) will contain information about the status transition 
// onFailure: error (HyperwalletErrorType) will contain HyperwalletErrors containing information about what caused the failure
```

### List Bank Card
```java
HyperwalletBankCardQueryParam bankCardQueryParam = new HyperwalletBankCardQueryParam.Builder()
        .status(ACTIVATED)
        .sortByCreatedOnAsc()
        .build();
Hyperwallet.getDefault().listBankCards(bankCardQueryParam, listener);
// onSuccess: response (HyperwalletPageList<HyperwalletBankCard>) will contain a PageList of HyperwalletBankCard or null if not exists 
// onFailure: error (HyperwalletErrorType) will contain HyperwalletErrors containing information about what caused the failure
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
// onSuccess: response (HyperwalletPageList<Receipt>) will contain a PageList of Receipts or null if not exists 
// onFailure: error (HyperwalletErrorType) will contain HyperwalletErrors containing information about what caused the failure
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
// onSuccess: response (HyperwalletPageList<Receipt>) will contain a PageList of Receipts or null if not exists 
// onFailure: error (HyperwalletErrorType) will contain HyperwalletErrors containing information about what caused the failure
```

### List Transfer Methods
```java
HyperwalletTransferMethodQueryParam transferMethodQueryParam = new HyperwalletTransferMethodQueryParam.Builder()
        .status(ACTIVATED)
        .limit(100)
        .sortByCreatedOnDesc()
        .build();
Hyperwallet.getDefault().listTransferMethods(transferMethodQueryParam, listener);
// onSuccess: response (HyperwalletPageList<HyperwalletTransferMethod>) will contain a PageList of Transfer Methods or null if not exists 
// onFailure: error (HyperwalletErrorType) will contain HyperwalletErrors containing information about what caused the failure
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
// onFailure: error (HyperwalletErrorType) will contain HyperwalletErrors containing information about what caused the failure
```

### Schedule Transfer
```java
Hyperwallet.getDefault().scheduleTransfer("trf-123456", "schedule transfer notes", listener);
// onSuccess: response (StatusTransition) will contain a StatusTransition of the transfer or null if not exists 
// onFailure: error (HyperwalletErrorType) will contain HyperwalletErrors containing information about what caused the failure 
```

### Get Transfer
```java
Hyperwallet.getDefault().getTransfer("trf-123456", listener);
// onSuccess: response (Transfer) will contain information about the inputted transfer or null if not exists 
// onFailure: error (HyperwalletErrorType) will contain HyperwalletErrors containing information about what caused the failure
```

### List Transfers
```java
TransferQueryParam transferQueryParam = new TransferQueryParam.Builder()
        .limit(100)
        .build();
Hyperwallet.getDefault().listTransfers(transferQueryParam, listener);
// onSuccess: response (HyperwalletPageList<Transfer>) will contain a PageList of Transfers or null if not exists 
// onFailure: error (HyperwalletErrorType) will contain HyperwalletErrors containing information about what caused the failure  
```

## Transfer Method Configurations

### Get countries, currencies and transfer method types
```java
HyperwalletTransferMethodConfigurationKeysQuery query = new HyperwalletTransferMethodConfigurationKeysQuery();
Hyperwallet.getDefault().retrieveTransferMethodConfigurationKeys(query, listener);
// onSuccess: response (HyperwalletTransferMethodConfigurationKey) 
//         will contain a dataset of available Transfer Method Configurations for the program 
// onFailure: error (HyperwalletErrorType) will contain HyperwalletErrors containing information about what caused the failure

// Get Countries/Country
Set<Country> countrySet = result.getCountries();
Country firstCountry = countrySet.iterator().next();

// Get currencies based on first country
Set<Currency> currencySet =  result.getCurrencies(firstCountry.getCode());
Currency firstCurrency = currencySet.iterator().next();

// Get Transfer Method Types for Country/Currency combination
Set<HyperwalletTransferMethodType>transferMethodTypeSet = 
        result.getTransferMethodType(firstCountry.getCode(), firstCurrency.getCode());

```
### Get fields for a transfer method type
```java
HyperwalletTransferMethodConfigurationFieldQuery fieldQuery = new HyperwalletTransferMethodConfigurationFieldQuery(
        "US", "USD", HyperwalletTransferMethod.TransferMethodTypes.BANK_ACCOUNT, "INDIVIDUAL");
Hyperwallet.getDefault().retrieveTransferMethodConfigurationFields(fieldQuery, listener);
// onSuccess: response (HyperwalletTransferMethodConfigurationFieldResult)
//         will contain a dataset of available Transfer Method Configuration Fields for the specified query
// onFailure: error (HyperwalletErrorType) will contain HyperwalletErrors containing information about what caused the failure

result.getFees();
result.getProcessingTime();
result.getFields();
result.getFields().getTransferMethodType();
```

## License
The Hyperwallet Android Core SDK is open source and available under the [MIT](https://github.com/hyperwallet/hyperwallet-android-sdk/blob/master/LICENSE) license.