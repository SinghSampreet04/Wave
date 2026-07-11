package com.wave.backend.workspace.service;

import com.wave.backend.common.util.SecurityUtil;
import com.wave.backend.user.entity.User;
import com.wave.backend.user.repository.UserRepository;
import com.wave.backend.workspace.dto.CreateWorkspaceRequest;
import com.wave.backend.workspace.dto.WorkspaceResponse;
import com.wave.backend.workspace.entity.Workspace;
import com.wave.backend.workspace.entity.WorkspaceMember;
import com.wave.backend.workspace.entity.WorkspaceRole;
import com.wave.backend.workspace.repository.WorkspaceMemberRepository;
import com.wave.backend.workspace.repository.WorkspaceRepository;
import org.springframework.stereotype.Service;

@Service
public class WorkspaceService {

    private final WorkspaceRepository workspaceRepository;
    private final WorkspaceMemberRepository workspaceMemberRepository;
    private final UserRepository userRepository;

    public WorkspaceService(
            WorkspaceRepository workspaceRepository,
            WorkspaceMemberRepository workspaceMemberRepository,
            UserRepository userRepository
    ) {
        this.workspaceRepository = workspaceRepository;
        this.workspaceMemberRepository = workspaceMemberRepository;
        this.userRepository = userRepository;
    }

    public WorkspaceResponse createWorkspace(CreateWorkspaceRequest request) {

        String email = SecurityUtil.getCurrentUserEmail();

        User owner = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found."));

        Workspace workspace = new Workspace();

        workspace.setName(request.getName());
        workspace.setDescription(request.getDescription());
        workspace.setOwner(owner);

        workspace = workspaceRepository.save(workspace);

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
}