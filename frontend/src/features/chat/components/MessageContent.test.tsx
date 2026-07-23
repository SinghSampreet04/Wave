import { render, screen } from "@testing-library/react";
import { describe, expect, it } from "vitest";

import MessageContent from "./MessageContent";
import type { ChatMessage } from "../types/chat";

const message: ChatMessage = {
  id: 1,
  senderId: 2,
  senderUsername: "Sampreet",
  channelId: 3,
  content: "Hello @alex, the draft is visible.",
  edited: false,
  deleted: false,
  reactions: [],
  createdAt: "2026-07-22T10:00:00Z",
  updatedAt: "2026-07-22T10:00:00Z",
};

describe("MessageContent", () => {
  it("renders message text and highlights mentions", () => {
    render(<MessageContent message={message} />);

    expect(
      screen.getByText(/the draft is visible/)
    ).toBeVisible();
    expect(screen.getByText("@alex")).toHaveClass("text-cyan-200");
  });

  it("does not expose deleted content", () => {
    render(
      <MessageContent
        message={{ ...message, deleted: true }}
      />
    );

    expect(screen.getByText("This message was deleted.")).toBeVisible();
    expect(screen.queryByText("@alex")).not.toBeInTheDocument();
  });
});
