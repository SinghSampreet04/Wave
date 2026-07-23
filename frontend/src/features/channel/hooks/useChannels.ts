import { useQuery } from "@tanstack/react-query";

import { getWorkspaceChannels } from "../api/channel";

export function useChannels(
  workspaceId?: number
) {
  return useQuery({
    queryKey: [
      "channels",
      workspaceId,
    ],

    queryFn: () =>
      getWorkspaceChannels(
        workspaceId!
      ),

    enabled: !!workspaceId,
  });
}