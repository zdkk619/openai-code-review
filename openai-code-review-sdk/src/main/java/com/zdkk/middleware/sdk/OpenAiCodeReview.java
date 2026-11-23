package com.zdkk.middleware.sdk;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

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

    private String llmCodeReview(String diffCode) {
        return chatClient.prompt()
                .system("你是一个高级编程架构师，精通各类场景方案、架构设计和编程语言。")
                .user("请您根据git diff记录，对代码做出评审。代码为: " + diffCode)
                .call()
                .content();
    }

    public String run(ApplicationArguments args) throws Exception {
        System.out.println("代码评审开始执行");
        String token = System.getenv("GITHUB_TOKEN");
        if (token == null || token.isEmpty()) {
            throw new RuntimeException("请设置 GITHUB_TOKEN 环境变量");
        }

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
        String log = llmCodeReview(diffCode.toString());
        return writeLog(token, log);
    }

    private String writeLog(String token, String log) throws GitAPIException {
        String uri = "https://github.com/zdkk619/openai-code-review-log.git";
        Git git = Git.cloneRepository()
                .setURI(uri)
                .setDirectory(new File("repo"))
                .setCredentialsProvider(new UsernamePasswordCredentialsProvider(token, ""))
                .call();

        String dateFolderName = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        File dateFolder = new File("repo/" + dateFolderName);
        if (!dateFolder.exists()) {
            dateFolder.mkdirs();
        }
        File logFile = new File(dateFolder, UUID.randomUUID().toString() + "-log.txt");
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(logFile))) {
            bufferedWriter.write(log);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        git.add().addFilepattern(dateFolderName + "/" + logFile.getName()).call();
        git.commit().setMessage("Add log file").call();
        git.push().setCredentialsProvider(new UsernamePasswordCredentialsProvider(token, "")).call();
        git.close();
        return "https://github.com/zdkk619/openai-code-review-log/blob/main/" + dateFolderName + "/" + logFile.getName();
    }
}
