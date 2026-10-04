package com.collabspace.backend.service;

import com.collabspace.backend.dto.request.WorkspaceRequest;
import com.collabspace.backend.dto.response.UserResponse;
import com.collabspace.backend.dto.response.WorkspaceResponse;
import com.collabspace.backend.entity.User;
import com.collabspace.backend.entity.Workspace;
import com.collabspace.backend.exception.AccessDeniedException;
import com.collabspace.backend.exception.BadRequestException;
import com.collabspace.backend.exception.ResourceNotFoundException;
import com.collabspace.backend.repository.UserRepository;
import com.collabspace.backend.repository.WorkspaceRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class WorkspaceService {

    private final UserRepository userRepository;
    private final WorkspaceRepository workspaceRepository;

    public WorkspaceService(UserRepository userRepository, WorkspaceRepository workspaceRepository) {
        this.userRepository = userRepository;
        this.workspaceRepository = workspaceRepository;
    }

    public WorkspaceResponse createWorkspace(WorkspaceRequest request, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new BadRequestException("User not found")
                );

        Workspace workspace = new Workspace();
        workspace.setName(request.getName());
        workspace.setDescription(request.getDescription());
        workspace.setOwner(user);

        Workspace savedWorkspace = workspaceRepository.save(workspace);

        return new WorkspaceResponse(savedWorkspace.getId(), savedWorkspace.getName(), savedWorkspace.getDescription(),
                new UserResponse(savedWorkspace.getOwner().getId(), savedWorkspace.getOwner().getName(), savedWorkspace.getOwner().getEmail())
        );
    }

    public List<WorkspaceResponse> getMyWorkspaces(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new BadRequestException("User not found")
                );

        List<Workspace> workspaces = workspaceRepository.findByOwnerId(user.getId());
        return workspaces.stream()
                .map(WorkspaceResponse::new)
                .toList();
    }

    public WorkspaceResponse getWorkspace(Long workspaceId, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new BadRequestException("User not found")
                );

        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Workspace not found with id: " + workspaceId
                        )
                );

        if(!Objects.equals(user.getId(), workspace.getOwner().getId())) {
            throw new AccessDeniedException("You do not have access to this workspace.");
        }

        return new WorkspaceResponse(
                workspace.getId(), workspace.getName(), workspace.getDescription(),
                new UserResponse(workspace.getOwner().getId(), workspace.getOwner().getName(), workspace.getOwner().getEmail())
        );
    }

    public WorkspaceResponse updateWorkspace(Long workspaceId, WorkspaceRequest request, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new BadRequestException("User not found")
                );

        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Workspace not found with id: " + workspaceId
                        )
                );

        if(!Objects.equals(user.getId(), workspace.getOwner().getId())) {
            throw new AccessDeniedException("You do not have access to this workspace.");

        }

        workspace.setName(request.getName());
        workspace.setDescription(request.getDescription());
        Workspace updatedWorkspace = workspaceRepository.save(workspace);

        return new WorkspaceResponse(
                updatedWorkspace.getId(), updatedWorkspace.getName(), updatedWorkspace.getDescription(),
                new UserResponse(updatedWorkspace.getOwner().getId(), updatedWorkspace.getOwner().getName(), updatedWorkspace.getOwner().getEmail())
        );
    }

    public void deleteWorkspace(Long workspaceId, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new BadRequestException("User not found")
                );

        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Workspace not found with id: " + workspaceId
                        )
                );
        if(!Objects.equals(user.getId(), workspace.getOwner().getId())) {
            throw new AccessDeniedException("You do not have access to this workspace.");
        }

        workspaceRepository.delete(workspace);
    }
}
