package com.wave.backend.workspaceinvite.repository;

import com.wave.backend.user.entity.User;
import com.wave.backend.workspace.entity.Workspace;
import com.wave.backend.workspaceinvite.entity.WorkspaceInvitation;
import com.wave.backend.workspaceinvite.entity.WorkspaceInvitationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WorkspaceInvitationRepository
        extends JpaRepository<WorkspaceInvitation, Long> {

    Optional<WorkspaceInvitation> findByWorkspaceAndInviteeAndStatus(
            Workspace workspace,
            User invitee,
            WorkspaceInvitationStatus status
    );

    List<WorkspaceInvitation> findByInviteeOrderByCreatedAtDesc(
            User invitee
    );

    List<WorkspaceInvitation> findByWorkspaceOrderByCreatedAtDesc(
            Workspace workspace
    );

}