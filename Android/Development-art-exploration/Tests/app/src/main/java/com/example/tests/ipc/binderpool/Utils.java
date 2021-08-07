package com.example.tests.ipc.binderpool;

public class Utils {

    public static String encrypt(String content, char de) {
        char[] chars = content.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            chars[i] ^= de;
        }
        return new String(chars);
    }
}
