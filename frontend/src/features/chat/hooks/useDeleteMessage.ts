import {
  useMutation,
  useQueryClient,
} from "@tanstack/react-query";
import toast from "react-hot-toast";

import { deleteMessage } from "../api/chat";
import { useChatStore } from "../store/chatStore";

export function useDeleteMessage() {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (
      messageId: number
    ) => deleteMessage(messageId),

    onSuccess: (deletedMessage) => {
      useChatStore.getState().updateMessage({
        id: deletedMessage.messageId,
        deleted: true,
        content: "This message was deleted.",
        reactions: [],
        updatedAt: deletedMessage.deletedAt,
      });

      void queryClient.invalidateQueries({
        queryKey: [
          "message-files",
          deletedMessage.messageId,
        ],
      });
      void queryClient.invalidateQueries({
        queryKey: [
          "pinned-messages",
          deletedMessage.channelId,
        ],
      });

      toast.success(
        "Message deleted."
      );
    },

    onError: () => {
      toast.error(
        "Unable to delete message."
      );
    },
  });
}
