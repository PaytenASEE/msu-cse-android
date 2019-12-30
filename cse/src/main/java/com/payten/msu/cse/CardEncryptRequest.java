package com.payten.msu.cse;

import android.annotation.SuppressLint;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import static com.payten.msu.cse.CardUtils.isValidCVV;
import static com.payten.msu.cse.CardUtils.isValidCardHolderName;
import static com.payten.msu.cse.CardUtils.isValidExpiry;
import static com.payten.msu.cse.CardUtils.isValidPan;
import static com.payten.msu.cse.CardUtils.normalizeYear;

/**
 * Created by jasmin.suljic@monri.com
 * MSU CSE
 */
final class CardEncryptRequest implements EncryptRequest {

    private final String pan;
    private final Integer expiryYear;
    private final Integer expiryMonth;
    private final String cardHolderName;
    private final String cvv;
    private final String nonce;
    private List<String> errors = new ArrayList<>();

    CardEncryptRequest(String pan, Integer expiryYear, Integer expiryMonth, String cardHolderName, String cvv, String nonce) {
        this.pan = CSETextUtils.removeNonDigits(pan);
        this.expiryYear = normalizeYear(expiryYear, Calendar.getInstance());
        this.expiryMonth = expiryMonth;
        this.cardHolderName = cardHolderName;
        this.cvv = CSETextUtils.removeNonDigits(cvv);
        this.nonce = nonce;
    }


    @Override
    public boolean validate() {

        errors.clear();

        if (!isValidPan(pan)) {
            this.errors.add("PAN_INVALID");
        }

        if (!isValidExpiry(Calendar.getInstance(), expiryMonth, expiryYear)) {
            this.errors.add("EXPIRY_INVALID");
        }

        if (!isValidCardHolderName(cardHolderName)) {
            this.errors.add("CARD_HOLDER_NAME_INVALID");
        }

        if (!isValidCVV(cvv, pan)) {
            this.errors.add("CVV_INVALID");
        }

        if (nonce != null && nonce.length() > 16) {
            this.errors.add("NONCE_MISSING_OR_INVALID");
        }

        return errors.isEmpty();
    }

    @Override
    public List<String> errors() {
        return Collections.unmodifiableList(errors);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public String plain() {
        return String.format("p=%s&y=%d&m=%s&c=%s&cn=%s&n=%s", pan, expiryYear, expiryMonth, cvv, cardHolderName, nonce);
    }
}
