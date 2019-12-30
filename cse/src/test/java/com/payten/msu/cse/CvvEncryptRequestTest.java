package com.payten.msu.cse;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * Created by jasmin.suljic@monri.com
 * MSU CSE
 */
public class CvvEncryptRequestTest {

    private CvvEncryptRequest request;

    @Test
    public void validate() {
        request = new CvvEncryptRequest("12", "123456");

        final boolean validate = request.validate();
        Assert.assertFalse(validate);
        final List<String> errors = request.errors();
        Assert.assertEquals(1, errors.size());
        Assert.assertEquals("CVV_INVALID", errors.get(0));
    }

    @Test
    public void validate1() {
        // valid cvv, invalid nonce
        request = new CvvEncryptRequest("123", "1234_1234_1234_12");

        final boolean validate = request.validate();
        Assert.assertFalse(validate);
        final List<String> errors = request.errors();
        Assert.assertEquals(1, errors.size());
        Assert.assertEquals("NONCE_MISSING_OR_INVALID", errors.get(0));
    }

    @Test
    public void validate2() {
        // invalid cvv, invalid nonce
        request = new CvvEncryptRequest("12", "1234_1234_1234_12");

        final boolean validate = request.validate();
        Assert.assertFalse(validate);
        final List<String> errors = request.errors();
        Assert.assertEquals(2, errors.size());
        Assert.assertEquals("CVV_INVALID", errors.get(0));
        Assert.assertEquals("NONCE_MISSING_OR_INVALID", errors.get(1));
    }

    @Test
    public void validate3() {
        // valid cvv, invalid nonce
        request = new CvvEncryptRequest("123", "1234_1234_1234_");

        final boolean validate = request.validate();
        Assert.assertTrue(validate);
        final List<String> errors = request.errors();
        Assert.assertEquals(0, errors.size());
    }

    @Test
    public void validate4() {
        // invalid cvv
        request = new CvvEncryptRequest("12", "1234_1234_1234_");

        final boolean validate = request.validate();
        Assert.assertFalse(validate);

        // Update CVV
        request.cvv = "123";

        Assert.assertTrue(request.validate());
        final List<String> errors = request.errors();
        Assert.assertEquals(0, errors.size());
    }


}