import { Building2, Hash, Lock } from "lucide-react";

import { useWorkspaceStore } from "../features/workspace/store/workspaceStore";
import { useChannelStore } from "../features/channel/store/channelStore";

import ChatWindow from "../features/chat/components/ChatWindow";

import MainLayout from "../layouts/MainLayout";
import { useDirectStore } from "../features/direct/store";
import DirectMessageWindow from "../features/direct/DirectMessageWindow";

export default function DashboardPage() {
  const activeWorkspace = useWorkspaceStore(
    (state) => state.activeWorkspace
  );

  const activeChannel = useChannelStore(
    (state) => state.activeChannel
  );

  const activeConversation = useDirectStore(
    (state) => state.activeConversation
  );

  return (
    <MainLayout>
      <div className="flex min-h-0 h-full flex-1 flex-col gap-6">
        {/* Workspace Header */}
        <div className="rounded-3xl border border-[var(--wave-border)] bg-gradient-to-r from-slate-900 to-slate-900/70 p-8 shadow-[var(--shadow-xl)]">
          <div className="flex flex-col gap-6 lg:flex-row lg:items-center lg:justify-between">
            <div className="flex items-start gap-5">
              <div className="flex h-16 w-16 items-center justify-center rounded-2xl bg-gradient-to-br from-cyan-500 to-blue-600 shadow-[var(--shadow-glow)]">
                <Building2
                  size={30}
                  className="text-white"
                />
              </div>

              <div>
                <p className="text-xs font-semibold uppercase tracking-[0.3em] text-cyan-400">
                  Workspace
                </p>

                <h1 className="mt-2 text-4xl font-bold tracking-tight text-white">
                  {activeWorkspace?.name ?? "Wave"}
                </h1>

                <p className="mt-3 max-w-xl text-sm leading-6 text-[var(--wave-text-secondary)]">
                  Collaborate with your team in real time,
                  organize conversations, and keep work in
                  one place.
                </p>
              </div>
            </div>

            {(activeChannel ||
              activeConversation) && (
              <div className="glass-card rounded-premium border border-cyan-400/20 px-6 py-5">
                <div className="flex items-center gap-3">
                  {activeConversation ? (
                    <Building2
                      size={18}
                      className="text-cyan-300"
                    />
                  ) : activeChannel?.isPrivate ? (
                    <Lock
                      size={18}
                      className="text-cyan-300"
                    />
                  ) : (
                    <Hash
                      size={18}
                      className="text-cyan-300"
                    />
                  )}

                  <div>
                    <p className="font-semibold text-white">
                      {activeConversation
                        ? activeConversation.otherUsername
                        : activeChannel?.name}
                    </p>

                    <p className="mt-1 text-xs text-cyan-200">
                      {activeConversation
                        ? "Direct Message"
                        : activeChannel?.isPrivate
                          ? "Private Channel"
                          : "Public Channel"}
                    </p>
                  </div>
                </div>
              </div>
            )}
          </div>
        </div>

        {/* Chat */}
        <div className="flex min-h-0 flex-1">
          {activeConversation ? (
            <DirectMessageWindow />
          ) : (
            <ChatWindow />
          )}
        </div>
      </div>
    </MainLayout>
  );
}
