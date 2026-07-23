import { useEffect } from "react";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import toast from "react-hot-toast";

import Button from "../../../components/ui/Button";
import Modal from "../../../components/ui/Modal";
import TextField from "../../../components/ui/TextField";

import { useCreateWorkspace } from "../hooks/useCreateWorkspace";
import { useWorkspaceUIStore } from "../store/workspaceUIStore";

import {
  createWorkspaceSchema,
  type CreateWorkspaceFormData,
} from "../schemas/createWorkspaceSchema";

export default function CreateWorkspaceModal() {
  const isOpen = useWorkspaceUIStore(
    (state) => state.isCreateWorkspaceModalOpen
  );

  const closeModal = useWorkspaceUIStore(
    (state) => state.closeCreateWorkspaceModal
  );

  const {
    register,
    handleSubmit,
    reset,
    formState: { errors },
  } = useForm<CreateWorkspaceFormData>({
    resolver: zodResolver(createWorkspaceSchema),
    defaultValues: {
      name: "",
      description: "",
    },
  });

  const {
    mutate,
    isPending,
    isSuccess,
  } = useCreateWorkspace();

  useEffect(() => {
    if (isSuccess) {
      toast.success("Workspace created successfully.");

      reset();

      closeModal();
    }
  }, [isSuccess, closeModal, reset]);

  const onSubmit = (
    data: CreateWorkspaceFormData
  ) => {
    mutate(
      {
        name: data.name,
        description: data.description ?? "",
      },
      {
        onError: () => {
          toast.error(
            "Failed to create workspace."
          );
        },
      }
    );
  };

  return (
    <Modal
      open={isOpen}
      title="Create Workspace"
      onClose={closeModal}
    >
      <form
        onSubmit={handleSubmit(onSubmit)}
        className="space-y-5"
      >
        <TextField
          id="name"
          label="Workspace Name"
          placeholder="Engineering"
          disabled={isPending}
          error={errors.name?.message}
          {...register("name")}
        />

        <TextField
          id="description"
          label="Description"
          placeholder="Optional description..."
          disabled={isPending}
          error={errors.description?.message}
          {...register("description")}
        />

        <div className="flex gap-3 pt-2">
          <Button
            type="button"
            variant="secondary"
            onClick={closeModal}
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
              : "Create Workspace"}
          </Button>
        </div>
      </form>
    </Modal>
  );
}