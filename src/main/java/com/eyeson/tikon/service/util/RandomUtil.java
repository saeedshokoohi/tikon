package com.eyeson.tikon.service.util;

import org.apache.commons.lang.RandomStringUtils;

/**
 * Utility class for generating random Strings.
 */
public final class RandomUtil {

    // for email
//    private static final int DEF_COUNT = 20;

    //new setting for sms validation
    private static final int DEF_COUNT = 5;

    private RandomUtil() {
    }

    /**
     * Generates a password.
     *
     * @return the generated password
     */
    public static String generatePassword() {
        return RandomStringUtils.randomAlphanumeric(DEF_COUNT);
    }

    /**
     * Generates an activation key.
     *
     * @return the generated activation key
     */
    public static String generateActivationKey() {
        return RandomStringUtils.randomNumeric(DEF_COUNT);
    }

    /**
    * Generates a reset key.
    *
    * @return the generated reset key
    */
    public static String generateResetKey() {
        return RandomStringUtils.randomNumeric(DEF_COUNT);
    }
}
