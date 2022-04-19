package com.easonhotel.common.utils;

import org.apache.commons.codec.digest.DigestUtils;

public class WxUtils {
    public static String getSignature(String rawData, String sessionKey) {
        return DigestUtils.sha1Hex(rawData + sessionKey);
    }

    public static boolean valid(String rawData, String sessionKey, String signature) {
        System.out.println(getSignature(rawData, sessionKey));
        System.out.println(signature);
        return getSignature(rawData, sessionKey).equals(signature);
    }
}
