package com.zdkk.middleware.sdk.test;

import ai.z.openapi.ZhipuAiClient;
import ai.z.openapi.service.model.ChatCompletionCreateParams;
import ai.z.openapi.service.model.ChatCompletionResponse;
import ai.z.openapi.service.model.ChatMessage;
import ai.z.openapi.service.model.ChatMessageRole;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;

/**
 * @author zdkk
 * @description
 * @create 2026-11-22 21:05
 */
@Slf4j
public class ApiTest {

    @Test
    public void test_api() throws IOException {
        String apiKey = System.getenv("ZHIPU_API_KEY");
        // 初始化客户端
        ZhipuAiClient client = ZhipuAiClient.builder().ofZHIPU()
                .apiKey(apiKey)
                .build();

        // 创建聊天完成请求
        ChatCompletionCreateParams request = ChatCompletionCreateParams.builder()
                .model("glm-5.3-flash")
                .messages(Collections.singletonList(
                        ChatMessage.builder()
                                .role(ChatMessageRole.USER.value())
                                .content("你好，介绍下你自己?")
                                .build()
                ))
                .stream(false)
                .temperature(0.6f)
                .maxTokens(1024)
                .build();

        // 发送请求
        ChatCompletionResponse response = client.chat().createChatCompletion(request);
        ChatMessage message = response.getData().getChoices().get(0).getMessage();

        // 获取回复
        log.info("{}", message);
    }

}
