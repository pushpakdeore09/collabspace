package com.collabspace.backend.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "line_number")
    private Integer lineNumber;

    @Enumerated(EnumType.STRING)
    private CommentStatus status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(
            name = "user_id",
            foreignKey = @ForeignKey(name = "fk_comment_user")
    )
    private User user;

    @ManyToOne
    @JoinColumn(
            name = "file_id",
            foreignKey = @ForeignKey(name = "fk_comment_file")
    )
    private File file;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "parent_comment_id",
            foreignKey = @ForeignKey(name = "fk_comment_parent")
    )
    private Comment parentComment;

    @OneToMany(mappedBy = "parentComment")
    private List<Comment> replies = new ArrayList<>();
}
