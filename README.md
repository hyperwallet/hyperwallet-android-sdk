# Hyperwallet Android Core SDK

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
api 'com.hyperwallet.android:core-sdk:1.0.0-beta03'
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
// TODO: 2019-10-01  
```

### Create PayPal Account

```
final PayPalAccount payPalAccount = new PayPalAccount.Builder()
                .transferMethodCountry("US")
                .transferMethodCurrency("USD")
                .email("jsmith@paypal.com")
                .build();
Hyperwallet.getDefault().createPayPalAccount(bankAccount, listener);
```

### Get PayPal Account
```java
// TODO: 2019-10-01 
```

### Update PayPal Account
```java
// TODO: 2019-10-01 
```

### Deactivate PayPal Account
```java
// TODO: 2019-10-01
```


### List PayPal Account
```java
// TODO: 2019-10-01
```

### Create Bank Account

```
final HyperwalletBankAccount bankAccount = new HyperwalletBankAccount.Builder("US", 
                                                                              "USD", 
                                                                              "8017110254")
                                                                     .branchId("211179539")
                                                                     .build();
Hyperwallet.getDefault().createBankAccount(bankAccount, listener);
```

### Get Bank Account
```java
// TODO: 2019-10-01
```

### Update Bank Account
```
final HyperwalletBankAccount updatedBankAccount = new HyperwalletBankAccount
                                                      .Builder()
                                                      .bankAccountId("new bank account Id")
                                                      .token("your-bank-account-token")
                                                      .build();
Hyperwallet.getDefault().updateBankAccount(updatedBankAccount, listener);
```

### Deactivate Bank Account
```
Hyperwallet.getDefault()
           .deactivateBankAccount("your-bank-account-token", "deactivate bank account", listener);
```

### List Bank Account
```java
// TODO: 2019-10-01
```

### Create Bank Card
```
final HyperwalletBankCard bankCard = new HyperwalletBankCard.Builder("US", 
                                                                     "USD", 
                                                                     "4216701111111114", 
                                                                     "2020-12", 
                                                                     "123")
                                                            .cardBrand("Visa")
                                                            .build()

Hyperwallet.getDefault().createBankCard(bankCard, listener);
```

### Get Bank Card
```java
// TODO: 2019-10-01  
```

### Update Bank Card
```
final HyperwalletBankCard updatedBankCard = new HyperwalletBankCard.Builder()
                                                                   .cardBrand("new card brand")
                                                                   .token("your-bank-card-token")
                                                                   .build()

Hyperwallet.getDefault().updateBankCard(updatedBankCard, listener);
```

### Deactivate Bank Card
```
Hyperwallet.getDefault()
           .deactivateBankAccount("your-bank-card-token", "deactivate bank card", listener);
```

### List Bank Card
```java
// TODO: 2019-10-01  
```

### List Prepaid Cards
```java
// TODO: 2019-10-01  
```

### List Prepaid Card Receipts
```java
// TODO: 2019-10-01  
```

### List User Receipts
```java
// TODO: 2019-10-01
```

### List Transfer Methods
```java
// TODO: 2019-10-01  
```

### Create Transfer
```java
// TODO: 2019-10-01  
```

### Schedule Transfer
```java
// TODO: 2019-10-01  
```

### Get Transfer
```java
// TODO: 2019-10-01  
```

### List Transfers
```java
// TODO: 2019-10-01  
```


## License
The Hyperwallet Android Core SDK is open source and available under the [MIT](https://github.com/hyperwallet/hyperwallet-android-sdk/blob/master/LICENSE) license.