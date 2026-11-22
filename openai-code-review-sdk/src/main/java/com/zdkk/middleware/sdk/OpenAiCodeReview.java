package com.zdkk.middleware.sdk;

import java.io.*;

/**
 * @author zdkk
 * @description
 * @create 2026-11-22 21:01
 */
public class OpenAiCodeReview {
    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("测试执行");

        // 1. 代码检出
        ProcessBuilder processBuilder = new ProcessBuilder("git", "diff", "HEAD~1", "HEAD");
        processBuilder.directory(new File("."));

        Process process = processBuilder.start();

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        StringBuilder diffCode = new StringBuilder();
        while ((line = bufferedReader.readLine()) != null) {
            diffCode.append(line).append("\n");
        }
        int exitCode = process.waitFor();
        System.out.println("Exit with Code: " + exitCode);
        System.out.println("Diff Code: " + diffCode);
    }
}
