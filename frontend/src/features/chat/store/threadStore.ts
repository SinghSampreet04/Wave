import { create } from "zustand";

import type { ChatMessage } from "../types/chat";
import type { ThreadReply } from "../types/thread";

interface ThreadStore {
  parentMessage: ChatMessage | null;

  replies: ThreadReply[];

  isOpen: boolean;

  setParentMessage: (
    message: ChatMessage | null
  ) => void;

  setReplies: (
    replies: ThreadReply[]
  ) => void;

  addReply: (
    reply: ThreadReply
  ) => void;

  openThread: (
    message: ChatMessage
  ) => void;

  closeThread: () => void;
}

export const useThreadStore =
  create<ThreadStore>((set) => ({
    parentMessage: null,

    replies: [],

    isOpen: false,

    setParentMessage: (
      message
    ) =>
      set({
        parentMessage: message,
      }),

    setReplies: (
      replies
    ) =>
      set({
        replies,
      }),

    addReply: (
      reply
    ) =>
      set((state) => {
        if (
          state.replies.some(
            (existing) =>
              existing.id === reply.id
          )
        ) {
          return state;
        }

        return {
          replies: [
            ...state.replies,
            reply,
          ],
        };
      }),

    openThread: (
      message
    ) =>
      set({
        parentMessage: message,
        replies: [],
        isOpen: true,
      }),

    closeThread: () =>
      set({
        parentMessage: null,
        replies: [],
        isOpen: false,
      }),
  }));
