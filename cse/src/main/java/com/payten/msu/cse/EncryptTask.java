package com.payten.msu.cse;

import android.os.AsyncTask;
import android.util.Log;

import java.lang.ref.WeakReference;

/**
 * Created by jasmin.suljic@monri.com
 * MSU CSE
 */
final class EncryptTask extends AsyncTask<Void, Void, EncryptTaskResult> {

    private final WeakReference<EncryptCallback> callback;
    private final EncryptRequest request;
    private final CSEApi cseApi;


    EncryptTask(EncryptCallback callback, EncryptRequest request, CSEApi cseApi) {
        this.callback = new WeakReference<>(callback);
        this.request = request;
        this.cseApi = cseApi;
    }

    @Override
    protected EncryptTaskResult doInBackground(Void... voids) {

        try {
            final PublicKeyFetchResult publicKeyFetchResult = cseApi.fetchPublicKey();

            if (publicKeyFetchResult.getCause() != null) {
                return EncryptTaskResult.failed(publicKeyFetchResult.getCause());
            }

            final String publicKey = publicKeyFetchResult.getPublicKey();

            return EncryptTaskResult.success(RSAEncryption.encrypt(publicKey, request.plain()));
        } catch (Exception e) {
            return EncryptTaskResult.failed(e, EncryptExceptionCode.UNKNOWN_EXCEPTION);
        }
    }

    @Override
    protected void onPostExecute(EncryptTaskResult encryptTaskResult) {
        EncryptCallback encryptCallback = callback.get();

        //noinspection ConstantConditions
        if (callback == null) {
            Log.w("EncryptTask", "onPostExecute invoked with callback = null");
            return;
        }

        if (encryptTaskResult.getEncrypted() != null) {
            encryptCallback.onSuccess(encryptTaskResult.getEncrypted());
        } else {
            encryptCallback.onError(encryptTaskResult.getEncryptException());
        }
    }
}
