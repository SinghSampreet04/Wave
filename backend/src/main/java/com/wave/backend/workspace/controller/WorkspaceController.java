package com.wave.backend.workspace.controller;

import com.wave.backend.workspace.dto.CreateWorkspaceRequest;
import com.wave.backend.workspace.dto.WorkspaceResponse;
import com.wave.backend.workspace.service.WorkspaceService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/workspaces")
public class WorkspaceController {

    private final WorkspaceService workspaceService;

    public WorkspaceController(WorkspaceService workspaceService) {
        this.workspaceService = workspaceService;
    }

    @PostMapping
    public WorkspaceResponse createWorkspace(
            @Valid @RequestBody CreateWorkspaceRequest request) {

        return workspaceService.createWorkspace(request);
    }
}