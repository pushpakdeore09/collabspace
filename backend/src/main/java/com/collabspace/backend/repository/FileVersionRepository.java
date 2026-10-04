package com.collabspace.backend.repository;

import com.collabspace.backend.entity.FileVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileVersionRepository extends JpaRepository<FileVersion, Long> {
}
