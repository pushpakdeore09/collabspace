package com.collabspace.backend.dto.response;


import com.collabspace.backend.entity.Project;

public class ProjectResponse {

    private Long id;
    private String name;
    private String description;
    private UserResponse owner;

    public ProjectResponse() {}

    public ProjectResponse(Long id, String name, String description, UserResponse owner) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.owner = owner;
    }

    public ProjectResponse(Project project) {
        this.id = project.getId();
        this.name = project.getName();
        this.description = project.getDescription();
        this.owner = new UserResponse(project.getCreatedBy().getId(), project.getCreatedBy().getName(), project.getCreatedBy().getEmail());
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
