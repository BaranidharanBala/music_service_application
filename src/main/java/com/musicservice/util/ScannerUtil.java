package com.musicservice.util;

import java.util.Scanner;

public final class ScannerUtil {

    private static Scanner scanner;

    private ScannerUtil() {
    }

    public static Scanner getScanner() {
        if (scanner == null) {
            scanner = new Scanner(System.in);
        }
        return scanner;
    }

}