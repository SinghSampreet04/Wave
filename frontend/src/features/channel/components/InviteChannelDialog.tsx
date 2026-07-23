import { useState } from "react";
import { useMutation, useQuery } from "@tanstack/react-query";
import toast from "react-hot-toast";

import Button from "../../../components/ui/Button";
import Modal from "../../../components/ui/Modal";
import useDebounce from "../../../hooks/useDebounce";
import { searchUsers } from "../../direct/api";
import { useAuthStore } from "../../auth/store/authStore";
import type { Channel } from "../types/channel";
import { inviteUserToChannel } from "../api/channel";

interface InviteChannelDialogProps {
  channel: Channel;
  onClose: () => void;
}

export default function InviteChannelDialog({
  channel,
  onClose,
}: InviteChannelDialogProps) {
  const [query, setQuery] = useState("");
  const debouncedQuery = useDebounce(
    query.trim(),
    250
  );
  const currentUserId = useAuthStore(
    (state) => state.currentUser?.id
  );

  const { data: users = [], isFetching } =
    useQuery({
      queryKey: [
        "channel-invite-user-search",
        debouncedQuery,
      ],
      queryFn: () =>
        searchUsers(debouncedQuery),
      enabled: debouncedQuery.length >= 2,
    });

  const invite = useMutation({
    mutationFn: (inviteeId: number) =>
      inviteUserToChannel(
        channel.id,
        inviteeId
      ),
    onSuccess: () => {
      toast.success(
        `Invitation sent to #${channel.name}.`
      );
      onClose();
    },
    onError: () => {
      toast.error(
        "Unable to invite this person. They may already be a member or have a pending invitation."
      );
    },
  });

  const availableUsers = users.filter(
    (user) => user.id !== currentUserId
  );

  return (
    <Modal
      open
      title={`Invite to #${channel.name}`}
      onClose={onClose}
    >
      <div className="space-y-4">
        <div>
          <label
            htmlFor="channel-member-search"
            className="mb-2 block text-sm font-medium text-slate-200"
          >
            Find a workspace member
          </label>
          <input
            id="channel-member-search"
            value={query}
            onChange={(event) =>
              setQuery(event.target.value)
            }
            autoFocus
            placeholder="Search by name or username…"
            className="w-full rounded-xl border border-white/10 bg-slate-900 px-4 py-3 text-white outline-none placeholder:text-slate-500 focus:border-cyan-400/40"
          />
          <p className="mt-2 text-xs leading-5 text-slate-400">
            Private-channel invitations can only be
            sent to existing workspace members.
          </p>
        </div>

        <div className="max-h-64 overflow-y-auto rounded-xl border border-white/10 bg-slate-950/70">
          {debouncedQuery.length < 2 ? (
            <p className="p-5 text-center text-sm text-slate-400">
              Type at least two characters.
            </p>
          ) : isFetching ? (
            <p className="p-5 text-center text-sm text-slate-400">
              Searching…
            </p>
          ) : availableUsers.length === 0 ? (
            <p className="p-5 text-center text-sm text-slate-400">
              No matching workspace members.
            </p>
          ) : (
            availableUsers.map((user) => (
              <button
                key={user.id}
                type="button"
                disabled={invite.isPending}
                onClick={() =>
                  invite.mutate(user.id)
                }
                className="flex w-full items-center justify-between border-b border-white/5 px-4 py-3 text-left transition last:border-b-0 hover:bg-white/5 disabled:opacity-50"
              >
                <span>
                  <span className="block text-sm font-semibold text-white">
                    {user.firstName}{" "}
                    {user.lastName}
                  </span>
                  <span className="text-xs text-slate-400">
                    @{user.username}
                  </span>
                </span>
                <span className="text-xs font-semibold text-cyan-300">
                  Invite
                </span>
              </button>
            ))
          )}
        </div>

        <Button
          type="button"
          variant="secondary"
          onClick={onClose}
        >
          Cancel
        </Button>
      </div>
    </Modal>
  );
}
