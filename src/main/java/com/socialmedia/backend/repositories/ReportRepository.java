package com.socialmedia.backend.repositories;

import com.socialmedia.backend.entities.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {

    boolean existsByPostPostIdAndReporterUserId(Long postId, Long reporterId);
}