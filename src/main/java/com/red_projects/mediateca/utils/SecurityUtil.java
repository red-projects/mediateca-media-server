package com.red_projects.mediateca.utils;

import com.red_projects.mediateca.security.PasswordRequirements;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Base64;
import java.util.Random;

public class SecurityUtil {

    public static PasswordRequirements passwordRequirements = new PasswordRequirements();

    private static final BCryptPasswordEncoder bCryptEncoder = new BCryptPasswordEncoder(10);

    public static String bCryptEncode(String string) {
        return bCryptEncoder.encode(string);
    }

    public static boolean bCryptCompare(String string, String encodedString) {
        return bCryptEncoder.matches(string, encodedString);
    }

    public static String base64Encode(String string) {
        return Base64.getEncoder().encodeToString(string.getBytes());

    }

    public static String base64Decode(String string) {
        try {
            return new String(Base64.getDecoder().decode(string));
        }
        catch (Exception e) {
            return null;
        }
    }


    public static String generateVerificationCode() {
        return generatePasscode(64, false);
    }

    public static String generatePasscode(int length, boolean withSymbols) {
        String lowerCase = "abcdefghijklmnopqrstovwxyz";
        String upperCase = "ABCDEFGHIJKLMNOPQRSTOVWXYZ";
        String numeric = "0123456789";
        String symbols = "!@#$%&*()_+-=[]|,./?><";

        String alphaNumeric = lowerCase + upperCase + numeric;
        if (withSymbols) {
            alphaNumeric = alphaNumeric + symbols;
        }

        Random random = new Random();
        StringBuilder passcode = new StringBuilder();
        for (int i = 0; i < length; i++) {
            passcode.append(alphaNumeric.charAt(random.nextInt(alphaNumeric.length())));
        }
        return passcode.toString();
    }


}
