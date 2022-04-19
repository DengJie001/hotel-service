package com.easonhotel.common.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ParseUtils {
    public static String parseString(List<String> list, String separator) {
        StringBuilder builder = new StringBuilder();
        if (list == null || list.size() == 0) {
            return null;
        }
        for (int i = 0; i < list.size(); ++i) {
            builder.append(list.get(i));
            if (i < list.size() - 1) {
                builder.append(separator);
            }
        }
        return builder.toString();
    }

    public static List<String> parseList(String str, String separator) {
        List<String> list = new ArrayList<>();
        list.addAll(Arrays.asList(str.split(separator)));
        return list;
    }
}
