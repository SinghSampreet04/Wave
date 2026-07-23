export interface Channel {
  id: number;
  name: string;
  description: string | null;
  isPrivate: boolean;
  workspaceId: number;
}

export interface CreateChannelRequest {
  name: string;
  description: string;
  workspaceId: number;
  isPrivate: boolean;
}

export interface ChannelState {
  activeChannel: Channel | null;

  setActiveChannel: (
    channel: Channel
  ) => void;

  clearActiveChannel: () => void;
}