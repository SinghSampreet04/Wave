export interface Workspace {
  id: number;
  name: string;
  description: string | null;
  ownerId: number;
  ownerUsername: string;
}

export interface CreateWorkspaceRequest {
  name: string;
  description?: string;
}

export interface InviteWorkspaceMemberRequest {
  email: string;
}

export interface WorkspaceState {
  activeWorkspace: Workspace | null;

  setActiveWorkspace: (workspace: Workspace) => void;

  clearActiveWorkspace: () => void;
}