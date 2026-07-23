import type { ReactNode } from "react";
import {
  QueryClient,
  QueryClientProvider,
} from "@tanstack/react-query";
import {
  act,
  renderHook,
  waitFor,
} from "@testing-library/react";
import {
  beforeEach,
  describe,
  expect,
  it,
  vi,
} from "vitest";

import { toggleReaction } from "../api/chat";
import { useChatStore } from "../store/chatStore";
import type { ChatMessage } from "../types/chat";
import { useToggleReaction } from "./useToggleReaction";

vi.mock("../api/chat", () => ({
  toggleReaction: vi.fn(),
}));

const message: ChatMessage = {
  id: 17,
  senderId: 4,
  senderUsername: "Wave Tester",
  channelId: 8,
  content: "React to this",
  edited: false,
  deleted: false,
  reactions: [],
  createdAt: "2026-07-23T00:00:00Z",
  updatedAt: "2026-07-23T00:00:00Z",
};

describe("useToggleReaction", () => {
  beforeEach(() => {
    useChatStore.getState().clearMessages();
    useChatStore
      .getState()
      .setMessages([message]);
    vi.clearAllMocks();
  });

  it("renders the optimistic reaction and keeps the server aggregate", async () => {
    vi.mocked(toggleReaction).mockResolvedValue({
      messageId: 17,
      emoji: "👍",
      count: 1,
      reactedByCurrentUser: true,
    });

    const queryClient = new QueryClient({
      defaultOptions: {
        mutations: {
          retry: false,
        },
      },
    });
    const wrapper = ({
      children,
    }: {
      children: ReactNode;
    }) => (
      <QueryClientProvider client={queryClient}>
        {children}
      </QueryClientProvider>
    );
    const { result } = renderHook(
      () => useToggleReaction(),
      { wrapper }
    );

    act(() => {
      result.current.mutate({
        messageId: 17,
        emoji: "👍",
      });
    });

    await waitFor(() => {
      expect(
        useChatStore.getState().messages[0]
          .reactions
      ).toEqual([
        {
          messageId: 17,
          emoji: "👍",
          count: 1,
          reactedByCurrentUser: true,
        },
      ]);
      expect(toggleReaction).toHaveBeenCalledWith(
        17,
        "👍"
      );
      expect(result.current.isSuccess).toBe(true);
    });
  });
});
