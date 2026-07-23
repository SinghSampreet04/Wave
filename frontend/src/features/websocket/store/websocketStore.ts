import { create } from "zustand";
import type { Client } from "@stomp/stompjs";

import type { WebSocketState } from "../types/websocket";

export const useWebSocketStore =
  create<WebSocketState>((set, get) => ({
    connected: false,

    connecting: false,

    client: null,

    setClient: (client: Client | null) =>
      set({
        client,
      }),

    setConnected: (connected: boolean) =>
      set({
        connected,
      }),

    setConnecting: (connecting: boolean) =>
      set({
        connecting,
      }),

    disconnect: () => {
      const client = get().client;

      if (client?.active) {
        client.deactivate();
      }

      set({
        connected: false,
        connecting: false,
        client: null,
      });
    },
  }));