package com.payten.msu.cse;

import androidx.annotation.Nullable;

/**
 * Created by jasmin.suljic@monri.com
 * MSU CSE
 */
class CSETextUtils {
    static boolean isBlank(@Nullable String value) {
        return value == null || value.trim().length() == 0;
    }

    /**
     * Converts a card number that may have spaces between the numbers into one without any spaces.
     * Note: method does not check that all characters are digits or spaces.
     *
     * @param cardNumberWithSpaces a card number, for instance "4242 4242 4242 4242"
     * @return the input number minus any spaces, for instance "4242424242424242".
     * Returns {@code null} if the input was {@code null} or all spaces.
     */
    @Nullable
    static String removeNonDigits(@Nullable String cardNumberWithSpaces) {
        if (isBlank(cardNumberWithSpaces)) {
            return null;
        }
        return cardNumberWithSpaces.replaceAll("[^\\d.]", "");
    }

    static String safeSubstring(String pan, @SuppressWarnings("SameParameterValue") int beginIndex, int endIndex) {

        if (pan.length() >= endIndex) {
            return pan.substring(beginIndex, endIndex);
        } else {
            return pan.substring(beginIndex);
        }
    }
}
