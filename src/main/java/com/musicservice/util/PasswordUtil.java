package com.musicservice.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public final class PasswordUtil {

    private static final BCryptPasswordEncoder ENCODER = new BCryptPasswordEncoder();

    private PasswordUtil() {
    }

    public static String hash(String password) {
        return ENCODER.encode(password);
    }

    public static boolean verify(String password, String storedHash) {
        return ENCODER.matches(password, storedHash);
    }

}