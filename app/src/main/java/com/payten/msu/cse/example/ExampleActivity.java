package com.payten.msu.cse.example;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.payten.msu.cse.CSE;
import com.payten.msu.cse.CardBrand;
import com.payten.msu.cse.EncryptCallback;
import com.payten.msu.cse.EncryptException;

import java.nio.charset.Charset;
import java.util.Random;

@SuppressWarnings("CreateIntent")
public class ExampleActivity extends AppCompatActivity {
    CSE cse;

    private static final String TAG = "CSE_EXAMPLE_APP";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example);

        cse = new CSE(true);

        findViewById(R.id.usage_example).setOnClickListener(this::usageExample);
        findViewById(R.id.encrypt_card_example).setOnClickListener(this::encryptExampleCard);
        findViewById(R.id.encrypt_cvv_example).setOnClickListener(this::encryptExampleCvv);
    }

    private void encryptExampleCvv(View view) {
        cse.encrypt("123", nonce(), new EncryptCallback() {
            @Override
            public void onSuccess(String result) {
                encryptionSuccess(result);
            }

            @Override
            public void onError(EncryptException encryptException) {
                onError(encryptException);
            }
        });
    }

    void encryptionSuccess(String result) {
        Log.d(TAG, result);
        Toast.makeText(this, String.format("Encrypt success = %s", result), Toast.LENGTH_LONG).show();
    }

    void encryptionError(EncryptException encryptException) {
        Toast.makeText(ExampleActivity.this, encryptException.getMessage(), Toast.LENGTH_LONG).show();

        // Check for validation errors
        if (cse.hasErrors()) {
            for (String error : cse.getErrors()) {
                Log.d(TAG, String.format("Validation error %s", error));
            }
        }
    }

    private void encryptExampleCard(View view) {
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
    }

    private String nonce() {
        byte[] array = new byte[16]; // length is bounded by 7
        new Random().nextBytes(array);
        return new String(array, Charset.forName("UTF-8"));
    }

    void usageExample(View view) {
        final String visaPan = "4111 1111 1111 1111";
        if (cse.isValidPan(visaPan)) {
            Log.d(TAG, "Pan is valid");
        }

        if (cse.isValidCardHolderName("John")) {
            Log.d(TAG, "cardholder name is valid");
        }

        if (cse.isValidCVV("123")) {
            Log.d(TAG, "cvv is valid");
        }

        if (!cse.isValidCVV("1234", visaPan)) {
            Log.d(TAG, "cvv 1234 is not valid for VISA card");
        }

        final String invalidCardToken = new String(new char[65]).replace('\0', 'E');
        if (!cse.isValidCardToken(invalidCardToken)) {
            Log.d(TAG, "card token is invalid");
        }

        if (!cse.isValidExpiry(10, 2018)) {
            Log.d(TAG, "Expiry year in past");
        }

        // dinacard - 989100
        final CardBrand cardBrand = cse.detectBrand("989100");

        if (cardBrand.equals(CardBrand.DINACARD)) {
            Log.d(TAG, "Detected brand is dinacard");
        }
    }

    @Override
    protected void onDestroy() {
        // Stop long running task, maybe public key fetch is slow
        cse.onDestroy();
        super.onDestroy();
    }
}
