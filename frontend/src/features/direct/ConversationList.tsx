import { useState } from "react";
import { MessageCircle, Plus, X } from "lucide-react";
import {
  useMutation,
  useQuery,
  useQueryClient,
} from "@tanstack/react-query";

import {
  createConversation,
  getConversations,
  searchUsers,
} from "./api";
import { useDirectStore } from "./store";
import useDebounce from "../../hooks/useDebounce";
import { useChannelStore } from "../channel/store/channelStore";
import type { Conversation } from "./types";

export default function ConversationList() {
  const [creating, setCreating] = useState(false);
  const [query, setQuery] = useState("");
  const debounced = useDebounce(
    query.trim(),
    250
  );
  const queryClient = useQueryClient();
  const active = useDirectStore(
    (state) => state.activeConversation
  );
  const setActive = useDirectStore(
    (state) => state.setActiveConversation
  );
  const clearChannel = useChannelStore(
    (state) => state.clearActiveChannel
  );

  const { data = [] } = useQuery({
    queryKey: ["conversations"],
    queryFn: getConversations,
  });

  const { data: users = [] } = useQuery({
    queryKey: ["user-search", debounced],
    queryFn: () => searchUsers(debounced),
    enabled: debounced.length >= 2,
  });

  const createMutation = useMutation({
    mutationFn: createConversation,
    onSuccess: (conversation) => {
      queryClient.setQueryData<Conversation[]>(
        ["conversations"],
        (current = []) =>
          current.some(
            (item) =>
              item.conversationId ===
              conversation.conversationId
          )
            ? current
            : [conversation, ...current]
      );
      clearChannel();
      setActive(conversation);
      setCreating(false);
      setQuery("");
    },
  });

  return (
    <section className="glass-card rounded-premium border border-[var(--wave-border)] p-4">
      <div className="mb-4 flex items-center justify-between">
        <div>
          <p className="text-[11px] font-semibold uppercase tracking-[0.22em] text-[var(--wave-text-muted)]">
            Direct messages
          </p>
          <p className="mt-1 text-xs text-[var(--wave-text-muted)]">
            Private conversations
          </p>
        </div>
        <button
          type="button"
          aria-label="Start conversation"
          onClick={() =>
            setCreating((value) => !value)
          }
          className="flex items-center gap-1.5 rounded-xl px-2.5 py-2 text-xs font-semibold text-slate-400 transition hover:bg-white/5 hover:text-cyan-300"
        >
          {creating ? (
            <>
              <X size={15} />
              Close
            </>
          ) : (
            <>
              <Plus size={15} />
              New
            </>
          )}
        </button>
      </div>

      {creating && (
        <div className="mb-3">
          <input
            value={query}
            onChange={(event) =>
              setQuery(event.target.value)
            }
            placeholder="Find a teammate…"
            className="w-full rounded-xl border border-white/10 bg-white/5 px-3 py-2 text-sm text-white outline-none focus:border-cyan-400/40"
          />
          {users.length > 0 && (
            <div className="mt-2 max-h-40 overflow-y-auto rounded-xl border border-white/10 bg-slate-950">
              {users.map((user) => (
                <button
                  key={user.id}
                  type="button"
                  disabled={
                    createMutation.isPending
                  }
                  onClick={() =>
                    createMutation.mutate(
                      user.id
                    )
                  }
                  className="block w-full px-3 py-2 text-left text-sm text-slate-200 hover:bg-white/5"
                >
                  {user.firstName}{" "}
                  {user.lastName}
                  <span className="ml-2 text-xs text-slate-500">
                    @{user.username}
                  </span>
                </button>
              ))}
            </div>
          )}
        </div>
      )}

      <div className="space-y-1">
        {data.length === 0 && !creating ? (
          <p className="py-2 text-xs text-slate-500">
            No conversations yet.
          </p>
        ) : (
          data.map((conversation) => (
            <button
              key={conversation.conversationId}
              type="button"
              onClick={() => {
                clearChannel();
                setActive(conversation);
              }}
              className={`flex w-full items-center gap-2 rounded-xl px-3 py-2 text-left text-sm transition ${
                active?.conversationId ===
                conversation.conversationId
                  ? "bg-cyan-500/15 text-cyan-100"
                  : "text-slate-300 hover:bg-white/5"
              }`}
            >
              <MessageCircle
                size={15}
                className="shrink-0"
              />
              <span className="min-w-0">
                <span className="block truncate font-medium">
                  {conversation.otherUsername}
                </span>
                {conversation.lastMessage && (
                  <span className="block truncate text-[11px] text-slate-500">
                    {conversation.lastMessage}
                  </span>
                )}
              </span>
              {conversation.unreadCount > 0 && (
                <span className="ml-auto min-w-5 rounded-full bg-cyan-400 px-1.5 text-center text-[10px] font-bold leading-5 text-slate-950">
                  {conversation.unreadCount > 99
                    ? "99+"
                    : conversation.unreadCount}
                </span>
              )}
            </button>
          ))
        )}
      </div>
    </section>
  );
}
