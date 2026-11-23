package com.zdkk.middleware.sdk.domain.service;

import com.zdkk.middleware.sdk.infrastructure.git.GitCommand;
import com.zdkk.middleware.sdk.infrastructure.openai.IOpenAI;
import com.zdkk.middleware.sdk.infrastructure.weixin.WeiXin;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author zdkk
 * @description
 * @create 2026-11-24 1:42
 */
public abstract class AbstractOpenAiCodeReviewService implements IOpenAiCodeReviewService {
    private Logger logger = LoggerFactory.getLogger(AbstractOpenAiCodeReviewService.class);

    protected final GitCommand gitCommand;
    protected final WeiXin weiXin;
    protected final IOpenAI openAI;

    public AbstractOpenAiCodeReviewService(GitCommand gitCommand, WeiXin weiXin, IOpenAI openAI) {
        this.gitCommand = gitCommand;
        this.weiXin = weiXin;
        this.openAI = openAI;
    }


    @Override
    public void exec() {
        try {
            // 1. 获取提交代码
            String diffCode = getDiffCode();
            // 2. 开始评审代码
            String recommend = codeReview(diffCode);
            // 3. 记录评审结果；返回日志地址
            String logUrl = writeReviewLog(recommend);
            // 4. 发送消息通知；日志地址、通知的内容
            sendMessage(logUrl);
        } catch (Exception e) {
            logger.error("openai-code-review error", e);
        }
    }

    protected abstract void sendMessage(String logUrl);

    protected abstract String writeReviewLog(String review) throws GitAPIException, IOException;

    protected abstract String getDiffCode() throws IOException, InterruptedException;

    protected abstract String codeReview(String diffCode);
}
