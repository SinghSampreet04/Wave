import { useCallback } from "react";

import { websocketService } from "../../../services/websocket/websocketService";
import { useWebSocketStore } from "../store/websocketStore";

export function useWebSocket() {
  const connected = useWebSocketStore(
    (state) => state.connected
  );

  const connecting = useWebSocketStore(
    (state) => state.connecting
  );

  const connect = useCallback(
    (token: string) => {
      websocketService.connect(token);
    },
    []
  );

  const disconnect = useCallback(() => {
    websocketService.disconnect();
  }, []);

  return {
    connected,
    connecting,
    connect,
    disconnect,
  };
}