package com.br.buggerFixer.controller;

import com.br.buggerFixer.model.BugFixRequest;
import com.br.buggerFixer.service.BugFixService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bugfix")
public class BugFixController {

    private final BugFixService bugFixService;

    public BugFixController(BugFixService bugFixService) {
        this.bugFixService = bugFixService;
    }

    @PostMapping
    public ResponseEntity<String> fixBug(@RequestBody BugFixRequest request) {
        try {
            String result = bugFixService.fixBug(request);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao corrigir bug: " + e.getMessage());
        }
    }
}