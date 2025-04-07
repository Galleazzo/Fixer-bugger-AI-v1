package com.br.buggerFixer.utils;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.RefSpec;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class GitUtils {

    @Value("${githubToken}")
    private static String gitHubToken;

    public static void createBranchAndPush(File repoDir, String branchName, String changedFilePath) throws Exception {
        try (Git git = Git.open(repoDir)) {
            git.checkout().setCreateBranch(true).setName(branchName).call();
            git.add().addFilepattern(".").call();
            git.commit().setMessage("fix: apply AI-generated fix").call();
            git.push().setRemote("origin").setRefSpecs(new RefSpec(branchName + ":" + branchName))
                    .setCredentialsProvider(new UsernamePasswordCredentialsProvider(gitHubToken, ""))
                    .call();
        }
    }

    public static File cloneRepository(String githubUrl) throws IOException, GitAPIException {
        File tempDir = Files.createTempDirectory("repo-clone").toFile();
        Git.cloneRepository()
                .setURI(githubUrl)
                .setDirectory(tempDir)
                .call();
        return tempDir;
    }
}
