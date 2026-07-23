import { useState } from "react";
import {
  Building2,
  Hash,
  Lock,
  UserPlus,
} from "lucide-react";

import { useChannelStore } from "../../channel/store/channelStore";
import { useWorkspaceStore } from "../../workspace/store/workspaceStore";
import { useWorkspaceUIStore } from "../../workspace/store/workspaceUIStore";
import InviteChannelDialog from "../../channel/components/InviteChannelDialog";
import PinnedMessages from "./PinnedMessages";

export default function ChatHeader({
  compact = false,
}: {
  compact?: boolean;
}) {
  const [channelInviteOpen, setChannelInviteOpen] =
    useState(false);
  const workspace = useWorkspaceStore(
    (state) => state.activeWorkspace
  );

  const channel = useChannelStore(
    (state) => state.activeChannel
  );
  const openWorkspaceInvite =
    useWorkspaceUIStore(
      (state) =>
        state.openInviteMemberModal
    );

  return (
    <>
    <header
      className={`relative z-30 border-b border-white/10 bg-slate-950/95 backdrop-blur-xl ${
        compact
          ? "px-5 py-4"
          : "px-8 py-5"
      }`}
    >
      <div className="flex min-w-0 flex-wrap items-center gap-4">
        <div className="flex min-w-0 items-center gap-4">
          <div className="flex h-12 w-12 flex-shrink-0 items-center justify-center rounded-2xl bg-gradient-to-br from-cyan-500 to-blue-600 shadow-lg shadow-cyan-500/20">
            {channel?.isPrivate ? (
              <Lock
                size={20}
                className="text-white"
              />
            ) : (
              <Hash
                size={20}
                className="text-white"
              />
            )}
          </div>

          <div className="min-w-0">
            <div className="flex items-center gap-2">
              <h2 className="truncate text-xl font-bold tracking-tight text-white">
                {channel?.name ?? "Select a Channel"}
              </h2>

              {channel && (
                <span className="rounded-full border border-cyan-400/20 bg-cyan-500/10 px-2.5 py-1 text-[11px] font-medium text-cyan-300">
                  {channel.isPrivate
                    ? "Private"
                    : "Public"}
                </span>
              )}
            </div>

            <div className="mt-2 flex min-w-0 items-center gap-2 text-sm text-slate-400">
              <Building2 size={14} />

              <span className="truncate">
                {workspace?.name ??
                  "No workspace selected"}
              </span>
            </div>
          </div>
        </div>

        {channel && (
          <div className="ml-auto flex min-w-0 items-center gap-2">
            <button
              type="button"
              aria-label={
                channel.isPrivate
                  ? "Invite to channel"
                  : "Invite to workspace"
              }
              onClick={() => {
                if (channel.isPrivate) {
                  setChannelInviteOpen(true);
                } else {
                  openWorkspaceInvite();
                }
              }}
              className="flex items-center gap-2 rounded-xl border border-cyan-400/20 bg-cyan-500/10 px-3 py-2 text-sm font-semibold text-cyan-200 transition hover:border-cyan-400/40 hover:bg-cyan-500/15"
            >
              <UserPlus size={15} />
              <span
                className={
                  compact
                    ? "sr-only"
                    : "hidden 2xl:inline"
                }
              >
                {channel.isPrivate
                  ? "Invite to channel"
                  : "Invite to workspace"}
              </span>
            </button>
            <PinnedMessages
              channelId={channel.id}
              compact={compact}
            />
          <div
            className={`rounded-2xl border border-white/10 bg-white/[0.03] px-4 py-3 ${
              compact
                ? "hidden"
                : "hidden 2xl:block"
            }`}
          >
            <p className="text-xs font-semibold uppercase tracking-[0.2em] text-slate-500">
              Channel
            </p>

            <p className="mt-1 text-sm text-slate-200">
              {channel.isPrivate
                ? "Private team discussion"
                : "Open team conversation"}
            </p>
          </div>
          </div>
        )}
      </div>
    </header>
    {channelInviteOpen &&
      channel?.isPrivate && (
        <InviteChannelDialog
          channel={channel}
          onClose={() =>
            setChannelInviteOpen(false)
          }
        />
      )}
    </>
  );
}
