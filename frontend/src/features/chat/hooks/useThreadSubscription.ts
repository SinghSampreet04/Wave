import { useEffect } from "react";
import { useQueryClient } from "@tanstack/react-query";

import { websocketService } from "../../../services/websocket/websocketService";
import { useWebSocketStore } from "../../websocket/store/websocketStore";
import type { ThreadReply } from "../types/thread";

export function useThreadSubscription(
  parentMessageId: number | null
) {
  const connected = useWebSocketStore(
    (state) => state.connected
  );
  const queryClient = useQueryClient();

  useEffect(() => {
    if (!connected || parentMessageId === null) {
      return;
    }

    const subscription =
      websocketService.subscribeToThread(
        parentMessageId,
        (reply) => {
          queryClient.setQueryData<ThreadReply[]>(
            ["thread", parentMessageId],
            (current = []) =>
              current.some(
                (item) => item.id === reply.id
              )
                ? current
                : [...current, reply]
          );
        }
      );

    return () => {
      subscription?.unsubscribe();
    };
  }, [
    connected,
    parentMessageId,
    queryClient,
  ]);
}
