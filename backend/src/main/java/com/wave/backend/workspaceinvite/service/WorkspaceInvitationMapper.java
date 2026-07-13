package com.wave.backend.workspaceinvite.service;

import com.wave.backend.workspaceinvite.dto.WorkspaceInvitationResponse;
import com.wave.backend.workspaceinvite.entity.WorkspaceInvitation;
import org.springframework.stereotype.Component;

@Component
public class WorkspaceInvitationMapper {

    public WorkspaceInvitationResponse toResponse(
            WorkspaceInvitation invitation
    ) {

        return new WorkspaceInvitationResponse(
                invitation.getId(),
                invitation.getWorkspace().getId(),
                invitation.getWorkspace().getName(),
                invitation.getInviter().getId(),
                invitation.getInviter().getUsername(),
                invitation.getInvitee().getId(),
                invitation.getInvitee().getUsername(),
                invitation.getStatus(),
                invitation.getCreatedAt(),
                invitation.getExpiresAt(),
                invitation.getRespondedAt()
        );

    }

}