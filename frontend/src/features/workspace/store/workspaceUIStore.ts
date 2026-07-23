import { create } from "zustand";

interface WorkspaceUIState {
  isCreateWorkspaceModalOpen: boolean;
  isInviteMemberModalOpen: boolean;

  openCreateWorkspaceModal: () => void;
  closeCreateWorkspaceModal: () => void;

  openInviteMemberModal: () => void;
  closeInviteMemberModal: () => void;
}

export const useWorkspaceUIStore =
  create<WorkspaceUIState>((set) => ({
    isCreateWorkspaceModalOpen: false,
    isInviteMemberModalOpen: false,

    openCreateWorkspaceModal: () =>
      set({
        isCreateWorkspaceModalOpen: true,
      }),

    closeCreateWorkspaceModal: () =>
      set({
        isCreateWorkspaceModalOpen: false,
      }),

    openInviteMemberModal: () =>
      set({
        isInviteMemberModalOpen: true,
      }),

    closeInviteMemberModal: () =>
      set({
        isInviteMemberModalOpen: false,
      }),
  }));