package com.easonhotel.common.utils;

import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpsRequest {

    public static JSONObject doGet(String urlStr) throws IOException {
        HttpURLConnection connection = null;
        BufferedReader in = null;
        String result = "";
        URL url = new URL(urlStr);
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("accept", "*/*");
        connection.setRequestProperty("connection", "Keep-Alive");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
        String line;
        while ((line = in.readLine()) != null) {
            result += line;
        }
        connection.disconnect();
        JSONObject jsonRes = JSONObject.parseObject(result);
        return jsonRes;
    }
}
