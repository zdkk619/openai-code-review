package com.zdkk.middleware.sdk.types.util;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * @author zdkk
 * @description
 * @create 2026-11-24 1:34
 */
public class WXTemplateUtils {
    private static final String urlTemplate = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=%s";
    public static String sendTemplateMessage(String accessToken, String jsonBody) {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(String.format(urlTemplate, accessToken));
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; utf-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonBody.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            try (InputStream in = connection.getInputStream()) {
                return new String(in.readAllBytes(), StandardCharsets.UTF_8);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}
