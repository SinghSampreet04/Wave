import { create } from "zustand";

import type {
  Channel,
  ChannelState,
} from "../types/channel";

export const useChannelStore =
  create<ChannelState>((set) => ({
    activeChannel: null,

    setActiveChannel: (
      channel: Channel
    ) =>
      set({
        activeChannel: channel,
      }),

    clearActiveChannel: () =>
      set({
        activeChannel: null,
      }),
  }));