package com.zdkk.middleware.sdk.infrastructure.git;

import com.zdkk.middleware.sdk.types.util.RandomStringUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * @author zdkk
 * @description
 * @create 2026-11-23 23:43
 */
public class GitCommand {
    private final Logger logger = LoggerFactory.getLogger(GitCommand.class);

    // https://github.com/zdkk619/openai-code-review-log
    private final String githubReviewLogUri;

    private final String githubToken;

    private final String project;

    private final String branch;

    private final String author;

    private final String message;

    public GitCommand(String githubReviewLogUri, String githubToken, String project, String branch, String author, String message) {
        this.githubReviewLogUri = githubReviewLogUri;
        this.githubToken = githubToken;
        this.project = project;
        this.branch = branch;
        this.author = author;
        this.message = message;
    }

    public String diff() throws IOException, InterruptedException {
        String latestCommitHash = getLatestCommitHash();

        ProcessBuilder processBuilder = new ProcessBuilder("git", "diff", latestCommitHash + "^", latestCommitHash);
        processBuilder.directory(new File("."));
        Process process = processBuilder.start();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        bufferedReader.close();
        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new RuntimeException("Git diff command failed with exit code " + exitCode);
        }
        return sb.toString();
    }

    private String getLatestCommitHash() throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder("git", "log", "-1", "--pretty=format:%H");
        processBuilder.directory(new File("."));
        Process process = processBuilder.start();
        String latestCommitHash = null;
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8))) {
            latestCommitHash = bufferedReader.readLine().trim();
        }

        int exitCode = process.waitFor();
        if (exitCode != 0 || latestCommitHash.isEmpty()) {
            throw new RuntimeException("Get latest commit hash failed with exit code " + exitCode);
        }
        return latestCommitHash;
    }

    public String commitAndPush(String review) throws GitAPIException, IOException {
        Git git = Git.cloneRepository()
                .setURI(githubReviewLogUri + ".git")
                .setDirectory(new File("repo"))
                .setCredentialsProvider(new UsernamePasswordCredentialsProvider(githubToken, ""))
                .call();

        String dateFolderName = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        File dateFolder = new File("repo/" + dateFolderName);
        if (!dateFolder.exists()) {
            dateFolder.mkdirs();
        }

        String logFileName = project + "-" + branch + "-" + author + System.currentTimeMillis() + "-" + RandomStringUtils.randomNumeric(4) + ".md";
        File logFile = new File(dateFolder, logFileName);
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(logFile, StandardCharsets.UTF_8))) {
            bufferedWriter.write(review);
        }
        git.add().addFilepattern(dateFolderName + "/" + logFileName).call();
        git.commit().setMessage("Add code review file " + logFileName).call();
        git.push().setCredentialsProvider(new UsernamePasswordCredentialsProvider(githubToken, "")).call();
        git.close();

        logger.info("openai-code-review git commit and push done! {}", logFileName);

        return githubReviewLogUri + "/blob/master/" + dateFolderName + "/" + logFileName;
    }

    public String getProject() {
        return project;
    }

    public String getBranch() {
        return branch;
    }

    public String getAuthor() {
        return author;
    }

    public String getMessage() {
        return message;
    }
}
