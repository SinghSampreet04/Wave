import {
  useEffect,
  useMemo,
  useRef,
  useState,
} from "react";
import {
  Paperclip,
  Pencil,
  Search,
  SendHorizontal,
  Trash2,
  X,
} from "lucide-react";
import {
  useMutation,
  useQuery,
  useQueryClient,
} from "@tanstack/react-query";
import toast from "react-hot-toast";

import { useAuthStore } from "../auth/store/authStore";
import { useWebSocketStore } from "../websocket/store/websocketStore";
import { websocketService } from "../../services/websocket/websocketService";
import {
  deleteDirectMessage,
  editDirectMessage,
  getDirectMessages,
  markDirectMessageRead,
  searchDirectMessages,
  sendDirectMessage,
  toggleDirectReaction,
  uploadDirectAttachment,
} from "./api";
import { useDirectStore } from "./store";
import type { DirectMessage } from "./types";
import EmojiPicker from "../chat/components/EmojiPicker";
import DirectAttachments from "./DirectAttachments";

export default function DirectMessageWindow() {
  const [content, setContent] = useState("");
  const [editingId, setEditingId] = useState<number | null>(null);
  const [editingContent, setEditingContent] = useState("");
  const [reactionPickerId, setReactionPickerId] = useState<number | null>(null);
  const [typingUser, setTypingUser] = useState<string | null>(null);
  const [file, setFile] = useState<File | null>(null);
  const [search, setSearch] = useState("");
  const typingTimeout = useRef<ReturnType<typeof setTimeout> | null>(null);
  const bottomRef = useRef<HTMLDivElement>(null);
  const conversation = useDirectStore(
    (state) => state.activeConversation
  );
  const currentUserId = useAuthStore(
    (state) => state.currentUser?.id
  );
  const connected = useWebSocketStore(
    (state) => state.connected
  );
  const queryClient = useQueryClient();
  const queryKey = useMemo(
    () =>
      [
        "direct-messages",
        conversation?.conversationId,
      ] as const,
    [conversation?.conversationId]
  );

  const { data: messages = [], isLoading } =
    useQuery({
      queryKey,
      queryFn: () =>
        getDirectMessages(
          conversation!.conversationId
        ),
      enabled: !!conversation,
    });
  const { data: searchResults = [] } = useQuery({
    queryKey: [
      "direct-search",
      conversation?.conversationId,
      search.trim(),
    ],
    queryFn: () =>
      searchDirectMessages(
        conversation!.conversationId,
        search.trim()
      ),
    enabled: !!conversation && search.trim().length >= 2,
  });

  useEffect(() => {
    if (!connected || !conversation) {
      return;
    }

    const update = (
      updater: (
        current: DirectMessage[]
      ) => DirectMessage[]
    ) =>
      queryClient.setQueryData<DirectMessage[]>(
        queryKey,
        (current = []) => updater(current)
      );

    const subscriptions =
      websocketService.subscribeToDirectConversation(
        conversation.conversationId,
        {
          onMessage: (message) =>
            update((current) =>
              current.some(
                (item) =>
                  item.messageId ===
                  message.messageId
              )
                ? current
                : [...current, message]
            ),
          onEdit: (event) =>
            update((current) =>
              current.map((item) =>
                item.messageId ===
                event.messageId
                  ? {
                      ...item,
                      content: event.content,
                      edited: true,
                      updatedAt:
                        event.updatedAt,
                    }
                  : item
              )
            ),
          onDelete: (event) =>
            update((current) =>
              current.map((item) =>
                item.messageId ===
                event.messageId
                  ? {
                      ...item,
                      content:
                        "This message was deleted.",
                      deleted: true,
                    }
                  : item
              )
            ),
          onReaction: (event) =>
            update((current) =>
              current.map((item) => {
                if (item.messageId !== event.directMessageId) return item;
                const existing = item.reactions.find(
                  (reaction) => reaction.emoji === event.emoji
                );
                const next = {
                  directMessageId: event.directMessageId,
                  emoji: event.emoji,
                  count: event.count,
                  reacted:
                    event.actorUserId === currentUserId
                      ? event.reacted
                      : existing?.reacted ?? false,
                };
                return {
                  ...item,
                  reactions:
                    next.count === 0
                      ? item.reactions.filter(
                          (reaction) => reaction.emoji !== next.emoji
                        )
                      : [
                          ...item.reactions.filter(
                            (reaction) => reaction.emoji !== next.emoji
                          ),
                          next,
                        ],
                };
              })
            ),
          onTyping: (event) => {
            if (event.userId === currentUserId) return;
            setTypingUser(event.typing ? event.username : null);
          },
        }
      );

    return () =>
      subscriptions.forEach(
        (subscription) =>
          subscription.unsubscribe()
      );
  }, [
    connected,
    conversation,
    queryClient,
    queryKey,
    currentUserId,
  ]);

  useEffect(() => {
    bottomRef.current?.scrollIntoView({
      behavior: "smooth",
    });
  }, [messages]);

  useEffect(() => {
    const last = messages[messages.length - 1];
    if (
      conversation &&
      last &&
      last.senderId !== currentUserId
    ) {
      void markDirectMessageRead(
        conversation.conversationId,
        last.messageId
      ).then(() =>
        queryClient.invalidateQueries({
          queryKey: ["conversations"],
        })
      );
    }
  }, [conversation, currentUserId, messages, queryClient]);

  const sendMutation = useMutation({
    mutationFn: async (message: string) => {
      const created = await sendDirectMessage(
        conversation!.conversationId,
        message
      );
      let attachmentFailed = false;
      if (file) {
        try {
          await uploadDirectAttachment(created.messageId, file);
        } catch {
          attachmentFailed = true;
        }
      }
      return { created, attachmentFailed };
    },
    onSuccess: ({ created: message, attachmentFailed }) => {
      queryClient.setQueryData<DirectMessage[]>(
        queryKey,
        (current = []) =>
          current.some(
            (item) =>
              item.messageId ===
              message.messageId
          )
            ? current
            : [...current, message]
      );
      setContent("");
      if (!attachmentFailed) {
        setFile(null);
      } else {
        toast.error(
          "Message sent, but the attachment failed. The file is still selected."
        );
      }
      void queryClient.invalidateQueries({
        queryKey: ["direct-attachments", message.messageId],
      });
      queryClient.invalidateQueries({
        queryKey: ["conversations"],
      });
    },
    onError: () =>
      toast.error("Unable to send message."),
  });

  if (!conversation) {
    return null;
  }

  return (
    <div className="flex min-h-0 flex-1 flex-col overflow-hidden rounded-3xl border border-white/10 bg-white/5">
      <div className="border-b border-white/10 px-6 py-4">
        <div className="flex flex-wrap items-center justify-between gap-3">
          <div>
            <p className="text-xs uppercase tracking-[0.2em] text-cyan-300">
              Direct message
            </p>
            <h2 className="mt-1 text-xl font-semibold text-white">
              {conversation.otherFirstName}{" "}
              {conversation.otherLastName}
              <span className="ml-2 text-sm font-normal text-slate-400">
                @{conversation.otherUsername}
              </span>
            </h2>
          </div>
          <label className="flex items-center gap-2 rounded-xl border border-white/10 bg-slate-950/70 px-3 py-2">
            <Search size={14} className="text-slate-500" />
            <input
              value={search}
              onChange={(event) => setSearch(event.target.value)}
              placeholder="Search conversation"
              className="w-44 bg-transparent text-sm text-white outline-none"
            />
          </label>
        </div>
        {search.trim().length >= 2 && (
          <div className="mt-3 max-h-40 overflow-y-auto rounded-xl border border-white/10 bg-slate-950 p-1">
            {searchResults.length === 0 ? (
              <p className="p-3 text-xs text-slate-500">No matching messages.</p>
            ) : (
              searchResults.map((result) => (
                <button
                  key={result.id}
                  type="button"
                  onClick={() => {
                    document
                      .getElementById(`direct-message-${result.id}`)
                      ?.scrollIntoView({ behavior: "smooth", block: "center" });
                    setSearch("");
                  }}
                  className="block w-full rounded-lg px-3 py-2 text-left hover:bg-white/5"
                >
                  <span className="block text-xs font-semibold text-cyan-200">
                    {result.senderUsername}
                  </span>
                  <span className="block truncate text-xs text-slate-300">
                    {result.content}
                  </span>
                </button>
              ))
            )}
          </div>
        )}
      </div>

      <div className="flex-1 overflow-y-auto px-6 py-5">
        {isLoading ? (
          <p className="text-sm text-slate-400">
            Loading conversation…
          </p>
        ) : messages.length === 0 ? (
          <p className="text-center text-sm text-slate-400">
            Start the conversation.
          </p>
        ) : (
          <div className="space-y-3">
            {messages.map((message) => {
              const own =
                message.senderId ===
                currentUserId;
              return (
                <div
                  key={message.messageId}
                  id={`direct-message-${message.messageId}`}
                  className={`group flex ${
                    own
                      ? "justify-end"
                      : "justify-start"
                  }`}
                >
                  <div
                    className={`max-w-[75%] rounded-2xl border px-4 py-3 ${
                      own
                        ? "border-cyan-400/20 bg-cyan-500/15"
                        : "border-white/10 bg-white/5"
                    }`}
                  >
                    <div className="mb-1 flex items-center gap-2 text-xs text-slate-400">
                      <span>
                        {message.senderUsername}
                      </span>
                      {message.edited && (
                        <span>(edited)</span>
                      )}
                    </div>
                    <p
                      className={`text-sm ${
                        message.deleted
                          ? "italic text-slate-500"
                          : "text-slate-100"
                      }`}
                    >
                      {editingId === message.messageId ? (
                        <input
                          autoFocus
                          value={editingContent}
                          onChange={(event) => setEditingContent(event.target.value)}
                          onKeyDown={(event) => {
                            if (event.key === "Escape") setEditingId(null);
                            if (event.key === "Enter" && editingContent.trim()) {
                              void editDirectMessage(
                                message.messageId,
                                editingContent.trim()
                              ).then(() => setEditingId(null));
                            }
                          }}
                          className="w-full rounded-lg border border-cyan-400/30 bg-slate-950 px-3 py-2 text-white"
                        />
                      ) : (
                        message.content
                      )}
                    </p>
                    <DirectAttachments messageId={message.messageId} />
                    {message.reactions.length > 0 && (
                      <div className="mt-2 flex flex-wrap gap-1.5">
                        {message.reactions.map((reaction) => (
                          <button
                            key={reaction.emoji}
                            type="button"
                            onClick={() => {
                              void toggleDirectReaction(
                                message.messageId,
                                reaction.emoji
                              );
                            }}
                            className={`rounded-full border px-2 py-0.5 text-xs ${
                              reaction.reacted
                                ? "border-cyan-400 bg-cyan-500/20"
                                : "border-white/10 bg-white/5"
                            }`}
                          >
                            {reaction.emoji} {reaction.count}
                          </button>
                        ))}
                      </div>
                    )}
                    {!message.deleted && (
                      <div className="relative mt-2 flex gap-2 opacity-70 transition group-hover:opacity-100">
                        <button
                          type="button"
                          aria-label="Add reaction"
                          onClick={() => setReactionPickerId(
                            reactionPickerId === message.messageId
                              ? null
                              : message.messageId
                          )}
                          className="text-xs text-slate-300 hover:text-cyan-200"
                        >
                          🙂
                        </button>
                        {reactionPickerId === message.messageId && (
                          <EmojiPicker
                            onSelect={(emoji) => {
                              setReactionPickerId(null);
                              void toggleDirectReaction(message.messageId, emoji)
                                .catch(() => toast.error("Unable to react."));
                            }}
                          />
                        )}
                        {own && (
                          <>
                            <button
                              type="button"
                              aria-label="Edit direct message"
                              onClick={() => {
                                setEditingId(message.messageId);
                                setEditingContent(message.content);
                              }}
                              className="text-slate-400 hover:text-cyan-200"
                            >
                              <Pencil size={13} />
                            </button>
                            <button
                              type="button"
                              aria-label="Delete direct message"
                              onClick={() => {
                                if (!window.confirm("Delete this message?")) return;
                                void deleteDirectMessage(
                                  message.messageId
                                ).catch(() =>
                                  toast.error(
                                    "Unable to delete message."
                                  )
                                );
                              }}
                              className="text-slate-400 hover:text-red-300"
                            >
                              <Trash2 size={13} />
                            </button>
                          </>
                        )}
                      </div>
                    )}
                  </div>
                </div>
              );
            })}
            <div ref={bottomRef} />
          </div>
        )}
      </div>

      {typingUser && (
        <p className="border-t border-white/5 px-6 py-2 text-xs text-cyan-300">
          {typingUser} is typing…
        </p>
      )}

      <form
        onSubmit={(event) => {
          event.preventDefault();
          const trimmed = content.trim();
          if (trimmed || file) {
            sendMutation.mutate(
              trimmed || `Shared ${file!.name}`
            );
          }
        }}
        className="border-t border-white/10 p-4"
      >
        <div className="flex items-end gap-3">
          <label
            aria-label="Attach file"
            className="flex h-11 w-11 shrink-0 cursor-pointer items-center justify-center rounded-xl border border-white/10 bg-white/5 text-slate-300"
          >
            <Paperclip size={17} />
            <input
              type="file"
              className="sr-only"
              onChange={(event) => {
                const selected = event.target.files?.[0] ?? null;
                if (selected && selected.size > 25 * 1024 * 1024) {
                  toast.error("Files must be 25 MB or smaller.");
                  return;
                }
                setFile(selected);
              }}
            />
          </label>
          <textarea
            rows={1}
            maxLength={4000}
            value={content}
            onKeyDown={(event) => {
              if (event.key === "Enter" && !event.shiftKey) {
                event.preventDefault();
                const trimmed = content.trim();
                if (trimmed || file) {
                  sendMutation.mutate(
                    trimmed || `Shared ${file!.name}`
                  );
                }
              }
            }}
            onChange={(event) => {
              setContent(event.target.value);
              if (connected) {
                websocketService.sendDirectTyping(
                  conversation.conversationId,
                  true
                );
                if (typingTimeout.current) clearTimeout(typingTimeout.current);
                typingTimeout.current = setTimeout(
                  () => websocketService.sendDirectTyping(
                    conversation.conversationId,
                    false
                  ),
                  1500
                );
              }
            }}
            placeholder={`Message @${conversation.otherUsername}`}
            className="max-h-40 min-h-11 min-w-0 flex-1 resize-y rounded-xl border border-white/10 bg-white/5 px-4 py-3 text-sm text-white outline-none focus:border-cyan-400/40"
          />
          <button
            type="submit"
            disabled={
              (!content.trim() && !file) ||
              sendMutation.isPending
            }
            className="h-11 shrink-0 rounded-xl bg-cyan-500 px-4 text-slate-950 disabled:opacity-50"
          >
            <SendHorizontal size={18} />
          </button>
        </div>
        {file && (
          <div className="mt-2 flex items-center gap-2 text-xs text-cyan-200">
            <Paperclip size={13} />
            <span className="max-w-xs truncate">{file.name}</span>
            <button
              type="button"
              aria-label="Remove attachment"
              onClick={() => setFile(null)}
            >
              <X size={13} />
            </button>
          </div>
        )}
      </form>
    </div>
  );
}
