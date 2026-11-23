package com.zdkk.middleware.sdk.infrastructure.openai.impl;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionCreateParams;
import com.zdkk.middleware.sdk.infrastructure.openai.IOpenAI;

/**
 * @author zdkk
 * @description
 * @create 2026-11-24 0:21
 */
public class OpenAI implements IOpenAI {
    private final OpenAIClient openAIClient;
    private final String modelName;

    public OpenAI(String apiHost, String apiKey, String modelName) {
        this.openAIClient = OpenAIOkHttpClient.builder()
                .baseUrl(apiHost)
                .apiKey(apiKey)
                .build();
        this.modelName = modelName;
    }

    @Override
    public String chatCompletionSync(String prompt) {
        ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
                .model(modelName)
                .addUserMessage(prompt)
                .build();
        ChatCompletion chatCompletion = openAIClient.chat().completions().create(params);
        return chatCompletion.choices().get(0).message().content().orElse("无返回结果");
    }
}
