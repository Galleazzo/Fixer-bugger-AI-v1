package com.br.buggerFixer.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GitHubTokenProvider {

    @Value("${githubToken}")
    private String githubToken;

    public String getToken() {
        return githubToken;
    }
}
