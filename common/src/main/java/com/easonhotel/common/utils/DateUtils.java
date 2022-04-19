package com.easonhotel.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    public static String parseDate(Long mills) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String format = simpleDateFormat.format(new Date(mills));
        return format;
    }

    public static String parseDate(Long mills, String formatStr) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatStr);
        String res = simpleDateFormat.format(new Date(mills));
        return res;
    }
}
