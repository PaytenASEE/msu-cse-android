package com.payten.msu.cse;

/**
 * Created by jasmin.suljic@monri.com
 * MSU CSE
 */
final class CSEApiImpl implements CSEApi {

    CSEApiImpl() {
    }

    @Override
    public PublicKeyFetchResult fetchPublicKey() {

        return PublicKeyFetchResult.success("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAhGNpIhKTlCi1iwKEbFD2CTL0AYbMV+QCaP/5bl2hkBjgkQdG931Vep7Z4gVCSYCmmE4T8d1TIdkNoTPwOltzoX9Z1pI/EoqktNLlS3re+dApPU36FHGaGaPCfNR+/zJ1Pd1qazaZ5SJhFyf17KU9HLi7w9WYRJVGDWj6CJKeefWYLclLThD+SBCmpJTqhDdFRt9bW1LwSqfshmSzxI7jHTqnj+o4Ikv2xC4V7bIwjzmUk7t4IzT+rJcin+oB+Xgq+stxvZodZrpSZbXnPNObSIsVCxXqdDz1lXjkwMc9aV0X5KqOjEK87QjguPAGsba3AfbWIWjzuR3xoAVzQRo+tQIDAQAB");
    }


    @SuppressWarnings("unused")
    private String endpoint(boolean developmentMode) {
        if (developmentMode) {
            return "https://test.merchantsafeunipay.com/msu/cse/publickey";
        } else {
            return "https://merchantsafeunipay.com/msu/cse/publickey";
        }
    }
}
