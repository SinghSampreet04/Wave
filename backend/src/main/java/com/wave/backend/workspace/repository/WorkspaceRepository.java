package com.wave.backend.workspace.repository;

import com.wave.backend.user.entity.User;
import com.wave.backend.workspace.entity.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkspaceRepository extends JpaRepository<Workspace, Long> {

    List<Workspace> findByOwner(User owner);

}