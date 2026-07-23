import {
  useMutation,
  useQueryClient,
} from "@tanstack/react-query";
import toast from "react-hot-toast";

import { deleteChannel } from "../api/channel";
import { useChannelStore } from "../store/channelStore";
import type { Channel } from "../types/channel";

export function useDeleteChannel(
  workspaceId: number
) {
  const queryClient = useQueryClient();
  const clearActiveChannel =
    useChannelStore(
      (state) =>
        state.clearActiveChannel
    );

  return useMutation({
    mutationFn: deleteChannel,

    onSuccess: (
      _response,
      channelId
    ) => {
      queryClient.setQueryData<
        Channel[]
      >(
        ["channels", workspaceId],
        (channels) =>
          channels?.filter(
            (channel) =>
              channel.id !== channelId
          ) ?? []
      );
      clearActiveChannel();
      void queryClient.invalidateQueries({
        queryKey: [
          "channels",
          workspaceId,
        ],
      });
      toast.success("Channel deleted.");
    },

    onError: () => {
      toast.error(
        "Unable to delete channel."
      );
    },
  });
}
