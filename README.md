# Client Side Encryption(CSE)

## Introduction
Client Side Encryption is a way to encrypt customer sensitive information on mobile device, before card data passes any other medium(i.e. merchant server). The model is suitable for merchants wanting to do API SALE/PREAUTH from their servers, because card data will pass encrypted on merchant server, this way avoiding the responsibility of "hosting" any card data on merchant side.

## How it works
Merchant must include MSU Android CSE SDK in their mobile application:

`implementation 'com.payten.msu:cse-android:1.1.0'`

The steps should be like this:
- include library in application
- initialize library
- After user has filled first 6 digits of pan you can issue a [SHOULDDO3D](https://test.merchantsafeunipay.com/msu/api/v2/doc#shouldDo3D) API Call to determine the next steps
- Customer has filled card data and clicks pay. If [SHOULDDO3D](https://test.merchantsafeunipay.com/msu/api/v2/doc#shouldDo3D) returned `YES`, then no CSE is needed as customer sensitive information will pass from Browser directly to MSU Servers via the Auth 3D flow. One of request parameters of the Auth 3D flow is `callbackUrl` which is an endpoint on merchant side that will receive the final 3D response, including `auth3DToken` if 3d authentication was successful. At this point you can call [API SALE](https://test.merchantsafeunipay.com/msu/api/v2/doc#sale) with the `auth3DToken`
- If [SHOULDDO3D](https://test.merchantsafeunipay.com/msu/api/v2/doc#shouldDo3D) returns `NO` or `OPTIONAL`, you may skip the 3D Journey and intercept main payment form by encrypting the data using the CSE SDK Payment With New Card

```java
boolean developmentMode = true;
CSE cse = new CSE(developmentMode);
cse.encrypt("4355084355084358",
    "Test Test",
    2020,
    12,
    "000",
    nonce(),
    new EncryptCallback() {
      @Override
      public void onSuccess(String result) {
          encryptionSuccess(result);
      }

      @Override
      public void onError(EncryptException encryptException) {
          encryptionError(encryptException);
      }
});
```

Payment with existing card/wallet, encrypting CVV
```java
boolean developmentMode = true;
CSE cse = new CSE(developmentMode);
cse.encrypt("123", nonce(), new EncryptCallback() {
      @Override
      public void onSuccess(String result) {
          encryptionSuccess(result);
      }

      @Override
      public void onError(EncryptException encryptException) {
          encryptionError(encryptException);
      }
});
```

Example is available on this [link](https://github.com/PaytenASEE/msu-cse-android/blob/master/app/src/main/java/com/payten/msu/cse/example/ExampleActivity.java)

# CSE SDK API Information

Make sure you have included library in application `build.gradle` file.

- After including library instantiate `CSE` library:

```
// Change this to false if production
// If set to true MSU test environment is used for encryption, meaning encrypted values with developmentMode = true will not work on production env
boolean developmentMode = true;
CSE cse = new CSE()
```
Available methods on `cse` object:
* encrypt card data
  * pan: valid pan
  * cardHolderName: non empty string
  * expiryYear: valid year, in `YYYY` format
  * expiryMonth: value from 1-12
  * cvv: valid cvv value, eg 123 is valid for VISA, 1234 is not
  * nonce: random generated alphanumeric value, max length 16 characters
  * callback: interface consisting of two methods: `onSuccess` and `onError`

Method signature:
```java
void encrypt(String pan,
            String cardHolderName,
            Integer expiryYear,
            Integer expiryMonth,
            String cvv,
            String nonce,
            EncryptCallback callback
    )
```

* encrypt cvv for existing card/wallet payment
  * cvv: valid cvv
  * nonce: random generated alphanumeric value, max length 16 characters

Method signature:
```java
void encrypt(String cvv,
             String nonce,
             EncryptCallback callback
    )
```
* hasErrors: returns true if any errors occurred during encryption 
Method signature:
```java

boolean hasErrors()

```

* getErrors: returns list of errors ocurred during encryption
```java

public List<String> getErrors();

```

* isValidCardHolderName: returns true if card holder's name is non empty
Method signature:
```java

boolean isValidCardHolderName(String name);

```
* isValidPan - returns true if pan is valid
Method signature:
```java
boolean isValidPan(String pan)
```
* isValidCVV(cvv) - returs true if cvv is valid length
Method signature:
```java
boolean isValidCVV(String cvv)
```

* isValidCVV(cvv, pan) - returs true if cvv is valid for brand detected from pan.
Method signature:
```java
boolean isValidCVV(String cvv, String pan)
```

* isValidCardToken - returns true if card token length is greater or equal to 32 and less or equal to 64
Method signature:
```java
boolean isValidCardToken(String cardToken)
```
* detectBrand: returns detected card brand, or `CardBrand.UNKNOWN`
Method signature:
```java
CardBrand detectBrand(String pan)
```

Usage example is available on [link](https://github.com/PaytenASEE/msu-cse-android/blob/master/app/src/main/java/com/payten/msu/cse/example/ExampleActivity.java#L92)

