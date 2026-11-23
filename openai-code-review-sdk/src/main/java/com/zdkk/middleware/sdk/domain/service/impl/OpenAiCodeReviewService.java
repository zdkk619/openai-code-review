package com.zdkk.middleware.sdk.domain.service.impl;

import com.zdkk.middleware.sdk.domain.service.AbstractOpenAiCodeReviewService;
import com.zdkk.middleware.sdk.infrastructure.git.GitCommand;
import com.zdkk.middleware.sdk.infrastructure.openai.IOpenAI;
import com.zdkk.middleware.sdk.infrastructure.weixin.WeiXin;
import com.zdkk.middleware.sdk.infrastructure.weixin.dto.TemplateMessageDTO;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zdkk
 * @description
 * @create 2026-11-24 1:47
 */
public class OpenAiCodeReviewService extends AbstractOpenAiCodeReviewService {
    private final String codeReviewTemplate = "你是一个高级架构师，精通各类场景方案，架构设计和编程语言。请详细评审代码，并给出修改建议。代码如下：\n\n%s";
    public OpenAiCodeReviewService(GitCommand gitCommand, WeiXin weiXin, IOpenAI openAI) {
        super(gitCommand, weiXin, openAI);
    }

    // 项目名称：{{repo_name.DATA}} 分支：{{branch_name.DATA}} 作者：{{commit_author.DATA}} 说明：{{commit_message.DATA}}
    @Override
    protected void sendMessage(String logUrl) {
        Map<String, Map<String, String>> data = new HashMap<>();
        TemplateMessageDTO.put(data, TemplateMessageDTO.TemplateKey.REPO_NAME, gitCommand.getProject());
        TemplateMessageDTO.put(data, TemplateMessageDTO.TemplateKey.BRANCH_NAME, gitCommand.getBranch());
        TemplateMessageDTO.put(data, TemplateMessageDTO.TemplateKey.COMMIT_AUTHOR, gitCommand.getAuthor());
        TemplateMessageDTO.put(data, TemplateMessageDTO.TemplateKey.COMMIT_MESSAGE, gitCommand.getMessage());
        weiXin.sendTemplateMessage(logUrl, data);
    }

    @Override
    protected String writeReviewLog(String review) throws GitAPIException, IOException {
        return gitCommand.commitAndPush(review);
    }

    @Override
    protected String getDiffCode() throws IOException, InterruptedException {
        return gitCommand.diff();
    }

    @Override
    protected String codeReview(String diffCode) {
        return openAI.chatCompletionSync(String.format(codeReviewTemplate, diffCode));
    }
}
