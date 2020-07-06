package com.payten.msu.cse;

import org.junit.Assert;
import org.junit.Test;

import java.util.Calendar;

/**
 * Created by jasmin.suljic@monri.com
 * MSU CSE
 */
public class CardEncryptRequestTest {

    private CardEncryptRequest request;

    @Test
    public void validate() {
        final Calendar instance = Calendar.getInstance();
        request = new CardEncryptRequest("4111 1111 1111 1111", instance.get(Calendar.YEAR), 12, "Test test", "123", "random");
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

    @Test
    public void plain() {
        final Calendar instance = Calendar.getInstance();
        instance.add(Calendar.YEAR, 3);
        request = new CardEncryptRequest("4111 1111 1111 1111", instance.get(Calendar.YEAR), 5, "Test test", "123", "random");
        String expected = String.format("p=4111111111111111&y=%s&m=%s&c=123&cn=Test test&n=random", instance.get(Calendar.YEAR), "05");
        Assert.assertEquals(expected, request.plain());
    }

    @Test
    public void plain1() {
        final Calendar instance = Calendar.getInstance();
        instance.add(Calendar.YEAR, 3);
        request = new CardEncryptRequest("4111 1111 1111 1111", instance.get(Calendar.YEAR), 11, "Test test", "123", "random");
        String expected = String.format("p=4111111111111111&y=%s&m=%s&c=123&cn=Test test&n=random", instance.get(Calendar.YEAR), "11");
        Assert.assertEquals(expected, request.plain());
    }
}