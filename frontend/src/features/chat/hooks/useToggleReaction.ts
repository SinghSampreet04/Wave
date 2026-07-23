import { useMutation } from "@tanstack/react-query";
import toast from "react-hot-toast";

import { toggleReaction } from "../api/chat";
import { useChatStore } from "../store/chatStore";

export function useToggleReaction() {
  return useMutation({
    mutationFn: ({
      messageId,
      emoji,
    }: {
      messageId: number;
      emoji: string;
    }) =>
      toggleReaction(
        messageId,
        emoji
      ),

    onSuccess: (reaction) => {
      useChatStore
        .getState()
        .updateReactions(
          reaction.messageId,
          [reaction]
        );
    },

    onError: () => {
      toast.error(
        "Unable to add reaction."
      );
    },
  });
}
