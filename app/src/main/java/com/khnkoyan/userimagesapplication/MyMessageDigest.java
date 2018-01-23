package com.khnkoyan.userimagesapplication;

import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MyMessageDigest {
    public static String makeMD5(String password) {
        String a = null;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(password.getBytes());
            byte[] digest = messageDigest.digest();
            StringBuilder strBuilder = new StringBuilder();
            for (byte b : digest) {
                strBuilder.append(String.format("%02X", b & 0xff));
            }
            a = strBuilder.toString();
            Log.i("myLog", "strBuffer: " + a);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return a;
    }
}
