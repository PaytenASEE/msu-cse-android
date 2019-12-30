package com.payten.msu.cse;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by jasmin.suljic@monri.com
 * MSU CSE
 */
@RunWith(AndroidJUnit4.class)
public class RSAEncryptionTest {

    @Test
    public void getKey() {
        String key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmbpnmYLKGZe3OAE5CYlvxihoEHoEe1z9istlqD4jYP7ntpIIwIU5ZkG7oJb7D3XrrW3wS6nQW8GfNyJJazCQ3mL9rb+wCT45L+hYQcueQ4sq8QyY8nGqrIsVx5kdMwrI5gPRdRumu8MQeyaDI46NsAIHvuem8Aq2xOlnLiG2MNeWlMAKwGwxm+aPnSS2dqG5nunj75Lp8iAYvKRz8VhbbhJbNEyZd73XYG7IF0VbjnlB44GVPkxIkUt6RD5MBBZEMqFL60cBsVHu5rhpt6Y3h4S6SdXdLu6TcYzAlrMx1ZgHR53Ee3yrkjpVjIT26YOfroeniQgZuOAOplaCKBloYQIDAQAB";

        try {

            final String encoded = RSAEncryption.encrypt(key, new CvvEncryptRequest("123", "12345").plain());
            // Heuristic :)
            Assert.assertEquals(344, encoded.length());
            Assert.assertNotNull(encoded);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}