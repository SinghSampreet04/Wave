import {
  useMutation,
  useQueryClient,
} from "@tanstack/react-query";
import toast from "react-hot-toast";

import { useChannelStore } from "../../channel/store/channelStore";
import { deleteWorkspace } from "../api/workspaceApi";
import { useWorkspaceStore } from "../store/workspaceStore";
import type { Workspace } from "../types/workspace";

export function useDeleteWorkspace() {
  const queryClient = useQueryClient();
  const clearActiveWorkspace =
    useWorkspaceStore(
      (state) =>
        state.clearActiveWorkspace
    );
  const clearActiveChannel =
    useChannelStore(
      (state) =>
        state.clearActiveChannel
    );

  return useMutation({
    mutationFn: deleteWorkspace,

    onSuccess: (
      _response,
      workspaceId
    ) => {
      queryClient.setQueryData<
        Workspace[]
      >(
        ["workspaces"],
        (workspaces) =>
          workspaces?.filter(
            (workspace) =>
              workspace.id !==
              workspaceId
          ) ?? []
      );
      queryClient.removeQueries({
        queryKey: [
          "channels",
          workspaceId,
        ],
      });
      clearActiveChannel();
      clearActiveWorkspace();
      void queryClient.invalidateQueries({
        queryKey: ["workspaces"],
      });
      toast.success("Workspace deleted.");
    },

    onError: () => {
      toast.error(
        "Unable to delete workspace."
      );
    },
  });
}
