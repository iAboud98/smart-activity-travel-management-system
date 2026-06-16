package com.encs5150.students1220216_1220071.travelplanner.utils;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ValidationUtilsTest {

    @Test
    public void emailValidation_acceptsOnlyFormattedAddresses() {
        assertTrue(ValidationUtils.isValidEmail("traveler@example.com"));
        assertTrue(ValidationUtils.isValidEmail(" first.last+trip@example.co "));

        assertFalse(ValidationUtils.isValidEmail(""));
        assertFalse(ValidationUtils.isValidEmail("traveler"));
        assertFalse(ValidationUtils.isValidEmail("traveler@example"));
    }

    @Test
    public void passwordValidation_requiresLengthLetterAndNumber() {
        assertTrue(ValidationUtils.isValidPassword("travel1"));

        assertFalse(ValidationUtils.isValidPassword("trv1"));
        assertFalse(ValidationUtils.isValidPassword("travel"));
        assertFalse(ValidationUtils.isValidPassword("123456"));
    }

    @Test
    public void phoneValidation_allowsOptionalInternationalPrefix() {
        assertTrue(ValidationUtils.isValidPhone("0591234567"));
        assertTrue(ValidationUtils.isValidPhone("+970 59-123-4567"));

        assertFalse(ValidationUtils.isValidPhone(""));
        assertFalse(ValidationUtils.isValidPhone("123"));
        assertFalse(ValidationUtils.isValidPhone("+97059abc456"));
    }
}
