import { useState } from "react";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import toast from "react-hot-toast";

import Button from "../../../components/ui/Button";
import Modal from "../../../components/ui/Modal";
import Switch from "../../../components/ui/Switch";
import TextField from "../../../components/ui/TextField";

import { useCreateChannel } from "../hooks/useCreateChannel";
import { useChannelUIStore } from "../store/channelUIStore";
import { useWorkspaceStore } from "../../workspace/store/workspaceStore";

import {
  createChannelSchema,
  type CreateChannelFormData,
} from "../schemas/createChannelSchema";

export default function CreateChannelModal() {
  const isOpen = useChannelUIStore(
    (state) => state.isCreateChannelModalOpen
  );

  const closeModal = useChannelUIStore(
    (state) => state.closeCreateChannelModal
  );

  const activeWorkspace = useWorkspaceStore(
    (state) => state.activeWorkspace
  );

  const {
    mutate,
    isPending,
  } = useCreateChannel();

  const {
    register,
    handleSubmit,
    reset,
    formState: { errors },
  } = useForm<CreateChannelFormData>({
    resolver: zodResolver(createChannelSchema),
    defaultValues: {
      name: "",
      description: "",
      isPrivate: false,
    },
  });

  const [isPrivate, setIsPrivate] = useState(false);

  const handleClose = () => {
    reset();
    setIsPrivate(false);
    closeModal();
  };

  const onSubmit = (
    data: CreateChannelFormData
  ) => {
    if (!activeWorkspace) {
      toast.error("Please select a workspace first.");
      return;
    }

    mutate(
      {
        name: data.name,
        description: data.description ?? "",
        workspaceId: activeWorkspace.id,
        isPrivate,
      },
      {
        onSuccess: handleClose,
        onError: () => {
          toast.error(
            "Failed to create channel."
          );
        },
      }
    );
  };

  return (
    <Modal
      open={isOpen}
      title="Create Channel"
      onClose={handleClose}
    >
      <form
        onSubmit={handleSubmit(onSubmit)}
        className="space-y-5"
      >
        <TextField
          id="channel-name"
          label="Channel Name"
          placeholder="general"
          disabled={isPending}
          error={errors.name?.message}
          {...register("name")}
        />

        <TextField
          id="channel-description"
          label="Description"
          placeholder="Optional description..."
          disabled={isPending}
          error={errors.description?.message}
          {...register("description")}
        />

        <div className="space-y-2">
          <label className="text-sm font-medium text-slate-300">
            Private Channel
          </label>

          <div className="flex items-center gap-3">
            <Switch
              checked={isPrivate}
              disabled={isPending}
              onCheckedChange={setIsPrivate}
            />

            <span className="text-sm text-slate-400">
              Only invited members can access this channel.
            </span>
          </div>
        </div>

        <div className="flex gap-3 pt-2">
          <Button
            type="button"
            variant="secondary"
            onClick={handleClose}
            disabled={isPending}
          >
            Cancel
          </Button>

          <Button
            type="submit"
            isLoading={isPending}
          >
            {isPending
              ? "Creating..."
              : "Create Channel"}
          </Button>
        </div>
      </form>
    </Modal>
  );
}
