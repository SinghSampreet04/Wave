import { useMutation } from "@tanstack/react-query";
import toast from "react-hot-toast";

import { deleteMessage } from "../api/chat";
import { useChatStore } from "../store/chatStore";

export function useDeleteMessage() {
  return useMutation({
    mutationFn: (
      messageId: number
    ) => deleteMessage(messageId),

    onSuccess: (deletedMessage) => {
      useChatStore.getState().updateMessage({
        id: deletedMessage.messageId,
        deleted: true,
        content: "This message was deleted.",
        updatedAt: deletedMessage.deletedAt,
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
