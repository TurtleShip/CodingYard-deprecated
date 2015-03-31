package com.codingyard.api.util;

import org.jasypt.util.password.PasswordEncryptor;
import org.jasypt.util.password.StrongPasswordEncryptor;

public class Encryptor {

    private static final PasswordEncryptor ENCRYPTOR = new StrongPasswordEncryptor();

    public static String encrypt(final String plain) {
        return ENCRYPTOR.encryptPassword(plain);
    }

    public static boolean isSame(final String plain, final String encrypted) {
        return ENCRYPTOR.checkPassword(plain, encrypted);
    }
}
