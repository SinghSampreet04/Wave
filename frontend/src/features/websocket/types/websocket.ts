export interface ReactionRealtimeEvent {
  messageId: number;
  emoji: string;
  count: number;
  actorUserId: number;
  reacted: boolean;
}

export interface ReactionResponse {
  messageId: number;
  emoji: string;
  count: number;
  reactedByCurrentUser: boolean;
}

export interface ChatMessage {
  channelId: number;

  content: string;
}

export interface ChatMessageResponse {
  id: number;

  channelId: number;

  senderId: number;

  senderUsername: string;

  content: string;

  edited: boolean;

  deleted: boolean;

  reactions: ReactionResponse[];

  createdAt: string;

  updatedAt: string;
}

export interface MessageEditResponse {
  messageId: number;

  channelId: number;

  content: string;

  edited: boolean;

  editedAt: string;
}

export interface TypingMessage {
  channelId: number;

  typing: boolean;
}

export interface TypingResponse {
  userId: number;

  username: string;

  typing: boolean;
}

export interface WebSocketState {
  connected: boolean;

  connecting: boolean;

  client: import("@stomp/stompjs").Client | null;

  setClient: (
    client: import("@stomp/stompjs").Client | null
  ) => void;

  setConnected: (
    connected: boolean
  ) => void;

  setConnecting: (
    connecting: boolean
  ) => void;

  disconnect: () => void;
}
