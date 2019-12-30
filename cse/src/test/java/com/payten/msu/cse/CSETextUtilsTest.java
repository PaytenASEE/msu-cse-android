package com.payten.msu.cse;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by jasminsuljic on 2019-12-27.
 * CSE Example
 */
public class CSETextUtilsTest {

    @Test
    public void safeSubstring() {
        Assert.assertEquals("123", CSETextUtils.safeSubstring("123", 0, 5));
        Assert.assertEquals("12", CSETextUtils.safeSubstring("123", 0, 2));
        Assert.assertEquals("12345", CSETextUtils.safeSubstring("12345", 0, 5));
        // length > index
        Assert.assertEquals("12345", CSETextUtils.safeSubstring("123456", 0, 5));
    }
}