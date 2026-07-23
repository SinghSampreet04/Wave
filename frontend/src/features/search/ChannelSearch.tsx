import { useEffect, useRef, useState } from "react";
import { Filter, Paperclip, Smile } from "lucide-react";
import { useQuery } from "@tanstack/react-query";

import api from "../../api/axios";
import SearchBar from "../../components/navigation/SearchBar";
import useClickOutside from "../../hooks/useClickOutside";
import useDebounce from "../../hooks/useDebounce";
import { useChannelStore } from "../channel/store/channelStore";
import { getWorkspaceChannels } from "../channel/api/channel";
import { useWorkspaceStore } from "../workspace/store/workspaceStore";
import { getWorkspaces } from "../workspace/api/workspaceApi";
import { useDirectStore } from "../direct/store";

interface SearchResult {
  messageId: number;
  channelId: number;
  channelName: string;
  workspaceId: number;
  workspaceName: string;
  senderId: number;
  senderUsername: string;
  content: string;
  createdAt: string;
  hasAttachments: boolean;
  hasReactions: boolean;
}

interface SearchPage {
  items: SearchResult[];
  hasMore: boolean;
  totalElements: number;
}

export default function ChannelSearch() {
  const [value, setValue] = useState("");
  const [focused, setFocused] = useState(false);
  const [filtersOpen, setFiltersOpen] = useState(false);
  const [allWorkspace, setAllWorkspace] = useState(true);
  const [hasAttachment, setHasAttachment] = useState(false);
  const [hasReaction, setHasReaction] = useState(false);
  const inputRef = useRef<HTMLInputElement>(null);
  const containerRef = useRef<HTMLDivElement>(null);
  const activeChannel = useChannelStore((state) => state.activeChannel);
  const activeWorkspace = useWorkspaceStore((state) => state.activeWorkspace);
  const setActiveWorkspace = useWorkspaceStore((state) => state.setActiveWorkspace);
  const setActiveChannel = useChannelStore((state) => state.setActiveChannel);
  const setActiveConversation = useDirectStore((state) => state.setActiveConversation);
  const debouncedValue = useDebounce(value.trim(), 250);

  useClickOutside(containerRef, () => setFocused(false));

  useEffect(() => {
    const onKeyDown = (event: KeyboardEvent) => {
      if ((event.metaKey || event.ctrlKey) && event.key.toLowerCase() === "k") {
        event.preventDefault();
        inputRef.current?.focus();
        setFocused(true);
      }
      if (event.key === "Escape") setFocused(false);
    };
    window.addEventListener("keydown", onKeyDown);
    return () => window.removeEventListener("keydown", onKeyDown);
  }, []);

  const { data, isFetching, isError } = useQuery({
    queryKey: [
      "message-search",
      debouncedValue,
      activeWorkspace?.id,
      allWorkspace ? null : activeChannel?.id,
      hasAttachment,
      hasReaction,
    ],
    queryFn: async () => {
      const response = await api.get<SearchPage>("/v1/messages/search", {
        params: {
          q: debouncedValue,
          workspaceId: activeWorkspace?.id,
          channelId: allWorkspace ? undefined : activeChannel?.id,
          hasAttachment: hasAttachment || undefined,
          hasReaction: hasReaction || undefined,
          size: 30,
        },
      });
      return response.data;
    },
    enabled: !!activeWorkspace && debouncedValue.length >= 2,
  });

  const activateResult = async (result: SearchResult) => {
    const workspaces = await getWorkspaces();
    const workspace = workspaces.find((item) => item.id === result.workspaceId);
    if (!workspace) return;
    setActiveWorkspace(workspace);
    const channels = await getWorkspaceChannels(workspace.id);
    const channel = channels.find((item) => item.id === result.channelId);
    if (!channel) return;
    setActiveConversation(null);
    setActiveChannel(channel);
    setFocused(false);
    window.setTimeout(() => {
      const element = document.getElementById(`message-${result.messageId}`);
      element?.scrollIntoView({ behavior: "smooth", block: "center" });
      element?.focus({ preventScroll: true });
    }, 350);
  };

  const showResults = focused && debouncedValue.length >= 2;

  return (
    <div ref={containerRef} className="relative">
      <div className="flex items-center gap-2">
        <div className="min-w-0 flex-1">
          <SearchBar
            ref={inputRef}
            value={value}
            disabled={!activeWorkspace}
            onFocus={() => setFocused(true)}
            onChange={(event) => setValue(event.target.value)}
            placeholder={
              activeWorkspace
                ? `Search ${activeWorkspace.name}…`
                : "Select a workspace to search…"
            }
          />
        </div>
        <button
          type="button"
          aria-label="Search filters"
          onClick={() => {
            setFocused(true);
            setFiltersOpen((current) => !current);
          }}
          className="rounded-xl border border-white/10 bg-slate-900 p-3 text-slate-300 hover:text-white"
        >
          <Filter size={17} />
        </button>
      </div>

      {focused && filtersOpen && (
        <div className="absolute left-0 right-0 top-14 z-[90] flex flex-wrap gap-4 rounded-2xl border border-white/15 bg-slate-950 p-4 shadow-2xl">
          <label className="flex items-center gap-2 text-sm text-slate-300">
            <input
              type="checkbox"
              checked={allWorkspace}
              onChange={(event) => setAllWorkspace(event.target.checked)}
            />
            All workspace channels
          </label>
          <label className="flex items-center gap-2 text-sm text-slate-300">
            <input
              type="checkbox"
              checked={hasAttachment}
              onChange={(event) => setHasAttachment(event.target.checked)}
            />
            Has attachment
          </label>
          <label className="flex items-center gap-2 text-sm text-slate-300">
            <input
              type="checkbox"
              checked={hasReaction}
              onChange={(event) => setHasReaction(event.target.checked)}
            />
            Has reaction
          </label>
        </div>
      )}

      {showResults && (
        <div className={`absolute left-0 right-0 z-50 max-h-96 overflow-y-auto rounded-2xl border border-white/10 bg-slate-950/98 p-2 shadow-2xl ${
          filtersOpen ? "top-32" : "top-14"
        }`}>
          {isFetching ? (
            <p className="px-4 py-6 text-center text-sm text-slate-400">Searching…</p>
          ) : isError ? (
            <p className="px-4 py-6 text-center text-sm text-red-300">
              Search is temporarily unavailable.
            </p>
          ) : !data?.items.length ? (
            <p className="px-4 py-6 text-center text-sm text-slate-400">
              No matching messages.
            </p>
          ) : (
            data.items.map((result) => (
              <button
                key={result.messageId}
                type="button"
                onClick={() => void activateResult(result)}
                className="block w-full rounded-xl px-3 py-3 text-left transition hover:bg-white/5"
              >
                <span className="flex items-center justify-between gap-3">
                  <span className="text-sm font-semibold text-cyan-200">
                    {result.workspaceName} / #{result.channelName} · {result.senderUsername}
                  </span>
                  <span className="text-[11px] text-slate-500">
                    {new Date(result.createdAt).toLocaleString()}
                  </span>
                </span>
                <span className="mt-1 flex items-center gap-2">
                  <span className="min-w-0 flex-1 truncate text-sm text-slate-300">
                    {result.content}
                  </span>
                  {result.hasAttachments && <Paperclip size={13} />}
                  {result.hasReactions && <Smile size={13} />}
                </span>
              </button>
            ))
          )}
        </div>
      )}
    </div>
  );
}
