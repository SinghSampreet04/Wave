import { useState } from "react";

import { useThreadStore } from "../store/threadStore";
import { useThreadReplies } from "../hooks/useThreadReplies";
import { useReplyToThread } from "../hooks/useReplyToThread";
import { useThreadSubscription } from "../hooks/useThreadSubscription";

export default function ThreadPanel() {
  const {
    parentMessage,
    closeThread,
  } = useThreadStore();

  const [content, setContent] =
    useState("");

  const { data: replies = [] } =
    useThreadReplies(
      parentMessage
        ? parentMessage.id
        : null
    );

  useThreadSubscription(
    parentMessage
      ? parentMessage.id
      : null
  );

  const {
    mutate: reply,
    isPending,
  } = useReplyToThread();

  if (!parentMessage) {
    return null;
  }

  return (
    <aside className="flex h-full w-96 flex-col border-l border-white/10 bg-[#111827]">
      <div className="flex items-center justify-between border-b border-white/10 p-4">
        <div>
          <h2 className="text-lg font-semibold text-white">
            Thread
          </h2>

          <p className="text-sm text-gray-400">
            {parentMessage.senderUsername}
          </p>
        </div>

        <button
          onClick={closeThread}
          className="text-gray-400 hover:text-white"
        >
          ✕
        </button>
      </div>

      <div className="border-b border-white/10 p-4">
        <p className="text-sm text-gray-300">
          {parentMessage.content}
        </p>
      </div>

      <div className="flex-1 overflow-y-auto p-4 space-y-4">
        {replies.map(
          (replyMessage) => (
            <div
              key={replyMessage.id}
              className="rounded-lg bg-white/5 p-3"
            >
              <div className="mb-1 text-sm font-semibold text-white">
                {replyMessage.senderUsername}
              </div>

              <div className="text-sm text-gray-300">
                {replyMessage.content}
              </div>
            </div>
          )
        )}
      </div>

      <div className="border-t border-white/10 p-4">
        <textarea
          value={content}
          onChange={(e) =>
            setContent(
              e.target.value
            )
          }
          rows={3}
          placeholder="Reply..."
          className="w-full rounded-lg border border-white/10 bg-white/5 p-3 text-white outline-none"
        />

        <button
          disabled={
            isPending ||
            !content.trim()
          }
          onClick={() => {
            reply(
              {
                parentMessageId:
                  parentMessage.id,
                content,
              },
              {
                onSuccess: () => {
                  setContent("");
                },
              }
            );
          }}
          className="mt-3 w-full rounded-lg bg-cyan-600 py-2 font-medium text-white hover:bg-cyan-500 disabled:opacity-50"
        >
          Reply
        </button>
      </div>
    </aside>
  );
}
