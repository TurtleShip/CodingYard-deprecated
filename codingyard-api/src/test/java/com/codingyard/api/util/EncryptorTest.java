package com.codingyard.api.util;

import com.codingyard.api.util.Encryptor;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class EncryptorTest {

    private final String password = "safe_password_YolO_123_#$!";

    @Test
    public void plainPasswordShouldMatchItsEncryptedVersion() throws Exception {
        assertTrue(Encryptor.isSame(password, Encryptor.encrypt(password)));
    }
}
