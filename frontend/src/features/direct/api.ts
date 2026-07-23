import api from "../../api/axios";
import type {
  Conversation,
  DirectMessage,
  DirectReaction,
  UserSummary,
} from "./types";

export async function getConversations(): Promise<
  Conversation[]
> {
  const response = await api.get<Conversation[]>(
    "/v1/conversations"
  );
  return response.data;
}

export async function createConversation(
  recipientUserId: number
): Promise<Conversation> {
  const response = await api.post<Conversation>(
    "/v1/conversations",
    { recipientUserId }
  );
  return response.data;
}

export async function searchUsers(
  query: string
): Promise<UserSummary[]> {
  const response = await api.get<UserSummary[]>(
    "/v1/users/search",
    { params: { q: query } }
  );
  return response.data;
}

export async function getDirectMessages(
  conversationId: number
): Promise<DirectMessage[]> {
  const response = await api.get<DirectMessage[]>(
    `/v1/direct-messages/conversation/${conversationId}`,
    { params: { page: 0, size: 100 } }
  );
  return [...response.data].reverse();
}

export async function sendDirectMessage(
  conversationId: number,
  content: string
): Promise<DirectMessage> {
  const response = await api.post<DirectMessage>(
    "/v1/direct-messages",
    { conversationId, content }
  );
  return response.data;
}

export async function editDirectMessage(
  messageId: number,
  content: string
): Promise<void> {
  await api.patch(
    `/v1/direct-messages/${messageId}`,
    { content }
  );
}

export async function deleteDirectMessage(
  messageId: number
): Promise<void> {
  await api.delete(
    `/v1/direct-messages/${messageId}`
  );
}

export async function toggleDirectReaction(
  directMessageId: number,
  emoji: string
): Promise<DirectReaction> {
  const response = await api.post<DirectReaction>("/v1/direct-reactions", {
    directMessageId,
    emoji,
  });
  return response.data;
}

export async function searchDirectMessages(
  conversationId: number,
  query: string
): Promise<DirectSearchResult[]> {
  const response = await api.get<DirectSearchResult[]>(
    `/v1/direct-messages/conversation/${conversationId}/search`,
    { params: { query } }
  );
  return response.data;
}

export interface DirectSearchResult {
  id: number;
  senderId: number;
  senderUsername: string;
  content: string;
  edited: boolean;
  deleted: boolean;
  createdAt: string;
}

export async function markDirectMessageRead(
  conversationId: number,
  messageId: number
): Promise<void> {
  await api.post(`/v1/direct-messages/${conversationId}/read/${messageId}`);
}

export interface DirectAttachment {
  id: number;
  directMessageId: number;
  originalFileName: string;
  contentType: string;
  fileSize: number;
  uploadedAt: string;
}

export async function uploadDirectAttachment(
  messageId: number,
  file: File
): Promise<DirectAttachment> {
  const form = new FormData();
  form.append("file", file);
  const response = await api.post<DirectAttachment>(
    `/v1/direct-attachments/${messageId}`,
    form,
    { headers: { "Content-Type": "multipart/form-data" } }
  );
  return response.data;
}

export async function getDirectAttachments(
  messageId: number
): Promise<DirectAttachment[]> {
  const response = await api.get<DirectAttachment[]>(
    `/v1/direct-attachments/message/${messageId}`
  );
  return response.data;
}

export async function downloadDirectAttachment(
  attachment: DirectAttachment
): Promise<void> {
  const response = await api.get(
    `/v1/direct-attachments/${attachment.id}`,
    { responseType: "blob" }
  );
  const url = URL.createObjectURL(response.data as Blob);
  const link = document.createElement("a");
  link.href = url;
  link.download = attachment.originalFileName;
  link.click();
  URL.revokeObjectURL(url);
}
