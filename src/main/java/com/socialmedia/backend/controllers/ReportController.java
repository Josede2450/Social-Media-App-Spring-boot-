package com.socialmedia.backend.controllers;

import com.socialmedia.backend.services.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts/{postId}/report")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @PostMapping
    public ResponseEntity<Void> report(
            @PathVariable Long postId,
            @RequestParam String reason) {

        reportService.reportPost(postId, reason);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}