import { z } from "zod";

export const createChannelSchema = z.object({
  name: z
    .string()
    .trim()
    .min(2, "Channel name must be at least 2 characters.")
    .max(50, "Channel name cannot exceed 50 characters."),

  description: z
    .string()
    .max(255, "Description cannot exceed 255 characters.")
    .optional(),

  isPrivate: z.boolean(),
});

export type CreateChannelFormData = z.infer<
  typeof createChannelSchema
>;