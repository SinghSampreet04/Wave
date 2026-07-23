import { useEffect, useRef, useState } from "react";

import { useChannelStore } from "../../channel/store/channelStore";

import { useMessages } from "../hooks/useMessages";
import { useChannelSubscription } from "../hooks/useChannelSubscription";

import { useChatStore } from "../store/chatStore";

import ChatEmptyState from "./ChatEmptyState";
import MessageBubble from "./MessageBubble";
import { markMessageRead } from "../api/chat";
import { useAuthStore } from "../../auth/store/authStore";

export default function MessageList() {
  const activeChannel = useChannelStore(
    (state) => state.activeChannel
  );

  useChannelSubscription(
    activeChannel?.id
  );

  const {
    data,
    isLoading,
    isError,
    hasNextPage,
    fetchNextPage,
    isFetchingNextPage,
  } = useMessages(
    activeChannel?.id
  );

  const messages = useChatStore(
    (state) => state.messages
  );
  const currentUserId = useAuthStore((state) => state.currentUser?.id);

  const setMessages = useChatStore(
    (state) => state.setMessages
  );

  const clearMessages = useChatStore(
    (state) => state.clearMessages
  );

  const containerRef = useRef<HTMLDivElement>(null);

  const [shouldAutoScroll, setShouldAutoScroll] =
    useState(true);

  useEffect(() => {
    if (!activeChannel) {
      clearMessages();
      return;
    }

    if (data) {
      setMessages(data);
    }
  }, [
    activeChannel,
    data,
    setMessages,
    clearMessages,
  ]);

  useEffect(() => {
    if (!containerRef.current) {
      return;
    }

    if (!shouldAutoScroll) {
      return;
    }

    containerRef.current.scrollTo({
      top: containerRef.current.scrollHeight,
      behavior: "smooth",
    });
  }, [messages, shouldAutoScroll]);

  useEffect(() => {
    const latestUnread = [...messages]
      .reverse()
      .find(
        (message) =>
          !message.deleted &&
          message.senderId !== currentUserId
      );
    if (latestUnread) {
      void markMessageRead(latestUnread.id);
    }
  }, [messages, currentUserId]);

  const handleScroll = () => {
    if (!containerRef.current) {
      return;
    }

    const {
      scrollTop,
      scrollHeight,
      clientHeight,
    } = containerRef.current;

    const distanceFromBottom =
      scrollHeight - scrollTop - clientHeight;

    setShouldAutoScroll(
      distanceFromBottom < 120
    );
  };

  if (!activeChannel) {
    return (
      <div className="flex flex-1 items-center justify-center text-slate-400">
        Select a channel to start chatting.
      </div>
    );
  }

  if (isLoading) {
    return (
      <div className="flex flex-1 items-center justify-center text-slate-400">
        Loading messages...
      </div>
    );
  }

  if (isError) {
    return (
      <div className="flex flex-1 items-center justify-center text-red-400">
        Unable to load messages.
      </div>
    );
  }

  if (messages.length === 0) {
    return <ChatEmptyState />;
  }

  return (
    <div
      ref={containerRef}
      onScroll={handleScroll}
      className="flex-1 overflow-y-auto px-8 py-6"
    >
      {hasNextPage && (
        <div className="mb-4 flex justify-center">
          <button
            type="button"
            disabled={isFetchingNextPage}
            onClick={() => void fetchNextPage()}
            className="rounded-full border border-white/10 bg-slate-900 px-4 py-2 text-xs font-semibold text-cyan-200 disabled:opacity-50"
          >
            {isFetchingNextPage ? "Loading…" : "Load earlier messages"}
          </button>
        </div>
      )}
      <div className="space-y-4">
  {messages.map((message, index) => {
    const previousMessage = messages[index - 1];

    const isGrouped =
      previousMessage !== undefined &&
      previousMessage.senderId ===
        message.senderId;

    return (
      <MessageBubble
        key={message.id}
        message={message}
        isGrouped={isGrouped}
      />
    );
  })}
</div>
    </div>
  );
}
