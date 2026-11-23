package com.zdkk.test;

import com.alibaba.fastjson2.JSON;
import com.zdkk.middleware.sdk.domain.model.Message;
import com.zdkk.middleware.sdk.types.util.WXAccessTokenUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * @author zdkk
 * @description
 * @create 2026-11-23 17:41
 */
@SpringBootTest
public class TestWX {
    String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=%s";
    @Test
    public void test_wx() {
        String urlString = String.format(url, WXAccessTokenUtils.getAccessToken());
        Message message = new Message();
        message.put("project", "test-project");
        message.put("review", "feat: 添加新功能");
        String jsonBody = JSON.toJSONString(message);
        sendPostRequest(urlString, jsonBody);
    }

    private void sendPostRequest(String urlString, String jsonBody) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; utf-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonBody.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            try (Scanner sc = new Scanner(connection.getInputStream(), StandardCharsets.UTF_8)) {
                String response = sc.useDelimiter("\\A").next();
                System.out.println(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
