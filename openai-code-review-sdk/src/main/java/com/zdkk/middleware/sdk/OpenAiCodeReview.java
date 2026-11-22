package com.zdkk.middleware.sdk;

import ai.z.openapi.ZhipuAiClient;
import ai.z.openapi.service.model.ChatCompletionCreateParams;
import ai.z.openapi.service.model.ChatCompletionResponse;
import ai.z.openapi.service.model.ChatMessage;
import ai.z.openapi.service.model.ChatMessageRole;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * @author zdkk
 * @description
 * @create 2026-11-22 21:01
 */
public class OpenAiCodeReview {
    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("测试执行");

        // 1. 代码检出
        ProcessBuilder processBuilder = new ProcessBuilder("git", "diff", "HEAD~1", "HEAD");
        processBuilder.directory(new File("."));
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8));
        String line;
        StringBuilder diffCode = new StringBuilder();
        while ((line = bufferedReader.readLine()) != null) {
            diffCode.append(line).append("\n");
        }
        int exitCode = process.waitFor();
        System.out.println("Exit with Code: " + exitCode);
        System.out.println("Diff Code: " + diffCode);

        // 2. llm 代码评审
        ChatMessage review = llmCodeReview(diffCode.toString());
        System.out.println("Review Result:\n" + review);
        bufferedReader.close();
    }

    public static ChatMessage llmCodeReview(String diffCode) {
        String apiKey = System.getenv("ZHIPU_API_KEY");
        // 初始化客户端
        ZhipuAiClient client = ZhipuAiClient.builder().ofZHIPU()
                .apiKey(apiKey)
                .build();

        // 创建聊天完成请求
        ChatCompletionCreateParams request = ChatCompletionCreateParams.builder()
                .model("glm-5.3-flash")
                .messages(Arrays.asList(
                        ChatMessage.builder()
                                .role(ChatMessageRole.SYSTEM.value())
                                .content("你是一个高级编程架构师，精通各类场景方案、架构设计和编程语言。")
                                .build(),
                        ChatMessage.builder()
                                .role(ChatMessageRole.USER.value())
                                .content("请您根据git diff记录，对代码做出评审。代码为: " + diffCode)
                                .build()
                ))
                .stream(false)
                .temperature(0.6f)
                .maxTokens(1024)
                .build();

        // 发送请求
        ChatCompletionResponse response = client.chat().createChatCompletion(request);
        return response.getData().getChoices().get(0).getMessage();
    }
}
