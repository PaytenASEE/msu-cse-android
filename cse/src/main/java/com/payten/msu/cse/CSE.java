package com.payten.msu.cse;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by jasmin.suljic@monri.com
 * MSU CSE
 */
public final class CSE {

    private final List<String> errors;
    private AsyncTask<Void, Void, EncryptTaskResult> task;
    private final Handler handler;
    private final CSEApi cseApi;

    public CSE(boolean developmentMode) {
        this(new Handler(Looper.getMainLooper()));
    }

    CSE(Handler handler) {
        this.handler = handler;
        errors = new ArrayList<>();
        this.cseApi = new CSEApiImpl();
    }


    public boolean isValidCVV(String cvv) {
        return isValidCVV(cvv, null);
    }

    public List<String> getErrors() {
        return errors;
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    public boolean isValidCVV(String cvv, String pan) {
        return CardUtils.isValidCVV(cvv, pan);
    }

    public boolean isValidCardHolderName(String name) {
        return CardUtils.isValidCardHolderName(name);
    }

    public boolean isValidPan(String pan) {
        return CardUtils.isValidPan(pan);
    }

    public boolean isValidCardToken(String cardToken) {
        return CardUtils.isValidCardToken(cardToken);
    }

    public CardBrand detectBrand(String pan) {
        return CardUtils.detectBrand(pan);
    }

    public void encrypt(String pan,
                        String cardHolderName,
                        Integer expiryYear,
                        Integer expiryMonth,
                        String cvv,
                        String nonce,
                        EncryptCallback callback
    ) {

        encrypt(new CardEncryptRequest(
                pan, expiryYear, expiryMonth, cardHolderName, cvv, nonce
        ), callback);
    }

    public void encrypt(String cvv,
                        String nonce,
                        EncryptCallback callback
    ) {
        encrypt(new CvvEncryptRequest(cvv, nonce), callback);
    }

    private void encrypt(EncryptRequest request, final EncryptCallback callback) {

        try {
            this.errors.clear();
            if (request.validate()) {
                this.task = new EncryptTask(callback, request, cseApi).execute();
            } else {
                this.errors.addAll(request.errors());
                callback.onError(EncryptException.create(EncryptExceptionCode.VALIDATION_FAILED));
            }
        } catch (final Exception e) {
            handler.post(() -> callback.onError(EncryptException.create(e, EncryptExceptionCode.UNKNOWN_EXCEPTION)));
        }
    }

    public void onDestroy() {
        if (task != null) {
            task.cancel(true);
            task = null;
        }
    }

    /**
     * @param month
     * @param year
     * @return
     */
    public boolean isValidExpiry(Integer month, Integer year) {
        return CardUtils.isValidExpiry(Calendar.getInstance(), month, year);
    }

}
