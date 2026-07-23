import { create } from "zustand";
import type { Conversation } from "./types";

interface DirectState {
  activeConversation: Conversation | null;
  setActiveConversation: (
    conversation: Conversation | null
  ) => void;
}

export const useDirectStore =
  create<DirectState>((set) => ({
    activeConversation: null,
    setActiveConversation: (
      activeConversation
    ) => set({ activeConversation }),
  }));
