package com.collabspace.backend.dto.response;

import com.collabspace.backend.entity.User;
import com.collabspace.backend.entity.Workspace;

public class WorkspaceResponse {

    private Long id;
    private String name;
    private String description;
    private UserResponse owner;

    public WorkspaceResponse() {}

    public WorkspaceResponse(Long id, String name, String description, UserResponse owner) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.owner = owner;
    }

    public WorkspaceResponse(Workspace workspace) {
        this.id = workspace.getId();
        this.name = workspace.getName();
        this.description = workspace.getDescription();
        if(workspace.getOwner() != null) {
            this.owner = new UserResponse(workspace.getOwner());
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserResponse getOwner() {
        return owner;
    }

    public void setOwner(UserResponse owner) {
        this.owner = owner;
    }
}
