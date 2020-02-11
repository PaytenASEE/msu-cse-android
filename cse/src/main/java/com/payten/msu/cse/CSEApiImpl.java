package com.payten.msu.cse;

import androidx.annotation.Nullable;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by jasmin.suljic@monri.com
 * MSU CSE
 */
final class CSEApiImpl implements CSEApi {

    @Nullable
    private String publicKey;
    private final String publicKeyUrl;

    CSEApiImpl(boolean developmentMode) {
        publicKeyUrl = endpoint(developmentMode);
    }

    @Override
    public PublicKeyFetchResult fetchPublicKey() {

        if (publicKey != null) {
            return PublicKeyFetchResult.success(publicKey);
        } else {

            try {
                // Instantiate the RequestQueue.
                URL url = new URL(publicKeyUrl);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    BufferedReader r = new BufferedReader(new InputStreamReader(in));
                    StringBuilder total = new StringBuilder();
                    for (String line; (line = r.readLine()) != null; ) {
                        total.append(line).append('\n');
                    }

                    JSONObject jsonObject = new JSONObject(total.toString());
                    this.publicKey = jsonObject.getString("publicKey");
                    return PublicKeyFetchResult.success(publicKey);
                } finally {
                    urlConnection.disconnect();
                }
            } catch (Exception e) {
                return PublicKeyFetchResult.failed(EncryptException.create(e, EncryptExceptionCode.REQUEST_FAILED));
            }
        }
    }


    @SuppressWarnings("unused")
    private static String endpoint(boolean developmentMode) {
        if (developmentMode) {
            return "https://test.merchantsafeunipay.com/msu/cse/publickey";
        } else {
            return "https://merchantsafeunipay.com/msu/cse/publickey";
        }
    }
}
