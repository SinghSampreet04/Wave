import type {
  IMessage,
  StompSubscription,
} from "@stomp/stompjs";
import { createStompClient } from "./stompClient";

import {
  useWebSocketStore,
} from "../../features/websocket/store/websocketStore";

import type {
  ChatMessage,
  ChatMessageResponse,
  TypingMessage,
  TypingResponse,
  ReactionRealtimeEvent,
} from "../../features/websocket/types/websocket";
import type { ThreadReply } from "../../features/chat/types/thread";
import type { DirectMessage } from "../../features/direct/types";

export interface MessageEditedResponse {
  messageId: number;
  channelId: number;
  content: string;
  edited: boolean;
  editedAt: string;
}

export interface MessageDeletedResponse {
  messageId: number;
  channelId: number;
  deleted: boolean;
  deletedAt: string;
}

interface ThreadReplyEvent {
  replyId: number;
  parentMessageId: number;
  senderId: number;
  senderUsername: string;
  content: string;
  createdAt: string;
}

interface DirectMessageSentEvent {
  messageId: number;
  conversationId: number;
  senderId: number;
  senderUsername: string;
  content: string;
  createdAt: string;
}

interface DirectMessageEditedEvent {
  messageId: number;
  conversationId: number;
  content: string;
  updatedAt: string;
}

interface DirectMessageDeletedEvent {
  messageId: number;
  conversationId: number;
}

interface DirectReactionEvent {
  directMessageId: number;
  emoji: string;
  count: number;
  actorUserId: number;
  reacted: boolean;
}

export interface NotificationEvent {
  notificationId: number;
  recipientId: number;
  type: string;
  title: string;
  body: string;
  referenceId: number | null;
  referenceType: string | null;
  createdAt: string;
}

class WebSocketService {
  connect(token: string) {
    const store = useWebSocketStore.getState();

    if (store.connected || store.connecting) {
      return;
    }

    store.setConnecting(true);

    const client = createStompClient(token);

    client.onConnect = () => {
      const current = useWebSocketStore.getState();

      current.setClient(client);
      current.setConnected(true);
      current.setConnecting(false);
    };

    client.onStompError = () => {
      const current = useWebSocketStore.getState();
      current.setConnected(false);
      current.setConnecting(client.active);
    };

    client.onWebSocketClose = () => {
      const current = useWebSocketStore.getState();

      current.setConnected(false);
      current.setConnecting(client.active);
    };

    client.onWebSocketError = () => {
      const current = useWebSocketStore.getState();
      current.setConnected(false);
      current.setConnecting(client.active);
    };

    client.activate();
  }

  disconnect() {
    useWebSocketStore
      .getState()
      .disconnect();
  }

  subscribeToChannel(
    channelId: number,
    callback: (
      message: ChatMessageResponse
    ) => void
  ): StompSubscription | null {
    const client =
      useWebSocketStore.getState().client;

    if (!client || !client.connected) {
      return null;
    }

    return client.subscribe(
      `/topic/channels/${channelId}`,
      (message: IMessage) => {
        callback(
          JSON.parse(
            message.body
          ) as ChatMessageResponse
        );
      }
    );
  }

  subscribeToTyping(
    channelId: number,
    callback: (
      typing: TypingResponse
    ) => void
  ): StompSubscription | null {
    const client =
      useWebSocketStore.getState().client;

    if (!client || !client.connected) {
      return null;
    }

    return client.subscribe(
      `/topic/channels/${channelId}/typing`,
      (message: IMessage) => {
        callback(
          JSON.parse(
            message.body
          ) as TypingResponse
        );
      }
    );
  }

  subscribeToEditedMessages(
    channelId: number,
    callback: (
      message: MessageEditedResponse
    ) => void
  ): StompSubscription | null {
    const client =
      useWebSocketStore.getState().client;

    if (!client || !client.connected) {
      return null;
    }

    return client.subscribe(
      `/topic/channels/${channelId}/edited`,
      (message: IMessage) => {
        callback(
          JSON.parse(
            message.body
          ) as MessageEditedResponse
        );
      }
    );
  }

  subscribeToDeletedMessages(
    channelId: number,
    callback: (
      message: MessageDeletedResponse
    ) => void
  ): StompSubscription | null {
    const client =
      useWebSocketStore.getState().client;

    if (!client || !client.connected) {
      return null;
    }

    return client.subscribe(
      `/topic/channels/${channelId}/deleted`,
      (message: IMessage) => {
        callback(
          JSON.parse(
            message.body
          ) as MessageDeletedResponse
        );
      }
    );
  }

