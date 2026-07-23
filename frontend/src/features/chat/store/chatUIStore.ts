import { create } from "zustand";

interface ChatUIState {
  activeMenuId: number | null;

  activeEmojiPickerId: number | null;

  editingMessageId: number | null;

  setActiveMenu: (
    messageId: number | null
  ) => void;

  setActiveEmojiPicker: (
    messageId: number | null
  ) => void;

  setEditingMessage: (
    messageId: number | null
  ) => void;

  closeAll: () => void;
}

export const useChatUIStore =
  create<ChatUIState>((set) => ({
    activeMenuId: null,

    activeEmojiPickerId: null,

    editingMessageId: null,

    setActiveMenu: (messageId) =>
      set({
        activeMenuId: messageId,
        activeEmojiPickerId: null,
      }),

    setActiveEmojiPicker: (messageId) =>
      set({
        activeEmojiPickerId: messageId,
        activeMenuId: null,
      }),

    setEditingMessage: (messageId) =>
      set({
        editingMessageId: messageId,
      }),

    closeAll: () =>
      set({
        activeMenuId: null,
        activeEmojiPickerId: null,
      }),
  }));