import { Client } from "@stomp/stompjs";
import SockJS from "sockjs-client";

const API_BASE_URL =
  import.meta.env.VITE_API_BASE_URL ??
  "http://localhost:8082/api/v1";

function getWebSocketUrl(token: string): string {
  const url = new URL(
    API_BASE_URL,
    window.location.origin
  );

  return `${url.origin}/ws?token=${encodeURIComponent(token)}`;
}

export function createStompClient(
  token: string
): Client {
  return new Client({
    webSocketFactory: () =>
      new SockJS(getWebSocketUrl(token)),

    reconnectDelay: 5000,

    heartbeatIncoming: 4000,

    heartbeatOutgoing: 4000,

    debug: () => undefined,
  });
}
