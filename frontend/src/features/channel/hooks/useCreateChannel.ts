import { useMutation, useQueryClient } from "@tanstack/react-query";
import toast from "react-hot-toast";

import { createChannel } from "../api/channel";
import { useChannelStore } from "../store/channelStore";

export function useCreateChannel() {
  const queryClient = useQueryClient();

  const setActiveChannel =
    useChannelStore(
      (state) => state.setActiveChannel
    );

  return useMutation({
    mutationFn: createChannel,

    onSuccess: (channel) => {
      queryClient.invalidateQueries({
        queryKey: [
          "channels",
          channel.workspaceId,
        ],
      });

      setActiveChannel(channel);

      toast.success(
        "Channel created successfully."
      );
    },

    onError: () => {
      toast.error(
        "Failed to create channel."
      );
    },
  });
}