  subscribeToReactions(
    channelId: number,
    callback: (
      reaction: ReactionRealtimeEvent
    ) => void
  ): StompSubscription | null {
    const client =
      useWebSocketStore.getState().client;

    if (!client || !client.connected) {
      return null;
    }

    return client.subscribe(
      `/topic/channels/${channelId}/reactions`,
      (message: IMessage) => {
        callback(
          JSON.parse(
            message.body
          ) as ReactionRealtimeEvent
        );
      }
    );
  }

  subscribeToThread(
    parentMessageId: number,
    callback: (
      reply: ThreadReply
    ) => void
  ): StompSubscription | null {
    const client =
      useWebSocketStore.getState().client;

    if (!client || !client.connected) {
      return null;
    }

    return client.subscribe(
      `/topic/threads/${parentMessageId}`,
      (message: IMessage) => {
        const event = JSON.parse(
          message.body
        ) as ThreadReplyEvent;

        callback({
          id: event.replyId,
          parentMessageId:
            event.parentMessageId,
          senderId: event.senderId,
          senderUsername:
            event.senderUsername,
          content: event.content,
          createdAt: event.createdAt,
        });
      }
    );
  }

  subscribeToNotifications(
    userId: number,
    callback: (
      notification: NotificationEvent
    ) => void
  ): StompSubscription | null {
    const client =
      useWebSocketStore.getState().client;

    if (!client || !client.connected) {
      return null;
    }

    return client.subscribe(
      `/topic/notifications/${userId}`,
      (message: IMessage) => {
        callback(
          JSON.parse(
            message.body
          ) as NotificationEvent
        );
      }
    );
  }

  subscribeToDirectConversation(
    conversationId: number,
    callbacks: {
      onMessage: (
        message: DirectMessage
      ) => void;
      onEdit: (
        event: DirectMessageEditedEvent
      ) => void;
      onDelete: (
        event: DirectMessageDeletedEvent
      ) => void;
      onReaction?: (
        event: DirectReactionEvent
      ) => void;
      onTyping?: (
        event: TypingResponse
      ) => void;
    }
  ): StompSubscription[] {
    const client =
      useWebSocketStore.getState().client;

    if (!client || !client.connected) {
      return [];
    }

    const messageSubscription =
      client.subscribe(
        `/topic/conversations/${conversationId}`,
        (message: IMessage) => {
          const event = JSON.parse(
            message.body
          ) as DirectMessageSentEvent;

          callbacks.onMessage({
            messageId: event.messageId,
            conversationId:
              event.conversationId,
            senderId: event.senderId,
            senderUsername:
              event.senderUsername,
            content: event.content,
            edited: false,
            deleted: false,
            createdAt: event.createdAt,
            updatedAt: event.createdAt,
            reactions: [],
          });
        }
      );

    const editedSubscription =
      client.subscribe(
        `/topic/conversations/${conversationId}/edited`,
        (message: IMessage) =>
          callbacks.onEdit(
            JSON.parse(
              message.body
            ) as DirectMessageEditedEvent
          )
      );

    const deletedSubscription =
      client.subscribe(
        `/topic/conversations/${conversationId}/deleted`,
        (message: IMessage) =>
          callbacks.onDelete(
            JSON.parse(
              message.body
            ) as DirectMessageDeletedEvent
          )
      );

    const reactionSubscription = client.subscribe(
      `/topic/conversations/${conversationId}/reactions`,
      (message: IMessage) =>
        callbacks.onReaction?.(
          JSON.parse(message.body) as DirectReactionEvent
        )
    );

    const typingSubscription = client.subscribe(
      `/topic/conversations/${conversationId}/typing`,
      (message: IMessage) =>
        callbacks.onTyping?.(
          JSON.parse(message.body) as TypingResponse
        )
    );

    return [
      messageSubscription,
      editedSubscription,
      deletedSubscription,
      reactionSubscription,
      typingSubscription,
    ];
  }

  sendMessage(
    message: ChatMessage
  ) {
    const client =
      useWebSocketStore.getState().client;

    if (!client || !client.connected) {
      return;
    }

    client.publish({
      destination: "/app/chat.send",
      body: JSON.stringify(message),
    });
  }

  sendTyping(
    typing: TypingMessage
  ) {
    const client =
      useWebSocketStore.getState().client;

    if (!client || !client.connected) {
      return;
    }

    client.publish({
      destination: "/app/chat.typing",
      body: JSON.stringify(typing),
    });
  }

  sendDirectTyping(conversationId: number, typing: boolean) {
    const client = useWebSocketStore.getState().client;
    if (!client || !client.connected) return;
    client.publish({
      destination: "/app/direct.typing",
      body: JSON.stringify({ conversationId, typing }),
    });
  }
}

export const websocketService =
  new WebSocketService();
