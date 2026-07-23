import { useQuery } from "@tanstack/react-query";

import { getWorkspaces } from "../api/workspaceApi";

export function useWorkspaces() {
  return useQuery({
    queryKey: ["workspaces"],
    queryFn: getWorkspaces,
  });
}