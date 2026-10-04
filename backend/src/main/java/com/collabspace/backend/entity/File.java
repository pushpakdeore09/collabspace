package com.collabspace.backend.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "files")
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "language", nullable = false)
    private String language;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(
            name = "user_id",
            foreignKey = @ForeignKey(name = "fk_file_user")
    )
    private User user;

    @ManyToOne
    @JoinColumn(
            name = "folder_id",
            foreignKey = @ForeignKey(name = "fk_file_folder")
    )
    private Folder folder;

    @ManyToOne
    @JoinColumn(
            name = "project_id",
            foreignKey = @ForeignKey(name = "fk_file_project")
    )
    private Project project;

    @OneToMany(mappedBy = "file")
    private List<FileVersion> fileVersions = new ArrayList<>();
}

