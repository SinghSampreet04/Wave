import api from "../../../api/axios";

import type {
  ReplyMessageRequest,
  ThreadReply,
} from "../types/thread";

export async function getThreadReplies(
  messageId: number
): Promise<ThreadReply[]> {
  const response =
    await api.get<ThreadReply[]>(
      `/v1/threads/${messageId}`
    );

  return response.data;
}

export async function replyToThread(
  request: ReplyMessageRequest
): Promise<ThreadReply> {
  const response =
    await api.post<ThreadReply>(
      "/v1/threads/reply",
      request
    );

  return response.data;
}