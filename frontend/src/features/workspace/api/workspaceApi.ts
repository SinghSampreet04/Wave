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
    `/v1/workspaces/${workspaceId}/members/invite`,
    request
  );
}

export async function deleteWorkspace(
  workspaceId: number
): Promise<void> {
  await api.delete(
    `/v1/workspaces/${workspaceId}`
  );
}
