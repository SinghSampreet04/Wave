package com.wave.backend.workspace.service;

import com.wave.backend.common.util.SecurityUtil;
import com.wave.backend.exception.UserNotFoundException;
import com.wave.backend.metrics.service.MetricsService;
import com.wave.backend.user.entity.User;
import com.wave.backend.user.repository.UserRepository;
import com.wave.backend.workspace.dto.AddWorkspaceMemberRequest;
import com.wave.backend.workspace.dto.CreateWorkspaceRequest;
import com.wave.backend.workspace.dto.WorkspaceResponse;
import com.wave.backend.workspace.entity.Workspace;
import com.wave.backend.workspace.entity.WorkspaceMember;
import com.wave.backend.workspace.entity.WorkspaceRole;
import com.wave.backend.workspace.repository.WorkspaceMemberRepository;
import com.wave.backend.workspace.repository.WorkspaceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WorkspaceService {

    private final WorkspaceRepository workspaceRepository;
    private final WorkspaceMemberRepository workspaceMemberRepository;
    private final UserRepository userRepository;
    private final MetricsService metricsService;

    public WorkspaceService(
            WorkspaceRepository workspaceRepository,
            WorkspaceMemberRepository workspaceMemberRepository,
            UserRepository userRepository,
            MetricsService metricsService
    ) {
        this.workspaceRepository = workspaceRepository;
        this.workspaceMemberRepository = workspaceMemberRepository;
        this.userRepository = userRepository;
        this.metricsService = metricsService;
    }

    @Transactional
    public WorkspaceResponse createWorkspace(CreateWorkspaceRequest request) {

        String email = SecurityUtil.getCurrentUserEmail();

        User owner = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found."));

        Workspace workspace = new Workspace();

        workspace.setName(request.getName());
        workspace.setDescription(request.getDescription());
        workspace.setOwner(owner);

        workspace = workspaceRepository.save(workspace);

        metricsService.incrementWorkspaceCreated();

        WorkspaceMember member = new WorkspaceMember();
        member.setWorkspace(workspace);
        member.setUser(owner);
        member.setRole(WorkspaceRole.OWNER);

        workspaceMemberRepository.save(member);

        return new WorkspaceResponse(
                workspace.getId(),
                workspace.getName(),
                workspace.getDescription(),
                owner.getId(),
                owner.getUsername()
        );
    }

    @Transactional(readOnly = true)
    public List<WorkspaceResponse> getMyWorkspaces() {

        String email = SecurityUtil.getCurrentUserEmail();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found."));

        List<WorkspaceMember> memberships =
                workspaceMemberRepository.findByUser(user);

        return memberships.stream()
                .map(member -> {

                    Workspace workspace = member.getWorkspace();

                    return new WorkspaceResponse(
                            workspace.getId(),
                            workspace.getName(),
                            workspace.getDescription(),
                            workspace.getOwner().getId(),
                            workspace.getOwner().getUsername()
                    );

                })
                .toList();
    }

    @Transactional
    public void inviteMember(
            Long workspaceId,
            AddWorkspaceMemberRequest request
    ) {

        String email = SecurityUtil.getCurrentUserEmail();

        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found."));

        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Workspace not found."));

        WorkspaceMember ownerMembership =
                workspaceMemberRepository.findByWorkspaceAndUser(
                        workspace,
                        currentUser
                ).orElseThrow(() ->
                        new IllegalArgumentException("You are not a member of this workspace."));

        if (ownerMembership.getRole() != WorkspaceRole.OWNER) {
            throw new IllegalArgumentException(
                    "Only the workspace owner can invite members."
            );
        }

        User invitedUser = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new UserNotFoundException("User not found."));

        if (workspaceMemberRepository.existsByWorkspaceAndUser(
                workspace,
                invitedUser
        )) {
            throw new IllegalArgumentException(
                    "User is already a workspace member."
            );
        }

        WorkspaceMember member = new WorkspaceMember();
        member.setWorkspace(workspace);
        member.setUser(invitedUser);
        member.setRole(WorkspaceRole.MEMBER);

        workspaceMemberRepository.save(member);
    }
}