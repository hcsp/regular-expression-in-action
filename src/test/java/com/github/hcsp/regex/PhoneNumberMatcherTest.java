package com.github.hcsp.regex;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PhoneNumberMatcherTest {
    @Test
    public void test() {
        Assertions.assertTrue(PhoneNumberMatcher.isPhoneNumber("021-12345678"));
        Assertions.assertTrue(PhoneNumberMatcher.isPhoneNumber("0211-12345678"));
        Assertions.assertTrue(PhoneNumberMatcher.isPhoneNumber("0211-1234567"));

        Assertions.assertFalse(PhoneNumberMatcher.isPhoneNumber("021-02345678"));
        Assertions.assertFalse(PhoneNumberMatcher.isPhoneNumber("02111-12345678"));
        Assertions.assertFalse(PhoneNumberMatcher.isPhoneNumber("111-12345678"));
        Assertions.assertFalse(PhoneNumberMatcher.isPhoneNumber("011-1234567"));
    }
}
