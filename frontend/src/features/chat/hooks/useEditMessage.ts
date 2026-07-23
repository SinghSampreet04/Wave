import { useMutation } from "@tanstack/react-query";
import toast from "react-hot-toast";

import { editMessage } from "../api/chat";

export function useEditMessage() {
  return useMutation({
    mutationFn: ({
      messageId,
      content,
    }: {
      messageId: number;
      content: string;
    }) => editMessage(messageId, content),

    onSuccess: () => {
      toast.success("Message updated.");
    },

    onError: () => {
      toast.error("Unable to update message.");
    },
  });
}