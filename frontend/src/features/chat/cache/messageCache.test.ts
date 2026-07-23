import {
  QueryClient,
  type InfiniteData,
} from "@tanstack/react-query";
import {
  describe,
  expect,
  it,
} from "vitest";

import type { MessageHistoryPage } from "../api/chat";
import type { ChatMessage } from "../types/chat";
import {
  mergeReaction,
  updateReactionInMessageCache,
} from "./messageCache";

const message: ChatMessage = {
  id: 11,
  senderId: 2,
  senderUsername: "Wave Tester",
  channelId: 3,
  content: "Reactions stay visible",
  edited: false,
  deleted: false,
  reactions: [],
  createdAt: "2026-07-23T00:00:00Z",
  updatedAt: "2026-07-23T00:00:00Z",
};

describe("message reaction cache", () => {
  it("adds, updates, and removes aggregated reactions", () => {
    const added = mergeReaction([], {
      messageId: 11,
      emoji: "👍",
      count: 1,
      reactedByCurrentUser: true,
    });

    expect(added).toHaveLength(1);
    expect(
      mergeReaction(added, {
        ...added[0],
        count: 0,
        reactedByCurrentUser: false,
      })
    ).toEqual([]);
  });

  it("keeps React Query history synchronized with a reaction", () => {
    const queryClient = new QueryClient();
    const queryKey = ["messages", 3];

    queryClient.setQueryData<
      InfiniteData<MessageHistoryPage>
    >(queryKey, {
      pages: [
        {
          items: [message],
          nextCursor: null,
          hasMore: false,
        },
      ],
      pageParams: [undefined],
    });

    updateReactionInMessageCache(
      queryClient,
      {
        messageId: 11,
        emoji: "🚀",
        count: 2,
        reactedByCurrentUser: true,
      }
    );

    const cached = queryClient.getQueryData<
      InfiniteData<MessageHistoryPage>
    >(queryKey);

    expect(
      cached?.pages[0].items[0].reactions
    ).toEqual([
      {
        messageId: 11,
        emoji: "🚀",
        count: 2,
        reactedByCurrentUser: true,
      },
    ]);
  });
});
