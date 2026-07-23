import { create } from "zustand";

interface ChannelUIState {
  isCreateChannelModalOpen: boolean;

  openCreateChannelModal: () => void;

  closeCreateChannelModal: () => void;
}

export const useChannelUIStore =
  create<ChannelUIState>((set) => ({
    isCreateChannelModalOpen: false,

    openCreateChannelModal: () =>
      set({
        isCreateChannelModalOpen: true,
      }),

    closeCreateChannelModal: () =>
      set({
        isCreateChannelModalOpen: false,
      }),
  }));