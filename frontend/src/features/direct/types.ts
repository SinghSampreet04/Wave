export interface Conversation {
  conversationId: number;
  otherUserId: number;
  otherUsername: string;
  otherFirstName: string;
  otherLastName: string;
  otherAvatar: string | null;
  lastMessage: string | null;
  lastMessageAt: string | null;
  createdAt: string;
  unreadCount: number;
}

export interface DirectMessage {
  messageId: number;
  conversationId: number;
  senderId: number;
  senderUsername: string;
  content: string;
  edited: boolean;
  deleted: boolean;
  createdAt: string;
  updatedAt: string;
  reactions: DirectReaction[];
}

export interface DirectReaction {
  directMessageId: number;
  emoji: string;
  count: number;
  reacted: boolean;
}

export interface UserSummary {
  id: number;
  username: string;
  firstName: string;
  lastName: string;
  avatar: string | null;
}
