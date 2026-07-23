import { useMutation } from "@tanstack/react-query";

import { inviteWorkspaceMember } from "../api/workspaceApi";
import type { InviteWorkspaceMemberRequest } from "../types/workspace";

export function useInviteWorkspaceMember() {
  return useMutation({
    mutationFn: ({
      workspaceId,
      request,
    }: {
      workspaceId: number;
      request: InviteWorkspaceMemberRequest;
    }) =>
      inviteWorkspaceMember(
        workspaceId,
        request
      ),
  });
}