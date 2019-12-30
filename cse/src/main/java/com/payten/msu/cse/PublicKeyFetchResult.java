package com.payten.msu.cse;

/**
 * Created by jasmin.suljic@monri.com
 * MSU CSE
 */
class PublicKeyFetchResult {
    private String publicKey;
    private EncryptException cause;

    public PublicKeyFetchResult(String publicKey, EncryptException cause) {
        this.publicKey = publicKey;
        this.cause = cause;
    }

    static PublicKeyFetchResult failed(EncryptException e) {
        return new PublicKeyFetchResult(null, e);
    }

    static PublicKeyFetchResult success(String publicKey) {
        return new PublicKeyFetchResult(publicKey, null);
    }

    public String getPublicKey() {
        return publicKey;
    }

    public EncryptException getCause() {
        return cause;
    }
}
