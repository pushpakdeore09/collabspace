package com.collabspace.backend.dto.request;

public class ProjectRequest {

    private String name;
    private String description;
    private Long workspaceId;

    public ProjectRequest() {}

    public ProjectRequest(String name, String description, Long workspaceId) {
        this.name = name;
        this.description = description;
        this.workspaceId = workspaceId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getWorkspaceId() {
        return workspaceId;
    }

    public void setWorkspaceId(Long workspaceId) {
        this.workspaceId = workspaceId;
    }
}
