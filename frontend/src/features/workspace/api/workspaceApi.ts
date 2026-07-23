import api from "../../../api/axios";

import type {
  CreateWorkspaceRequest,
  InviteWorkspaceMemberRequest,
  Workspace,
} from "../types/workspace";

export async function getWorkspaces(): Promise<Workspace[]> {
  const response = await api.get<Workspace[]>("/v1/workspaces");
  return response.data;
}

export async function createWorkspace(
  request: CreateWorkspaceRequest
): Promise<Workspace> {
  const response = await api.post<Workspace>("/v1/workspaces", request);
  return response.data;
}

export async function inviteWorkspaceMember(
  workspaceId: number,
  request: InviteWorkspaceMemberRequest
): Promise<void> {
  await api.post(
    "/v1/workspace-invitations",
    {
      workspaceId,
      inviteeEmail:
        request.email.trim(),
    }
  );
}

export interface WorkspaceInvitation {
  id: number;
  workspaceId: number;
  workspaceName: string;
  inviterUsername: string;
  status:
    | "PENDING"
    | "ACCEPTED"
    | "DECLINED"
    | "EXPIRED";
}

export async function getMyWorkspaceInvitations(): Promise<
  WorkspaceInvitation[]
> {
  const response = await api.get<
    WorkspaceInvitation[]
  >(
    "/v1/workspace-invitations/me"
  );
  return response.data;
}

export async function acceptWorkspaceInvitation(
  invitationId: number
): Promise<void> {
  await api.patch(
    `/v1/workspace-invitations/${invitationId}/accept`
  );
}

export async function deleteWorkspace(
  workspaceId: number
): Promise<void> {
  await api.delete(
    `/v1/workspaces/${workspaceId}`
  );
}
