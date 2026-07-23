import {
  ChevronRight,
  Plus,
  Sparkles,
  UserPlus,
  Wifi,
} from "lucide-react";

import Sidebar from "./Sidebar";

import WaveBrand from "../branding/WaveBrand";
import Button from "../ui/Button";

import WorkspaceList from "../../features/workspace/components/WorkspaceList";
import CreateWorkspaceModal from "../../features/workspace/components/CreateWorkspaceModal";
import InviteMemberDialog from "../../features/workspace/components/InviteMemberDialog";

import ChannelList from "../../features/channel/components/ChannelList";
import CreateChannelModal from "../../features/channel/components/CreateChannelModal";

import StatusBadge from "../shared/StatusBadge";

import { useWorkspaceUIStore } from "../../features/workspace/store/workspaceUIStore";
import { useChannelUIStore } from "../../features/channel/store/channelUIStore";
import { useWorkspaceStore } from "../../features/workspace/store/workspaceStore";
import ConversationList from "../../features/direct/ConversationList";
import { useWebSocketStore } from "../../features/websocket/store/websocketStore";

export default function AppSidebar() {
  const openCreateWorkspaceModal =
    useWorkspaceUIStore(
      (state) =>
        state.openCreateWorkspaceModal
    );

  const openCreateChannelModal =
    useChannelUIStore(
      (state) =>
        state.openCreateChannelModal
    );

  const openInviteMemberModal =
    useWorkspaceUIStore(
      (state) =>
        state.openInviteMemberModal
    );

  const activeWorkspace =
    useWorkspaceStore(
      (state) => state.activeWorkspace
    );
  const connected = useWebSocketStore((state) => state.connected);
  const connecting = useWebSocketStore((state) => state.connecting);

  return (
    <>
      <Sidebar
        header={
          <div className="glass-panel relative overflow-hidden rounded-premium border border-[var(--wave-border)] p-5 shadow-[var(--shadow-xl)]">
            <div className="absolute -right-8 -top-8 h-24 w-24 rounded-full bg-cyan-500/20 blur-3xl" />

            <div className="relative">
              <WaveBrand compact />

              <p className="mt-4 text-sm leading-6 text-[var(--wave-text-secondary)]">
                Collaborate with your team
                across workspaces, channels
                and live conversations.
              </p>

              <div className="glass-card mt-5 flex items-center justify-between rounded-premium border border-cyan-400/20 px-4 py-3">
                <div>
                  <p className="text-[11px] font-semibold uppercase tracking-[0.18em] text-cyan-300">
                    Workspace
                  </p>

                  <p className="mt-1 text-sm font-semibold text-[var(--wave-text)]">
                    {activeWorkspace?.name ??
                      "No workspace selected"}
                  </p>
                </div>

                <Sparkles
                  size={18}
                  className="text-cyan-300"
                />
              </div>
            </div>
          </div>
        }
      >
        <div className="flex flex-col gap-5 px-4 pb-4">
          {/* Workspaces */}
          <section className="glass-card rounded-premium border border-[var(--wave-border)] p-4">
            <div className="mb-5 flex items-center justify-between">
              <div>
                <p className="text-[11px] font-semibold uppercase tracking-[0.22em] text-[var(--wave-text-muted)]">
                  Workspaces
                </p>

                <p className="mt-1 text-xs text-[var(--wave-text-muted)]">
                  Organize your teams
                </p>
              </div>

              <Button
                variant="ghost"
                size="sm"
                onClick={
                  openCreateWorkspaceModal
                }
                aria-label="Create workspace"
              >
                <Plus size={16} />
              </Button>
            </div>

            <WorkspaceList />

            {activeWorkspace && (
              <button
                type="button"
                onClick={openInviteMemberModal}
                className="mt-3 flex w-full items-center justify-center gap-2 rounded-xl border border-cyan-400/20 bg-cyan-500/10 px-3 py-2 text-xs font-semibold text-cyan-200 transition hover:border-cyan-400/40 hover:bg-cyan-500/15"
              >
                <UserPlus size={15} />
                Invite people
              </button>
            )}
          </section>

          {/* Channels */}
          <section className="glass-card rounded-premium border border-[var(--wave-border)] p-4">
            <div className="mb-5 flex items-center justify-between">
              <div>
                <p className="text-[11px] font-semibold uppercase tracking-[0.22em] text-[var(--wave-text-muted)]">
                  Channels
                </p>

                <p className="mt-1 text-xs text-[var(--wave-text-muted)]">
                  Team conversations
                </p>
              </div>

              <Button
                variant="ghost"
                size="sm"
                onClick={
                  openCreateChannelModal
                }
                disabled={!activeWorkspace}
                aria-label="Create channel"
              >
                <Plus size={16} />
              </Button>
            </div>

            <ChannelList />
          </section>

          <ConversationList />

          {/* Footer */}
          <div className="mt-auto pt-2">
            <div className={`rounded-premium border p-4 ${
              connected
                ? "border-emerald-500/20 bg-emerald-500/5"
                : connecting
                  ? "border-amber-500/20 bg-amber-500/5"
                  : "border-red-500/20 bg-red-500/5"
            }`}>
              <div className="flex items-center justify-between">
                <div className="flex items-center gap-3">
                  <div className={`flex h-9 w-9 items-center justify-center rounded-xl ${
                    connected ? "bg-emerald-500/15" : "bg-slate-500/15"
                  }`}>
                    <Wifi
                      size={16}
                      className={connected ? "text-emerald-400" : "text-slate-400"}
                    />
                  </div>

                  <div>
                    <StatusBadge variant={connected ? "success" : "default"}>
                      {connected ? "Connected" : connecting ? "Reconnecting" : "Offline"}
                    </StatusBadge>

                    <p className="mt-1 text-xs text-[var(--wave-text-muted)]">
                      {connected ? "Real-time sync active" : "Messages will sync when reconnected"}
                    </p>
                  </div>
                </div>

                <ChevronRight
                  size={18}
                  className="text-[var(--wave-text-muted)]"
                />
              </div>
            </div>
          </div>
        </div>
      </Sidebar>

      <CreateWorkspaceModal />
      <CreateChannelModal />
      <InviteMemberDialog />
    </>
  );
}
