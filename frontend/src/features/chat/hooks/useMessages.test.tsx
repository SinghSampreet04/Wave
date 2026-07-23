import type { ReactNode } from "react";
import {
  QueryClient,
  QueryClientProvider,
} from "@tanstack/react-query";
import {
  renderHook,
  waitFor,
} from "@testing-library/react";
import {
  describe,
  expect,
  it,
  vi,
} from "vitest";

import { getChannelMessageHistory } from "../api/chat";
import type { ChatMessage } from "../types/chat";
import { useMessages } from "./useMessages";

vi.mock("../api/chat", () => ({
  getChannelMessageHistory: vi.fn(),
}));

const message: ChatMessage = {
  id: 1,
  senderId: 2,
  senderUsername: "Wave Tester",
  channelId: 3,
  content: "Stable history",
  edited: false,
  deleted: false,
  reactions: [],
  createdAt: "2026-07-23T00:00:00Z",
  updatedAt: "2026-07-23T00:00:00Z",
};

describe("useMessages", () => {
  it("keeps the flattened message reference stable across rerenders", async () => {
    vi.mocked(getChannelMessageHistory).mockResolvedValue({
      items: [message],
      nextCursor: null,
      hasMore: false,
    });

    const queryClient = new QueryClient({
      defaultOptions: {
        queries: {
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

    const { result, rerender } = renderHook(
      () => useMessages(3),
      { wrapper }
    );

    await waitFor(() => {
      expect(result.current.isSuccess).toBe(true);
    });

    const firstReference = result.current.data;

    rerender();

    expect(result.current.data).toBe(firstReference);
  });
});
