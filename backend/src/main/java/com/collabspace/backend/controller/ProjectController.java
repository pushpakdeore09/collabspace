package com.collabspace.backend.controller;

import com.collabspace.backend.dto.request.ProjectMemberRequest;
import com.collabspace.backend.dto.request.ProjectRequest;
import com.collabspace.backend.dto.response.ProjectMemberResponse;
import com.collabspace.backend.dto.response.ProjectResponse;
import com.collabspace.backend.service.ProjectService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping
    public ResponseEntity<ProjectResponse> createProject(@RequestBody ProjectRequest request, Authentication authentication) {
        ProjectResponse response = projectService.createProject(request, authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ProjectResponse>> getMyProjects(Authentication authentication) {
        List<ProjectResponse> projectResponses = projectService.getMyProjects(authentication.getName());
        return ResponseEntity.ok(projectResponses);
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<ProjectResponse> getProject(@PathVariable Long projectId, Authentication authentication) {
        ProjectResponse response = projectService.getProject(projectId, authentication.getName());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{projectId}")
    public ResponseEntity<ProjectResponse> updateProject(@PathVariable Long projectId, @RequestBody ProjectRequest request, Authentication authentication) {
        ProjectResponse response = projectService.updateProject(projectId, request, authentication.getName());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{projectId}/members")
    public ResponseEntity<ProjectMemberResponse> addMember(@PathVariable Long projectId, @RequestBody ProjectMemberRequest request, Authentication authentication) {
        ProjectMemberResponse response = projectService.addMember(projectId, request, authentication.getName());
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{projectId}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long projectId, Authentication authentication) {
        projectService.deleteProject(projectId, authentication.getName());
        return ResponseEntity.noContent().build();
    }

}
