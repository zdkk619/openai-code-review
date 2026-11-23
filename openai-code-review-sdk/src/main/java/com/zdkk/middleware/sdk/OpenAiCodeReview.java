package com.zdkk.middleware.sdk;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * @author zdkk
 * @description
 * @create 2026-11-22 21:01
 */
public class OpenAiCodeReview {
    private final ChatClient chatClient;

    public OpenAiCodeReview(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    public String llmCodeReview(String diffCode) {
        return chatClient.prompt()
                .system("你是一个高级编程架构师，精通各类场景方案、架构设计和编程语言。")
                .user("请您根据git diff记录，对代码做出评审。代码为: " + diffCode)
                .call()
                .content();
    }

    public String run(ApplicationArguments args) throws Exception {
        System.out.println("代码评审开始执行");

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
        bufferedReader.close();
        process.destroy();
        System.out.println("Exit with Code: " + exitCode);
        System.out.println("Diff Code: " + diffCode);

        // 2. llm 代码评审
        return llmCodeReview(diffCode.toString());
    }
}
