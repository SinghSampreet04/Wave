import { create } from "zustand";

import type {
  ChatMessage,
  ChatState,
  Reaction,
} from "../types/chat";

import type { TypingResponse } from "../../websocket/types/websocket";

interface ChatStore extends ChatState {
  typingUsers: TypingResponse[];

  startTyping: (
    user: TypingResponse
  ) => void;

  stopTyping: (
    userId: number
  ) => void;

  updateMessage: (
    message: Partial<ChatMessage> & {
      id: number;
    }
  ) => void;

  updateReactions: (
    messageId: number,
    reactions: Reaction[]
  ) => void;
}

export const useChatStore =
  create<ChatStore>((set) => ({
    messages: [],

    typingUsers: [],

    setMessages: (
      messages: ChatMessage[]
    ) =>
      set({
        messages,
      }),

    addMessage: (
      message: ChatMessage
    ) =>
      set((state) => {
        if (
          state.messages.some(
            (existing) =>
              existing.id === message.id
          )
        ) {
          return state;
        }

        return {
          messages: [
            ...state.messages,
            message,
          ],
        };
      }),

    updateMessage: (
      updatedMessage
    ) =>
      set((state) => ({
        messages: state.messages.map(
          (message) =>
            message.id ===
            updatedMessage.id
              ? {
                  ...message,
                  ...updatedMessage,
                }
              : message
        ),
      })),

    updateReactions: (
      messageId,
      reactions
    ) =>
      set((state) => ({
        messages: state.messages.map(
          (message) => {
            if (message.id !== messageId) {
              return message;
            }

            const updatedReactions = [
              ...message.reactions,
            ];

            reactions.forEach(
              (incomingReaction) => {
                const index =
                  updatedReactions.findIndex(
                    (reaction) =>
                      reaction.emoji ===
                      incomingReaction.emoji
                  );

                if (
                  incomingReaction.count ===
                  0
                ) {
                  if (index !== -1) {
                    updatedReactions.splice(
                      index,
                      1
                    );
                  }

                  return;
                }

                if (index === -1) {
                  updatedReactions.push(
                    incomingReaction
                  );
                } else {
                  updatedReactions[
                    index
                  ] = incomingReaction;
                }
              }
            );

            return {
              ...message,
              reactions:
                updatedReactions,
            };
          }
        ),
      })),

    startTyping: (
      user: TypingResponse
    ) =>
      set((state) => {
        const exists =
          state.typingUsers.some(
            (typingUser) =>
              typingUser.userId ===
              user.userId
          );

        if (exists) {
          return state;
        }

        return {
          typingUsers: [
            ...state.typingUsers,
            user,
          ],
        };
      }),

    stopTyping: (
      userId: number
    ) =>
      set((state) => ({
        typingUsers:
          state.typingUsers.filter(
            (user) =>
              user.userId !== userId
          ),
      })),

    clearMessages: () =>
      set({
        messages: [],
        typingUsers: [],
      }),
  }));
