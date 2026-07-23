import { z } from "zod";

export const createWorkspaceSchema = z.object({
  name: z
    .string()
    .trim()
    .min(
      3,
      "Workspace name must be at least 3 characters."
    )
    .max(
      50,
      "Workspace name cannot exceed 50 characters."
    ),

  description: z
    .string()
    .trim()
    .max(
      255,
      "Description cannot exceed 255 characters."
    )
    .optional(),
});

export type CreateWorkspaceFormData =
  z.infer<typeof createWorkspaceSchema>;