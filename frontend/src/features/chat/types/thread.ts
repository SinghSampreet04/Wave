export interface ThreadReply {
  id: number;

  parentMessageId: number;

  senderId: number;

  senderUsername: string;

  content: string;

  createdAt: string;
}

export interface ReplyMessageRequest {
  parentMessageId: number;

  content: string;
}