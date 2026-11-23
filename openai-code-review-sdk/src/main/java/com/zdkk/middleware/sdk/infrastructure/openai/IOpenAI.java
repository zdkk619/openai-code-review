package com.zdkk.middleware.sdk.infrastructure.openai;

/**
 * @author zdkk
 * @description
 * @create 2026-11-24 0:18
 */
public interface IOpenAI {
    String chatCompletionSync(String userPrompt);
}
