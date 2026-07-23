import { useEffect } from "react";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import toast from "react-hot-toast";

import Button from "../../../components/ui/Button";
import Modal from "../../../components/ui/Modal";
import TextField from "../../../components/ui/TextField";

import { useWorkspaceStore } from "../store/workspaceStore";
import { useWorkspaceUIStore } from "../store/workspaceUIStore";
import { useInviteWorkspaceMember } from "../hooks/useInviteWorkspaceMember";

import {
  inviteWorkspaceMemberSchema,
  type InviteWorkspaceMemberFormData,
} from "../schemas/inviteWorkspaceMemberSchema";

export default function InviteMemberDialog() {
  const activeWorkspace = useWorkspaceStore(
    (state) => state.activeWorkspace
  );

  const isOpen = useWorkspaceUIStore(
    (state) => state.isInviteMemberModalOpen
  );

  const closeModal = useWorkspaceUIStore(
    (state) => state.closeInviteMemberModal
  );

  const {
    register,
    handleSubmit,
    reset,
    formState: { errors },
  } = useForm<InviteWorkspaceMemberFormData>({
    resolver: zodResolver(
      inviteWorkspaceMemberSchema
    ),
    defaultValues: {
      email: "",
    },
  });

  const {
    mutate,
    isPending,
    isSuccess,
  } = useInviteWorkspaceMember();

  useEffect(() => {
    if (!isSuccess) {
      return;
    }

    toast.success("Member invited successfully.");

    reset();

    closeModal();
  }, [isSuccess, reset, closeModal]);

  const onSubmit = (
    data: InviteWorkspaceMemberFormData
  ) => {
    if (!activeWorkspace) {
      toast.error("No workspace selected.");
      return;
    }

    mutate(
      {
        workspaceId: activeWorkspace.id,
        request: {
          email: data.email,
        },
      },
      {
        onError: () => {
          toast.error(
            "Failed to invite member."
          );
        },
      }
    );
  };

  return (
    <Modal
      open={isOpen}
      title="Invite Member"
      onClose={closeModal}
    >
      <form
        onSubmit={handleSubmit(onSubmit)}
        className="space-y-5"
      >
        <TextField
          id="email"
          label="Email Address"
          placeholder="john@example.com"
          disabled={isPending}
          error={errors.email?.message}
          {...register("email")}
        />

        <div className="flex gap-3 pt-2">
          <Button
            type="button"
            variant="secondary"
            disabled={isPending}
            onClick={closeModal}
          >
            Cancel
          </Button>

          <Button
            type="submit"
            isLoading={isPending}
          >
            {isPending
              ? "Inviting..."
              : "Invite Member"}
          </Button>
        </div>
      </form>
    </Modal>
  );
}