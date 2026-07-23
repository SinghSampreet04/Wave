import {
  useMutation,
  useQueryClient,
} from "@tanstack/react-query";
import toast from "react-hot-toast";

import { toggleReaction } from "../api/chat";
import { updateReactionInMessageCache } from "../cache/messageCache";
import { useChatStore } from "../store/chatStore";
import type { Reaction } from "../types/chat";

export function useToggleReaction() {
  const queryClient = useQueryClient();

  const updateEverywhere = (
    reaction: Reaction
  ) => {
    useChatStore
      .getState()
      .updateReactions(
        reaction.messageId,
        [reaction]
      );
    updateReactionInMessageCache(
      queryClient,
      reaction
    );
  };

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

    onMutate: async ({
      messageId,
      emoji,
    }) => {
      await queryClient.cancelQueries({
        queryKey: ["messages"],
      });

      const existing = useChatStore
        .getState()
        .messages.find(
          (message) =>
            message.id === messageId
        )
        ?.reactions?.find(
          (reaction) =>
            reaction.emoji === emoji
        );
      const optimistic: Reaction = {
        messageId,
        emoji,
        count: existing
          ? Math.max(
              0,
              existing.count +
                (existing.reactedByCurrentUser
                  ? -1
                  : 1)
            )
          : 1,
        reactedByCurrentUser:
          !existing?.reactedByCurrentUser,
      };

      updateEverywhere(optimistic);

      return {
        previous:
          existing ?? {
            messageId,
            emoji,
            count: 0,
            reactedByCurrentUser:
              false,
          },
      };
    },

    onSuccess: (reaction) => {
      updateEverywhere(reaction);
    },

    onError: (
      _error,
      _variables,
      context
    ) => {
      if (context?.previous) {
        updateEverywhere(
          context.previous
        );
      }

      toast.error(
        "Unable to update reaction."
      );
    },

    onSettled: () => {
      void queryClient.invalidateQueries({
        queryKey: ["messages"],
      });
    },
  });
}
