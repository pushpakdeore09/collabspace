package com.collabspace.backend.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "folder")
public class Folder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "folder")
    private List<File> files = new ArrayList<>();

    @ManyToOne
    @JoinColumn(
            name = "project_id",
            foreignKey = @ForeignKey(name = "fk_folder_project")
    )
    private Project project;

    @ManyToOne
    @JoinColumn(
            name = "user_id",
            foreignKey = @ForeignKey(name = "fk_folder_user")
    )
    private User user;

    @ManyToOne
    @JoinColumn(
            name = "parent_folder_id",
            foreignKey = @ForeignKey(name = "fk_folder_parent")
    )
    private Folder parentFolder;

    @OneToMany(mappedBy = "parentFolder")
    private List<Folder> childFolders = new ArrayList<>();
}
