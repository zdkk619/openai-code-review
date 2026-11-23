package com.zdkk.middleware.sdk.infrastructure.openai.impl;

import com.zdkk.middleware.sdk.infrastructure.openai.IOpenAI;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;

/**
 * @author zdkk
 * @description
 * @create 2026-11-24 0:21
 */
public class OpenAI implements IOpenAI {
    private final ChatClient chatClient;

    public OpenAI(String apiHost, String apiKey, String modelName) {
        // 1. 构建官方 SDK 的 ChatModel
        OpenAiChatModel chatModel = OpenAiChatModel.builder()
                .options(OpenAiChatOptions.builder()
                        .baseUrl(apiHost)
                        .apiKey(apiKey)
                        .model(modelName)
                        .build())
                .build();

        this.chatClient = ChatClient.builder(chatModel)
                .build();
    }

    @Override
    public String chatCompletionSync(String prompt) {
        return chatClient.prompt(prompt).call().content();
    }
}
