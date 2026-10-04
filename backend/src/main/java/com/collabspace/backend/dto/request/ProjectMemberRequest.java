package com.collabspace.backend.dto.request;

import com.collabspace.backend.entity.ProjectMemberRole;

public class ProjectMemberRequest {
    private String email;
    private ProjectMemberRole role;

    public ProjectMemberRequest() {}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ProjectMemberRole getRole() {
        return role;
    }

    public void setRole(ProjectMemberRole role) {
        this.role = role;
    }
}
