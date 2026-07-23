import { useEffect } from "react";
import { useQueryClient } from "@tanstack/react-query";

import { websocketService } from "../../../services/websocket/websocketService";

import { useChatStore } from "../store/chatStore";
import { useWebSocketStore } from "../../websocket/store/websocketStore";
import { useAuthStore } from "../../auth/store/authStore";

import type { ChatMessage } from "../types/chat";
import { updateReactionInMessageCache } from "../cache/messageCache";

export function useChannelSubscription(
  channelId?: number
) {
  const queryClient = useQueryClient();
  const connected = useWebSocketStore(
    (state) => state.connected
  );

  const addMessage = useChatStore(
    (state) => state.addMessage
  );

  const updateMessage = useChatStore(
    (state) => state.updateMessage
  );
  const updateReactions = useChatStore(
    (state) => state.updateReactions
  );
  const currentUserId = useAuthStore(
    (state) => state.currentUser?.id
  );

  const startTyping = useChatStore(
    (state) => state.startTyping
  );

  const stopTyping = useChatStore(
    (state) => state.stopTyping
  );

  useEffect(() => {
    if (!connected || !channelId) {
      return;
    }

    const messageSubscription =
      websocketService.subscribeToChannel(
        channelId,
        (message) => {
          const chatMessage: ChatMessage = {
            id: message.id,
            senderId: message.senderId,
            senderUsername: message.senderUsername,
            channelId: message.channelId,
            content: message.content,
            edited: false,
            deleted: false,
            reactions: message.reactions ?? [],
            createdAt: message.createdAt,
            updatedAt: message.createdAt,
          };

          addMessage(chatMessage);
        }
      );

    const editedSubscription =
      websocketService.subscribeToEditedMessages(
        channelId,
        (editedMessage) => {
          updateMessage({
            id: editedMessage.messageId,
            content: editedMessage.content,
            edited: editedMessage.edited,
            updatedAt:
              editedMessage.editedAt,
          });
        }
      );

    const deletedSubscription =
      websocketService.subscribeToDeletedMessages(
        channelId,
        (deletedMessage) => {
          updateMessage({
            id: deletedMessage.messageId,
            deleted: deletedMessage.deleted,
            content:
              "This message was deleted.",
            updatedAt:
              deletedMessage.deletedAt,
          });
        }
      );

    const reactionSubscription =
      websocketService.subscribeToReactions(
        channelId,
        (reaction) => {
          const existing = useChatStore
            .getState()
            .messages.find(
              (message) => message.id === reaction.messageId
            )
            ?.reactions.find(
              (item) => item.emoji === reaction.emoji
            );

          const normalized = {
            messageId: reaction.messageId,
            emoji: reaction.emoji,
            count: reaction.count,
            reactedByCurrentUser:
              reaction.actorUserId === currentUserId
                ? reaction.reacted
                : existing?.reactedByCurrentUser ?? false,
          };

          updateReactions(
            reaction.messageId,
            [normalized]
          );
          updateReactionInMessageCache(
            queryClient,
            normalized
          );
        }
      );

    const typingSubscription =
      websocketService.subscribeToTyping(
        channelId,
        (typing) => {
          if (typing.typing) {
            startTyping(typing);
          } else {
            stopTyping(
              typing.userId
            );
          }
        }
      );

    return () => {
      messageSubscription?.unsubscribe();
      editedSubscription?.unsubscribe();
      deletedSubscription?.unsubscribe();
      reactionSubscription?.unsubscribe();
      typingSubscription?.unsubscribe();
    };
  }, [
    connected,
    channelId,
    addMessage,
    updateMessage,
    updateReactions,
    currentUserId,
    queryClient,
    startTyping,
    stopTyping,
  ]);
}
