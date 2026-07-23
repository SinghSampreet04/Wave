import api from "../../../api/axios";

import type {
  ChatMessage,
  Reaction,
  SendMessageRequest,
} from "../types/chat";

export async function getChannelMessages(
  channelId: number
): Promise<ChatMessage[]> {
  const response = await api.get<ChatMessage[]>(
    `/v1/messages/channel/${channelId}`
  );

  return response.data;
}

export interface MessageHistoryPage {
  items: ChatMessage[];
  nextCursor: number | null;
  hasMore: boolean;
}

export async function getChannelMessageHistory(
  channelId: number,
  before?: number
): Promise<MessageHistoryPage> {
  const response = await api.get<MessageHistoryPage>(
    `/v1/messages/channel/${channelId}/history`,
    { params: { before, size: 50 } }
  );
  return response.data;
}

export async function sendMessage(
  request: SendMessageRequest
): Promise<ChatMessage> {
  const response = await api.post<ChatMessage>(
    "/v1/messages",
    request
  );

  return response.data;
}

export async function editMessage(
  messageId: number,
  content: string
): Promise<ChatMessage> {
  const response = await api.patch<ChatMessage>(
    `/v1/messages/${messageId}`,
    {
      content,
    }
  );

  return response.data;
}

export async function deleteMessage(
  messageId: number
): Promise<{
  messageId: number;
  channelId: number;
  deleted: boolean;
  deletedAt: string;
}> {
  const response = await api.delete<{
    messageId: number;
    channelId: number;
    deleted: boolean;
    deletedAt: string;
  }>(
    `/v1/messages/${messageId}`
  );

  return response.data;
}

export async function toggleReaction(
  messageId: number,
  emoji: string
): Promise<Reaction> {
  const response = await api.post<Reaction>(
    "/v1/reactions",
    {
      messageId,
      emoji,
    }
  );

  return response.data;
}

export async function uploadMessageFile(
  messageId: number,
  file: File
): Promise<void> {
  const form = new FormData();
  form.append("file", file);
  await api.post(`/v1/files/upload/${messageId}`, form, {
    headers: { "Content-Type": "multipart/form-data" },
  });
}

export interface PinnedMessage {
  pinId: number;
  messageId: number;
  channelId: number;
  pinnedByUserId: number;
  pinnedByUsername: string;
  messageContent: string;
  pinnedAt: string;
}

export async function pinMessage(messageId: number): Promise<PinnedMessage> {
  const response = await api.post<PinnedMessage>(
    `/v1/messages/${messageId}/pin`
  );
  return response.data;
}

export async function unpinMessage(messageId: number): Promise<void> {
  await api.delete(`/v1/messages/${messageId}/pin`);
}

export async function getPinnedMessages(
  channelId: number
): Promise<PinnedMessage[]> {
  const response = await api.get<PinnedMessage[]>(
    `/v1/channels/${channelId}/pins`
  );
  return response.data;
}

export async function markMessageRead(messageId: number): Promise<void> {
  await api.post("/v1/read-receipts", { messageId });
}
