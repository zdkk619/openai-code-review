package com.zdkk.middleware.sdk;

import com.zdkk.middleware.sdk.domain.service.impl.OpenAiCodeReviewService;
import com.zdkk.middleware.sdk.infrastructure.git.GitCommand;
import com.zdkk.middleware.sdk.infrastructure.openai.impl.OpenAI;
import com.zdkk.middleware.sdk.infrastructure.weixin.WeiXin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zdkk
 * @description
 * @create 2026-11-22 21:01
 */
public class OpenAiCodeReview {
    private static Logger logger = LoggerFactory.getLogger(OpenAiCodeReview.class);

    public static void main(String[] args) {
        GitCommand gitCommand = new GitCommand(
                getEnv("GITHUB_REVIEW_LOG_URI"),
                getEnv("GITHUB_TOKEN"),
                getEnv("COMMIT_PROJECT"),
                getEnv("COMMIT_BRANCH"),
                getEnv("COMMIT_AUTHOR"),
                getEnv("COMMIT_MESSAGE")
        );

        WeiXin weiXin = new WeiXin(
                getEnv("WX_APPID"),
                getEnv("WX_APP_SECRET"),
                getEnv("WX_TO_USER"),
                getEnv("WX_TEMPLATE_ID"));

        OpenAI openAI = new OpenAI(
                getEnv("OPENAI_API_HOST"),
                getEnv("OPENAI_API_KEY"),
                getEnv("OPENAI_MODEL_NAME"));

        OpenAiCodeReviewService openAiCodeReviewService = new OpenAiCodeReviewService(gitCommand, weiXin, openAI);
        openAiCodeReviewService.exec();
    }

    private static String getEnv(String key) {
        String value = System.getenv(key);
        if (value == null || value.isEmpty()) {
            throw new RuntimeException("Env " + key + " is not set");
        }
        return value;
    }
}
