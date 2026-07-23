import { useEffect } from "react";

import { useChannels } from "../hooks/useChannels";
import { useChannelStore } from "../store/channelStore";
import { useWorkspaceStore } from "../../workspace/store/workspaceStore";

import ChannelItem from "./ChannelItem";
import ChannelLoading from "./ChannelLoading";
import ChannelEmptyState from "./ChannelEmptyState";
import { useDirectStore } from "../../direct/store";
import { useAuthStore } from "../../auth/store/authStore";
import { useDeleteChannel } from "../hooks/useDeleteChannel";

export default function ChannelList() {
  const activeWorkspace = useWorkspaceStore(
    (state) => state.activeWorkspace
  );

  const {
    data,
    isLoading,
    isError,
  } = useChannels(activeWorkspace?.id);

  const activeChannel = useChannelStore(
    (state) => state.activeChannel
  );

  const setActiveChannel = useChannelStore(
    (state) => state.setActiveChannel
  );

  const setActiveConversation = useDirectStore(
    (state) => state.setActiveConversation
  );
  const currentUserId = useAuthStore(
    (state) =>
      state.currentUser?.id
  );
  const {
    mutate: deleteChannel,
    isPending: isDeleting,
  } = useDeleteChannel(
    activeWorkspace?.id ?? 0
  );

  useEffect(() => {
    if (
      !activeChannel &&
      data &&
      data.length > 0
    ) {
      setActiveChannel(data[0]);
    }
  }, [
    activeChannel,
    data,
    setActiveChannel,
  ]);

  if (!activeWorkspace) {
    return null;
  }

  if (isLoading) {
    return <ChannelLoading />;
  }

  if (isError) {
    return (
      <p className="text-sm text-red-400">
        Unable to load channels.
      </p>
    );
  }

  if (!data || data.length === 0) {
    return <ChannelEmptyState />;
  }

  return (
    <div className="space-y-2">
      {data.map((channel) => (
        <ChannelItem
          key={channel.id}
          channel={channel}
          active={activeChannel?.id === channel.id}
          onClick={() => {
            setActiveConversation(null);
            setActiveChannel(channel);
          }}
          canDelete={
            activeWorkspace.ownerId ===
            currentUserId
          }
          isDeleting={isDeleting}
          onDelete={() => {
            if (
              window.confirm(
                `Delete channel #${channel.name} and all of its messages? This cannot be undone.`
              )
            ) {
              deleteChannel(channel.id);
            }
          }}
        />
      ))}
    </div>
  );
}
