package com.zdkk.middleware.sdk.infrastructure.openai;

import org.springframework.ai.chat.prompt.Prompt;

import java.net.MalformedURLException;

/**
 * @author zdkk
 * @description
 * @create 2026-11-24 0:18
 */
public interface IOpenAI {
    String chatCompletionSync(String userPrompt);
}
