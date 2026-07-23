import { create } from "zustand";

import type {
  Workspace,
  WorkspaceState,
} from "../types/workspace";

export const useWorkspaceStore = create<WorkspaceState>((set) => ({
  activeWorkspace: null,

  setActiveWorkspace: (workspace: Workspace) =>
    set({
      activeWorkspace: workspace,
    }),

  clearActiveWorkspace: () =>
    set({
      activeWorkspace: null,
    }),
}));