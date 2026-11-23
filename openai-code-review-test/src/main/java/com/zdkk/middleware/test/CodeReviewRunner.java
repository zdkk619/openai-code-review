package com.zdkk.middleware.test;

import com.zdkk.middleware.sdk.OpenAiCodeReview;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @author zdkk
 * @description
 * @create 2026-11-23 16:16
 */
@Component
public class CodeReviewRunner implements ApplicationRunner {
    private final OpenAiCodeReview openAiCodeReview;

    public CodeReviewRunner(OpenAiCodeReview openAiCodeReview) {
        this.openAiCodeReview = openAiCodeReview;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        String result = openAiCodeReview.run(args);
        System.out.println("Code Review Result: " + result);
    }
}
