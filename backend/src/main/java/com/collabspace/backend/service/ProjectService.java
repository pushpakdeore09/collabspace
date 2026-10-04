package com.collabspace.backend.service;

import com.collabspace.backend.dto.request.ProjectMemberRequest;
import com.collabspace.backend.dto.request.ProjectRequest;
import com.collabspace.backend.dto.response.ProjectMemberResponse;
import com.collabspace.backend.dto.response.ProjectResponse;
import com.collabspace.backend.dto.response.UserResponse;
import com.collabspace.backend.entity.*;
import com.collabspace.backend.exception.AccessDeniedException;
import com.collabspace.backend.exception.BadRequestException;
import com.collabspace.backend.exception.ConflictException;
import com.collabspace.backend.exception.ResourceNotFoundException;
import com.collabspace.backend.repository.ProjectMemberRepository;
import com.collabspace.backend.repository.ProjectRepository;
import com.collabspace.backend.repository.UserRepository;
import com.collabspace.backend.repository.WorkspaceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class ProjectService {

    private final UserRepository userRepository;
    private final WorkspaceRepository workspaceRepository;
    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;

    public ProjectService(UserRepository userRepository, WorkspaceRepository workspaceRepository, ProjectRepository projectRepository, ProjectMemberRepository projectMemberRepository) {
        this.userRepository = userRepository;
        this.workspaceRepository = workspaceRepository;
        this.projectRepository = projectRepository;
        this.projectMemberRepository = projectMemberRepository;
    }

    @Transactional
    public ProjectResponse createProject(ProjectRequest request, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new BadRequestException("User not found")
                );

        Workspace workspace = workspaceRepository.findById(request.getWorkspaceId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Workspace not found with id: " + request.getWorkspaceId()
                        )
                );

        if(!Objects.equals(workspace.getOwner().getId(), user.getId())) {
            throw new AccessDeniedException("You do not have access to this workspace.");
        }

        Project project = new Project();
        project.setName(request.getName());
        project.setDescription(request.getDescription());
        project.setCreatedBy(user);
        project.setWorkspace(workspace);
        Project savedProject = projectRepository.save(project);

        ProjectMember member = new ProjectMember();
        member.setUser(user);
        member.setProject(savedProject);
        member.setRole(ProjectMemberRole.OWNER);
        member.setJoinedAt(LocalDateTime.now());
        projectMemberRepository.save(member);

        return new ProjectResponse(
                savedProject.getId(),
                savedProject.getName(),
                savedProject.getDescription(),
                new UserResponse(savedProject.getCreatedBy().getId(), savedProject.getCreatedBy().getName(), savedProject.getCreatedBy().getEmail())
        );
    }

    public List<ProjectResponse> getMyProjects(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new BadRequestException("User not found")
                );
        List<Project> projects = projectRepository.findByCreatedBy(user);
        return projects.stream()
                .map(ProjectResponse::new)
                .toList();
    }

    public ProjectResponse getProject(Long projectId, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new BadRequestException("User not found")
                );

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Project not found with id: " + projectId
                        )
                );

        if(!Objects.equals(user.getId(), project.getCreatedBy().getId())) {
            throw new AccessDeniedException("You do not have access to this project.");
        }

        return new ProjectResponse(
                project.getId(),
                project.getName(),
                project.getDescription(),
                new UserResponse(project.getCreatedBy().getId(), project.getCreatedBy().getName(), project.getCreatedBy().getEmail())
        );
    }

    public ProjectResponse updateProject(Long projectId, ProjectRequest request, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new BadRequestException("User not found")
                );

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Project not found with id: " + projectId
                        )
                );

        if(!Objects.equals(user.getId(), project.getCreatedBy().getId())) {
            throw new AccessDeniedException("You do not have access to this project.");
        }

        project.setName(request.getName());
        project.setDescription(request.getDescription());

        Project updatedProject = projectRepository.save(project);
        return new ProjectResponse(
                updatedProject.getId(), updatedProject.getName(), updatedProject.getDescription(),
                new UserResponse(updatedProject.getCreatedBy().getId(), updatedProject.getCreatedBy().getName(), updatedProject.getCreatedBy().getEmail())
        );
    }

    public ProjectMemberResponse addMember(Long projectId, ProjectMemberRequest request, String email) {
        User owner = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new BadRequestException("Project owner not found")
                );

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Project not found with id: " + projectId
                        )
                );

        if(!Objects.equals(owner.getId(), project.getCreatedBy().getId())) {
            throw new AccessDeniedException("You do not have access to add member");
        }

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new BadRequestException("User not found")
                );

        if(project.getMembers().contains(user)) {
            throw new ConflictException("User is already a member");
        }

        ProjectMember member = new ProjectMember();
        member.setUser(user);
        member.setProject(project);
        member.setRole(request.getRole());
        member.setJoinedAt(LocalDateTime.now());

        ProjectMember savedProjectMember = projectMemberRepository.save(member);
        return new ProjectMemberResponse(
                savedProjectMember.getId(),
                new UserResponse(savedProjectMember.getUser().getId(), savedProjectMember.getUser().getName(), savedProjectMember.getUser().getEmail()),
                savedProjectMember.getRole(),
                savedProjectMember.getJoinedAt()
        );
    }

    public void deleteProject(Long projectId, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new BadRequestException("User not found")
                );

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Project not found with id: " + projectId
                        )
                );

        if(!Objects.equals(user.getId(), project.getCreatedBy().getId())) {
            throw new AccessDeniedException("You do not have access to this project.");
        }

        projectRepository.delete(project);
    }
}
