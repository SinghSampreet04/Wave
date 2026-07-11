package com.wave.backend.workspace.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WorkspaceResponse {

    private Long id;

    private String name;

    private String description;

    private Long ownerId;

    private String ownerUsername;

}