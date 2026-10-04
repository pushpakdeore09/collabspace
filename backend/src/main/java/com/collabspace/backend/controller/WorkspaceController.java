package com.collabspace.backend.controller;

import com.collabspace.backend.dto.request.WorkspaceRequest;
import com.collabspace.backend.dto.response.WorkspaceResponse;
import com.collabspace.backend.service.WorkspaceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/workspaces")
public class WorkspaceController {

    private final WorkspaceService workspaceService;

    public WorkspaceController(WorkspaceService workspaceService) {
        this.workspaceService = workspaceService;
    }

    @PostMapping
    public ResponseEntity<WorkspaceResponse> createWorkspace(@RequestBody WorkspaceRequest request, Authentication authentication) {
        WorkspaceResponse response = workspaceService.createWorkspace(request, authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<WorkspaceResponse>> getMyWorkspaces(Authentication authentication) {
        List<WorkspaceResponse> workspaceResponses = workspaceService.getMyWorkspaces(authentication.getName());
        return ResponseEntity.ok(workspaceResponses);
    }

    @GetMapping("/{workspaceId}")
    public ResponseEntity<WorkspaceResponse> getWorkspace(@PathVariable Long workspaceId, Authentication authentication) {
        WorkspaceResponse response = workspaceService.getWorkspace(workspaceId, authentication.getName());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{workspaceId}")
    public ResponseEntity<WorkspaceResponse> updateWorkspace(@PathVariable Long workspaceId, @RequestBody WorkspaceRequest request,  Authentication authentication) {
        WorkspaceResponse response = workspaceService.updateWorkspace(workspaceId, request, authentication.getName());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{workspaceId}")
    public ResponseEntity<Void> deleteWorkspace(@PathVariable Long workspaceId, Authentication authentication) {
        workspaceService.deleteWorkspace(workspaceId, authentication.getName());
        return ResponseEntity.noContent().build();
    }
}
