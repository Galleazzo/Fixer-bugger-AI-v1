package com.br.buggerFixer.utils;

import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;

public class GitHubUtils {
    @Value("${githubToken}")
    private static String gitHubToken;

    public static void createPullRequest(String githubUrl, String head, String base, String title, String body) throws IOException {
        GitHub github = new GitHubBuilder().withOAuthToken(gitHubToken).build();

        String repoPath = githubUrl.replace("https://github.com/", "").replace(".git", "");
        GHRepository repo = github.getRepository(repoPath);

        repo.createPullRequest(title, head, base, body);
    }
}
