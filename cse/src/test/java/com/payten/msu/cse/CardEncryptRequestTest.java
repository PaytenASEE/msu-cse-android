package com.payten.msu.cse;

import org.junit.Assert;
import org.junit.Test;

import java.util.Calendar;

import static com.payten.msu.cse.CSETest.MAESTRO_TEST_CARDS;

/**
 * Created by jasmin.suljic@monri.com
 * MSU CSE
 */
public class CardEncryptRequestTest {

    CardEncryptRequest request;

    @Test
    public void validate() {
        final Calendar instance = Calendar.getInstance();
        request = new CardEncryptRequest(MAESTRO_TEST_CARDS.get(0), instance.get(Calendar.YEAR), 12, "Test test", "123", "random");
        Assert.assertTrue(request.validate());
        Assert.assertTrue(request.errors().isEmpty());
    }

    @Test
    public void validate1() {
        final Calendar instance = Calendar.getInstance();
        // Wrong pan
        request = new CardEncryptRequest("4111 1111 1111", instance.get(Calendar.YEAR), 12, "Test test", "123", "random");
        Assert.assertFalse(request.validate());
        Assert.assertEquals(1, request.errors().size());

        Assert.assertEquals("PAN_INVALID", request.errors().get(0));
    }
}