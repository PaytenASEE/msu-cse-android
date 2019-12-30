package com.payten.msu.cse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by jasmin.suljic@monri.com
 * MSU CSE
 */
final class CvvEncryptRequest implements EncryptRequest {

    String cvv;
    String nonce;

    private final List<String> errors = new ArrayList<>();

    CvvEncryptRequest(String cvv, String nonce) {
        this.cvv = cvv;
        this.nonce = nonce;
    }

    @Override
    public boolean validate() {
        errors.clear();
        if (!CardUtils.isValidCVV(cvv)) {
            this.errors.add("CVV_INVALID");
        }

        if (!CardUtils.validateNonce(nonce)) {
            this.errors.add("NONCE_MISSING_OR_INVALID");
        }

        return errors.isEmpty();
    }

    @Override
    public List<String> errors() {
        return Collections.unmodifiableList(errors);
    }

    @Override
    public String plain() {
        return String.format("c=%s&n=%s", cvv, nonce);
    }
}
