package com.br.buggerFixer.service;

import com.br.buggerFixer.client.GitHubTokenProvider;
import com.br.buggerFixer.client.OpenAIClient;
import com.br.buggerFixer.model.BugFixRequest;
import com.br.buggerFixer.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class BugFixService {

    private final OpenAIClient openAIClient;

    @Autowired
    private GitHubTokenProvider tokenProvider;

    public BugFixService(OpenAIClient openAIClient) {
        this.openAIClient = openAIClient;
    }

    public String fixBug(BugFixRequest request) throws Exception {
        File repoDir = GitUtils.cloneRepository(request.getGithubUrl());

        MethodFinder.FoundMethod foundMethod = MethodFinder.findMethodInProject(repoDir, request.getMethodName());
        if (foundMethod == null) {
            throw new RuntimeException("Método não encontrado no projeto.");
        }
        String suggestedCode = openAIClient.getSuggestedFix(foundMethod.methodCode, request.getExpectedBehavior());
        MethodReplacer.replaceMethodInFile(new File(foundMethod.filePath), foundMethod.methodCode, suggestedCode);
        JavaFormatterUtil.formatFile(new File(foundMethod.filePath));
        String branchName = "fix/" + request.getMethodName();
        String token = tokenProvider.getToken();
        GitUtils.createBranchAndPush(repoDir, branchName, foundMethod.filePath, token);
        GitHubUtils.createPullRequest(request.getGithubUrl(), branchName, "main", "Fix: corrected " + request.getMethodName(), "This PR contains a suggested fix generated by AI.", token);

        return "Código corrigido com sucesso! Método atualizado no arquivo.";
    }
}
