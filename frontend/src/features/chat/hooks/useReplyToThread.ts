import {
  useMutation,
  useQueryClient,
} from "@tanstack/react-query";

import { replyToThread } from "../api/thread";

export function useReplyToThread() {
  const queryClient =
    useQueryClient();

  return useMutation({
    mutationFn: replyToThread,

    onSuccess: (threadReply) => {
      queryClient.setQueryData(
        [
          "thread",
          threadReply.parentMessageId,
        ],
        (
          old:
            | typeof threadReply[]
            | undefined
        ) => {
          if (!old) {
            return [threadReply];
          }

          return [
            ...old,
            threadReply,
          ];
        }
      );
    },
  });
}