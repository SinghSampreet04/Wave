import { useEffect } from "react";
import type { ReactNode } from "react";

import AppSidebar from "../components/layout/AppSidebar";
import TopNavigation from "../components/layout/TopNavigation";

import { useAuthStore } from "../features/auth/store/authStore";
import { useWebSocket } from "../features/websocket/hooks/useWebSocket";

interface Props {
  children: ReactNode;
}

export default function MainLayout({
  children,
}: Props) {
  const accessToken = useAuthStore(
    (state) => state.accessToken
  );

  const {
    connected,
    connecting,
    connect,
    disconnect,
  } = useWebSocket();

  useEffect(() => {
    if (!accessToken) {
      return;
    }

    if (!connected && !connecting) {
      connect(accessToken);
    }
  }, [
    accessToken,
    connected,
    connecting,
    connect,
  ]);

  useEffect(() => {
    return () => {
      disconnect();
    };
  }, [disconnect]);

  return (
    <div className="relative flex h-screen overflow-hidden bg-[#040814] text-white">
      {/* Ambient Background */}
      <div className="pointer-events-none absolute inset-0 overflow-hidden">
        <div className="absolute -left-40 -top-40 h-[520px] w-[520px] rounded-full bg-cyan-500/10 blur-[140px]" />

        <div className="absolute right-[-180px] top-1/3 h-[520px] w-[520px] rounded-full bg-blue-600/10 blur-[170px]" />

        <div className="absolute bottom-[-220px] left-1/3 h-[620px] w-[620px] rounded-full bg-violet-600/10 blur-[180px]" />

        <div className="absolute inset-0 bg-[radial-gradient(circle_at_top,rgba(255,255,255,0.04),transparent_55%)]" />

        <div className="absolute inset-0 bg-[linear-gradient(rgba(255,255,255,0.015)_1px,transparent_1px),linear-gradient(90deg,rgba(255,255,255,0.015)_1px,transparent_1px)] bg-[size:48px_48px] opacity-20" />
      </div>

      <AppSidebar />

      <div className="relative z-10 flex min-h-0 flex-1 flex-col overflow-hidden">
        <TopNavigation />

        <main className="relative min-h-0 flex-1 overflow-y-auto px-8 py-8">
          <div className="relative flex min-h-full flex-col rounded-[30px] border border-white/10 bg-white/[0.025] shadow-[0_25px_80px_rgba(0,0,0,0.45)] backdrop-blur-2xl">
            <div className="absolute inset-0 rounded-[30px] bg-gradient-to-br from-white/[0.05] via-transparent to-slate-900/40" />

            <div className="relative flex min-h-0 flex-1 flex-col p-8">
              {children}
            </div>
          </div>
        </main>
      </div>
    </div>
  );
}