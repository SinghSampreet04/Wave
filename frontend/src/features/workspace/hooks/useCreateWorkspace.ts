import { useMutation, useQueryClient } from "@tanstack/react-query";

import { createWorkspace } from "../api/workspaceApi";
import { useWorkspaceStore } from "../store/workspaceStore";

export function useCreateWorkspace() {
  const queryClient = useQueryClient();

  const setActiveWorkspace = useWorkspaceStore(
    (state) => state.setActiveWorkspace
  );

  return useMutation({
    mutationFn: createWorkspace,

    onSuccess: (workspace) => {
      setActiveWorkspace(workspace);

      queryClient.invalidateQueries({
        queryKey: ["workspaces"],
      });
    },
  });
}