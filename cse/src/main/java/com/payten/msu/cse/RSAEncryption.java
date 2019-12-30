package com.payten.msu.cse;

import android.util.Base64;

import androidx.annotation.NonNull;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;

/**
 * Created by jasmin.suljic@monri.com
 * MSU CSE
 */
class RSAEncryption {
    private static final int BASE64_FLAG = Base64.NO_WRAP;

    /**
     * Encrypt a string with RSA using a public key
     *
     * @param publicKey the public key
     * @param inputData the data to encrypt
     * @return the data encrypted
     */
    static byte[] encrypt(PublicKey publicKey, String inputData) throws BadPaddingException, IllegalBlockSizeException, NoSuchPaddingException,
            NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, InvalidAlgorithmParameterException {
        Cipher cipher = getEncryptionCipher(publicKey);

        byte[] messageBytes = inputData.getBytes();
//Uint8Array(13) [99, 61, 49, 50, 51, 38, 110, 61, 49, 50, 51, 52, 53]
        return cipher.doFinal(messageBytes, 0, messageBytes.length);
    }

    /**
     * Create and return the encryption cipher
     */
    private static Cipher getEncryptionCipher(PublicKey publicKey) throws NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException,
            InvalidKeyException, InvalidAlgorithmParameterException {
        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPPadding");
        // To use SHA-256 for both digests
        cipher.init(Cipher.ENCRYPT_MODE, publicKey, new OAEPParameterSpec("SHA-256", "MGF1", MGF1ParameterSpec.SHA256, PSource.PSpecified.DEFAULT));
        return cipher;
    }

    @NonNull
    private static PublicKey getKey(String key) {
        try {
            byte[] byteKey = Base64.decode(key.getBytes(), Base64.NO_WRAP);
            X509EncodedKeySpec X509publicKey = new X509EncodedKeySpec(byteKey);
            KeyFactory kf = KeyFactory.getInstance("RSA");

            return kf.generatePublic(X509publicKey);
        } catch (Exception e) {
            throw EncryptException.create(e, EncryptExceptionCode.PUBLIC_KEY_DECODING_FAILED);
        }
    }

    static String encrypt(String publicKeyBase64, String inputData) throws BadPaddingException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, IllegalBlockSizeException, NoSuchProviderException, NoSuchPaddingException, InvalidKeyException, InvalidKeySpecException {
        final PublicKey publicKey = getKey(publicKeyBase64);
        final byte[] encrypt = encrypt(publicKey, inputData);
        return new String(Base64.encode(encrypt, BASE64_FLAG));
    }
}
