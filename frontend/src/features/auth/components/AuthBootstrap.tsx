import { useEffect, useRef, type ReactNode } from "react";

import { refreshAccessToken } from "../../../api/axios";
import { getToken } from "../../../lib/token";
import { getCurrentUser } from "../api/auth";
import { useAuthStore } from "../store/authStore";

export default function AuthBootstrap({
  children,
}: {
  children: ReactNode;
}) {
  const started = useRef(false);
  const initialized = useAuthStore((state) => state.initialized);
  const setCurrentUser = useAuthStore((state) => state.setCurrentUser);
  const setInitialized = useAuthStore((state) => state.setInitialized);
  const logout = useAuthStore((state) => state.logout);

  useEffect(() => {
    if (started.current) {
      return;
    }
    started.current = true;

    const bootstrap = async () => {
      try {
        if (!getToken()) {
          await refreshAccessToken();
        }
        setCurrentUser(await getCurrentUser());
      } catch {
        logout();
      } finally {
        setInitialized(true);
      }
    };

    void bootstrap();
  }, [logout, setCurrentUser, setInitialized]);

  if (!initialized) {
    return (
      <div className="flex min-h-screen items-center justify-center bg-[#040814] text-sm text-slate-400">
        Loading Wave…
      </div>
    );
  }

  return <>{children}</>;
}
