import { z } from "zod";

export const inviteWorkspaceMemberSchema = z.object({
  email: z
    .email("Please enter a valid email address.")
});

export type InviteWorkspaceMemberFormData =
  z.infer<typeof inviteWorkspaceMemberSchema>;