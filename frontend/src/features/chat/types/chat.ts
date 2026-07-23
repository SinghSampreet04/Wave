export interface Reaction {
  messageId: number;

  emoji: string;

  count: number;

  reactedByCurrentUser: boolean;
}

export interface ChatMessage {
  id: number;

  senderId: number;

  senderUsername: string;

  channelId: number;

  content: string;

  edited: boolean;

  deleted: boolean;

  reactions: Reaction[];

  createdAt: string;

  updatedAt: string;
}

export interface SendMessageRequest {
  channelId: number;

  content: string;
}

export interface ChatState {
  messages: ChatMessage[];

  setMessages: (
    messages: ChatMessage[]
  ) => void;

  addMessage: (
    message: ChatMessage
  ) => void;

  updateMessage: (
    message: ChatMessage
  ) => void;

  clearMessages: () => void;
}