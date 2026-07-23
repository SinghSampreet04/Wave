import { Sparkles } from "lucide-react";
import { useNavigate } from "react-router-dom";

import UserMenu from "../navigation/UserMenu";

import { useAuthStore } from "../../features/auth/store/authStore";
import NotificationCenter from "../../features/notification/NotificationCenter";
import ChannelSearch from "../../features/search/ChannelSearch";
import { logoutSession } from "../../features/auth/api/auth";
import AccountSettingsModal from "../../features/auth/components/AccountSettingsModal";
import { useState } from "react";

export default function TopNavigation() {
  const navigate = useNavigate();
  const [settingsOpen, setSettingsOpen] = useState(false);

  const logout = useAuthStore(
    (state) => state.logout
  );

  const currentUser = useAuthStore(
    (state) => state.currentUser
  );

  const userName =
    currentUser?.username ?? "User";

  const userEmail = currentUser?.email;

  async function handleLogout() {
    try {
      await logoutSession();
    } catch {
      // Local logout still completes if the server is unavailable.
    }
    logout();
    navigate("/login", {
      replace: true,
    });
  }

  return (
    <>
    <header className="relative z-20 px-6 pt-6 lg:px-8">
      <div className="glass-panel flex h-20 items-center justify-between rounded-premium border border-[var(--wave-border)] px-6 shadow-[var(--shadow-xl)]">
        {/* Left */}
        <div className="flex min-w-0 flex-1 items-center gap-6">
          <div className="glass-card hidden items-center gap-3 rounded-premium border border-cyan-400/20 px-4 py-2 xl:flex">
            <div className="flex h-9 w-9 items-center justify-center rounded-xl bg-gradient-to-br from-cyan-500 to-blue-600 shadow-[var(--shadow-glow)]">
              <Sparkles
                size={18}
                className="text-white"
              />
            </div>

            <div>
              <p className="text-[11px] font-semibold uppercase tracking-[0.22em] text-cyan-300">
                Workspace
              </p>

              <p className="text-sm font-medium text-[var(--wave-text)]">
                Wave Collaboration
              </p>
            </div>
          </div>

          <div className="flex-1 max-w-xl">
            <ChannelSearch />
          </div>
        </div>

        {/* Right */}
        <div className="ml-6 flex items-center gap-4">
          <NotificationCenter />

          <UserMenu
            name={userName}
            email={userEmail}
            onLogout={handleLogout}
            onProfile={() => setSettingsOpen(true)}
            onSettings={() => setSettingsOpen(true)}
          />
        </div>
      </div>
    </header>
    {settingsOpen && (
      <AccountSettingsModal
        onClose={() => setSettingsOpen(false)}
      />
    )}
    </>
  );
}
