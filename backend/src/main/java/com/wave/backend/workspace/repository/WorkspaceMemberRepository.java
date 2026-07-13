package com.wave.backend.workspace.repository;

import com.wave.backend.user.entity.User;
import com.wave.backend.workspace.entity.Workspace;
import com.wave.backend.workspace.entity.WorkspaceMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WorkspaceMemberRepository
        extends JpaRepository<WorkspaceMember, Long> {

    List<WorkspaceMember> findByWorkspace(
            Workspace workspace
    );

    List<WorkspaceMember> findByUser(
            User user
    );

    Optional<WorkspaceMember> findByWorkspaceAndUser(
            Workspace workspace,
            User user
    );

    boolean existsByWorkspaceAndUser(
            Workspace workspace,
            User user
    );

}