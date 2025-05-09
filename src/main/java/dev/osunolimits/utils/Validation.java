package dev.osunolimits.utils;

import java.security.SecureRandom;
import java.util.Random;
import java.util.regex.Pattern;

public class Validation {
    private static final Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final Random RANDOM = new SecureRandom();

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }

        if (!pattern.matcher(strNum).matches()) {
            return false;
        }

        try {
            double num = Double.parseDouble(strNum);
            return num >= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static String randomString(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("Length must be greater than 0");
        }

        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }
}