import api from "../../../api/axios";

import type {
  Channel,
  CreateChannelRequest,
} from "../types/channel";

export async function getWorkspaceChannels(
  workspaceId: number
): Promise<Channel[]> {
  const response =
    await api.get<Channel[]>(
      `/v1/channels/workspace/${workspaceId}`
    );

  return response.data;
}

export async function createChannel(
  request: CreateChannelRequest
): Promise<Channel> {
  const response =
    await api.post<Channel>(
      "/v1/channels",
      request
    );

  return response.data;
}

export async function deleteChannel(
  channelId: number
): Promise<void> {
  await api.delete(
    `/v1/channels/${channelId}`
  );
}

export async function inviteUserToChannel(
  channelId: number,
  inviteeId: number
): Promise<void> {
  await api.post("/v1/channel-invitations", {
    channelId,
    inviteeId,
  });
}

export interface ChannelInvitation {
  id: number;
  channelId: number;
  channelName: string;
  inviterUsername: string;
  status:
    | "PENDING"
    | "ACCEPTED"
    | "DECLINED"
    | "EXPIRED";
}

export async function getMyChannelInvitations(): Promise<
  ChannelInvitation[]
> {
  const response =
    await api.get<ChannelInvitation[]>(
      "/v1/channel-invitations/me"
    );
  return response.data;
}

export async function acceptChannelInvitation(
  invitationId: number
): Promise<void> {
  await api.post(
    `/v1/channel-invitations/${invitationId}/accept`
  );
}
