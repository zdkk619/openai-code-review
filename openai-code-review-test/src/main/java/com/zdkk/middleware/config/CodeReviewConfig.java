package com.zdkk.middleware.config;

import com.zdkk.middleware.sdk.OpenAiCodeReview;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zdkk
 * @description
 * @create 2026-11-23 16:25
 */
@Configuration
public class CodeReviewConfig {

    @Bean
    public ChatClient chatClient(ChatClient.Builder builder) {
        return builder.build();
    }

    @Bean
    public OpenAiCodeReview openAiCodeReview(ChatClient chatClient) {
        return new OpenAiCodeReview(chatClient);
    }
}
