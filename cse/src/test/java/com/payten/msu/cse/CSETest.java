package com.payten.msu.cse;

import android.os.Handler;

import androidx.core.util.Pair;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * Created by jasmin.suljic@monri.com
 * MSU CSE
 */
@SuppressWarnings("ConstantConditions")
public class CSETest {

    private CSE cse;

    @Before
    public void setUp() throws Exception {
        cse = new CSE(Mockito.mock(Handler.class), true);
    }

    @Test
    public void isValidCVV() {
        Arrays.asList(
                Pair.create("123", true),
                Pair.create("1234", true),
                Pair.create("12", false),
                Pair.create("1", false),
                Pair.create("12345", false)
        ).forEach(pair -> {
            //noinspection ConstantConditions
            if (pair.second) {
                Assert.assertTrue(String.format("CVV = %s should be valid", pair.first), cse.isValidCVV(pair.first));
            } else {
                Assert.assertFalse(String.format("CVV = %s should be invalid", pair.first), cse.isValidCVV(pair.first));
            }
        });
    }


    @Test
    public void isValidCVV1() {
        Arrays.asList(
                Pair.create(new String[]{"4111 1111 1111 1111", "123"}, true),
                Pair.create(new String[]{"4111 1111 1111 1111", "1234"}, false),
                Pair.create(new String[]{"4111 1111 1111 1111", "12"}, false),
                Pair.create(new String[]{"3782 822463 10005", "1234"}, true),
                Pair.create(new String[]{"378282246310005", "123"}, true),
                Pair.create(new String[]{"378282246310005", "12345"}, false),
                Pair.create(new String[]{"378282246310005", "12"}, false),
                Pair.create(new String[]{"55554444 4444 4444", "123"}, true),
                Pair.create(new String[]{"55554444 4444 4444", "1234"}, false),
                Pair.create(new String[]{"55554444 4444 4444", "12"}, false)
        ).forEach(pair -> {
            //noinspection ConstantConditions
            final String cvv = pair.first[1];
            final String pan = pair.first[0];
            if (pair.second) {
                Assert.assertTrue(String.format("CVV = %s should be valid for pan = %s", cvv, pan), cse.isValidCVV(cvv, pan));
            } else {
                Assert.assertFalse(String.format("CVV = %s should be invalid for pan = %s", cvv, pan), cse.isValidCVV(cvv, pan));
            }
        });
    }

    @Test
    public void isValidCardHolderName() {
        Arrays.asList(
                Pair.create("", false),
                Pair.create("a", true),
                // 128 - max
                Pair.create(repeat(128, '0'), true),
                /// 129 - over max
                Pair.create(repeat(129, '0'), false)
        ).forEach(pair -> {
            if (pair.second) {
                Assert.assertTrue(String.format("CardHolder name = '%s' should be valid", pair.first), cse.isValidCardHolderName(pair.first));
            } else {
                Assert.assertFalse(String.format("CardHolder name = '%s' should be invalid", pair.first), cse.isValidCardHolderName(pair.first));
            }
        });
    }

    @Test
    public void isValidPan() {
        Arrays.asList(
                Pair.create("4111 1111 1111 1111", true),
                Pair.create("4111 1111 1111", false),
                Pair.create("4111 1111 1111 1112", false),
                Pair.create("4242 4242 4242 4242", true),
                Pair.create("4242 4242 4242 4243", false),
                Pair.create("5168441223630339", true),
                Pair.create("5555 4444 4444 4441", false),
                Pair.create("3782 822463 10005", true),
                Pair.create("3782 822463 10000", false)
        ).forEach(pair -> {
            //noinspection ConstantConditions
            if (pair.second) {
                Assert.assertTrue(String.format("pan = %s should be valid", pair.first), cse.isValidPan(pair.first));
            } else {
                Assert.assertFalse(String.format("pan = %s should be invalid", pair.first), cse.isValidPan(pair.first));
            }
        });
    }

    @Test
    public void isValidCardToken() {
        Arrays.asList(
                Pair.create(repeat(32, 'a'), true),
                Pair.create(repeat(33, 'b'), true),
                Pair.create(repeat(31, 'c'), false),
                Pair.create(repeat(29, 'd'), false),
                Pair.create(repeat(64, 'e'), true),
                Pair.create(repeat(63, 'f'), true),
                Pair.create(repeat(65, 'g'), false)
        ).forEach(pair -> {
            if (pair.second) {
                Assert.assertTrue(String.format("cardToken '%s' should be valid", pair.first), cse.isValidCardToken(pair.first));
            } else {
                Assert.assertFalse(String.format("cardToken '%s' should be invalid", pair.first), cse.isValidCardToken(pair.first));
            }
        });
    }

    @Test
    public void detectBrand() {
        Arrays.asList(
                Pair.create("411111", CardBrand.VISA),
                Pair.create("5168 4412 2363 0339", CardBrand.MASTERCARD),
                Pair.create("3782 822463 10005", CardBrand.AMERICAN_EXPRESS),
                Pair.create("989100", CardBrand.DINACARD),
                Pair.create("989101", CardBrand.DINACARD),
                Pair.create("657371", CardBrand.DISCOVER)
        ).forEach(pair -> Assert.assertEquals(String.format("Bin '%s' brand should be %s", pair.first, pair.second),
                pair.second,
                cse.detectBrand(pair.first)
        ));
    }

    @Test
    public void isValidExpiry() {
        final Calendar instance = Calendar.getInstance();
        // months are from 0 in java
        final int currentMonth = instance.get(Calendar.MONTH) + 1;
        final int currentYear = instance.get(Calendar.YEAR);


        final List<Pair<Integer[], Boolean>> testValues = new ArrayList<>(Arrays.asList(
                Pair.create(new Integer[]{currentYear, currentMonth}, true),
                Pair.create(new Integer[]{currentYear + 1, currentMonth}, true),
                Pair.create(new Integer[]{currentYear - 1, currentMonth}, false)
        ));

        if (currentMonth > 1) {
            testValues.add(Pair.create(new Integer[]{currentYear, currentMonth - 1}, false));
        }

        if (currentMonth < 12) {
            testValues.add(Pair.create(new Integer[]{currentYear, currentMonth + 1}, true));
        }

        testValues.forEach(pair -> {
            final Integer month = pair.first[1];
            final Integer year = pair.first[0];
            if (pair.second) {
                Assert.assertTrue(String.format("Expiry date = %s/%s should be valid", year, month), cse.isValidExpiry(month, year));
            } else {
                Assert.assertFalse(String.format("Expiry date = %s/%s should be invalid", year, month), cse.isValidExpiry(month, year));
            }
        });
    }

    static String repeat(int length, char repeat) {
        return new String(new char[length]).replace('\0', repeat);
    }
}