package com.collabspace.backend.dto.response;

import com.collabspace.backend.entity.ProjectMemberRole;

import java.time.LocalDateTime;

public class ProjectMemberResponse {

    private Long id;
    private UserResponse user;
    private ProjectMemberRole role;
    private LocalDateTime joinedAt;

    public ProjectMemberResponse() {}

    public ProjectMemberResponse(Long id, UserResponse user, ProjectMemberRole role, LocalDateTime joinedAt) {
        this.id = id;
        this.user = user;
        this.role = role;
        this.joinedAt = joinedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(LocalDateTime joinedAt) {
        this.joinedAt = joinedAt;
    }

    public ProjectMemberRole getRole() {
        return role;
    }

    public void setRole(ProjectMemberRole role) {
        this.role = role;
    }

    public UserResponse getUser() {
        return user;
    }

    public void setUser(UserResponse user) {
        this.user = user;
    }
}
