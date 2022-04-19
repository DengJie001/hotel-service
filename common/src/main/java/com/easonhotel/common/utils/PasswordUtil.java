package com.easonhotel.common.utils;

import org.apache.commons.codec.digest.DigestUtils;

public class PasswordUtil {
    public static String sha256Times(String value, String key, int times) {
        String str = value;
        for (int i = 0; i < times; ++i) {
            if (i == 499) {
                str += key;
            }
            str = DigestUtils.sha256Hex(str);
        }
        return str;
    }

    public static String encrypt(String password) {
        return sha256Times(password, "6bda3f60-2f84-4b73-a495-a0ec3d55afd5", 998);
    }
}
