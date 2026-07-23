import { useQuery } from "@tanstack/react-query";

import { getThreadReplies } from "../api/thread";

export function useThreadReplies(
  messageId: number | null
) {
  return useQuery({
    queryKey: [
      "thread",
      messageId,
    ],

    queryFn: () =>
      getThreadReplies(
        messageId!
      ),

    enabled:
      messageId !== null,
  });
}