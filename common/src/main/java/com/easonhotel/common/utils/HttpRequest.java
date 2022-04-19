package com.easonhotel.common.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpRequest {
    private static final String HTTP_METHOD_GET = "GET";

    public static String doGet(String url) throws IOException {
        URL targetUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) targetUrl.openConnection();
        connection.setRequestMethod(HTTP_METHOD_GET);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setInstanceFollowRedirects(false);
        connection.connect();
        StringBuffer stringBuffer = new StringBuffer();
        try (
                InputStream inputStream = connection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);) {
            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                stringBuffer.append(str);
            }
            String result = stringBuffer.toString();
            return result;
        }
    }
}
