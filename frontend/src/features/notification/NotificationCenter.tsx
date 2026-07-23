import { useEffect, useRef, useState } from "react";
import { Bell, CheckCheck } from "lucide-react";
import {
  useMutation,
  useQuery,
  useQueryClient,
} from "@tanstack/react-query";
import toast from "react-hot-toast";

import { IconButton } from "../../components/ui";
import useClickOutside from "../../hooks/useClickOutside";
import { useAuthStore } from "../auth/store/authStore";
import { useWebSocketStore } from "../websocket/store/websocketStore";
import { websocketService } from "../../services/websocket/websocketService";
import {
  getNotifications,
  markAllNotificationsRead,
  markNotificationRead,
  type NotificationPage,
} from "./api";
import type { Notification } from "./types";
import api from "../../api/axios";
import {
  acceptWorkspaceInvitation,
  getMyWorkspaceInvitations,
  getWorkspaces,
} from "../workspace/api/workspaceApi";
import {
  acceptChannelInvitation,
  getMyChannelInvitations,
  getWorkspaceChannels,
} from "../channel/api/channel";
import { getConversations } from "../direct/api";
import { useWorkspaceStore } from "../workspace/store/workspaceStore";
import { useChannelStore } from "../channel/store/channelStore";
import { useDirectStore } from "../direct/store";

const queryKey = ["notifications"] as const;

export default function NotificationCenter() {
  const [open, setOpen] = useState(false);
  const [filter, setFilter] = useState<"all" | "unread">("all");
  const containerRef = useRef<HTMLDivElement>(null);
  const userId = useAuthStore(
    (state) => state.currentUser?.id
  );
  const connected = useWebSocketStore(
    (state) => state.connected
  );
  const queryClient = useQueryClient();
  const setActiveWorkspace = useWorkspaceStore((state) => state.setActiveWorkspace);
  const setActiveChannel = useChannelStore((state) => state.setActiveChannel);
  const clearActiveChannel = useChannelStore(
    (state) => state.clearActiveChannel
  );
  const setActiveConversation = useDirectStore((state) => state.setActiveConversation);

  useClickOutside(
    containerRef,
    () => setOpen(false)
  );

  const { data } = useQuery({
    queryKey,
    queryFn: getNotifications,
    refetchInterval: connected
      ? false
      : 30_000,
  });

  useEffect(() => {
    if (!connected || !userId) {
      return;
    }

    const subscription =
      websocketService.subscribeToNotifications(
        userId,
        (event) => {
          const incoming: Notification = {
            id: event.notificationId,
            type: event.type,
            title: event.title,
            body: event.body,
            referenceId: event.referenceId,
            referenceType: event.referenceType,
            read: false,
            createdAt: event.createdAt,
          };

          queryClient.setQueryData<NotificationPage>(
            queryKey,
            (current) => {
              const page = current ?? {
                items: [],
                page: 0,
                size: 100,
                totalElements: 0,
                totalPages: 1,
                hasMore: false,
              };
              return page.items.some(
                (item) => item.id === incoming.id
              )
                ? page
                : {
                    ...page,
                    items: [incoming, ...page.items],
                    totalElements: page.totalElements + 1,
                  };
            }
          );
        }
      );

    return () => {
      subscription?.unsubscribe();
    };
  }, [
    connected,
    userId,
    queryClient,
  ]);

  const markOne = useMutation({
    mutationFn: markNotificationRead,
    onSuccess: (_, notificationId) => {
      queryClient.setQueryData<NotificationPage>(
        queryKey,
        (current) => current
          ? {
              ...current,
              items: current.items.map((item) =>
                item.id === notificationId
                  ? { ...item, read: true }
                  : item
              ),
            }
          : current
      );
    },
  });

  const markAll = useMutation({
    mutationFn: markAllNotificationsRead,
    onSuccess: () => {
      queryClient.setQueryData<NotificationPage>(
        queryKey,
        (current) => current
          ? {
              ...current,
              items: current.items.map((item) => ({
                ...item,
                read: true,
              })),
            }
          : current
      );
    },
  });

  const notifications = data?.items ?? [];
  const unreadCount = notifications.filter(
    (item) => !item.read
  ).length;
  const visibleNotifications =
    filter === "unread"
      ? notifications.filter((item) => !item.read)
      : notifications;

  const openNotification = async (notification: Notification) => {
    if (!notification.read) {
      markOne.mutate(notification.id);
    }

    if (notification.referenceType === "CONVERSATION") {
      const conversations = await getConversations();
      const conversation = conversations.find(
        (item) => item.conversationId === notification.referenceId
      );
      if (conversation) {
        setActiveConversation(conversation);
        setOpen(false);
      }
      return;
    }

    if (
      notification.type === "WORKSPACE_INVITE" &&
      notification.title === "Workspace Invitation" &&
      notification.referenceId
    ) {
      try {
        const invitations =
          await getMyWorkspaceInvitations();
        const invitation = invitations.find(
          (item) =>
            item.workspaceId ===
              notification.referenceId &&
            item.status === "PENDING"
        );
        if (invitation) {
          await acceptWorkspaceInvitation(
            invitation.id
          );
          await queryClient.invalidateQueries({
            queryKey: ["workspaces"],
          });
          toast.success(
            `Joined ${invitation.workspaceName}.`
          );
        }
        setOpen(false);
      } catch {
        toast.error(
          "Unable to accept this workspace invitation."
        );
      }
      return;
    }

    if (
      notification.type === "CHANNEL_INVITE" &&
      notification.title === "Channel Invitation" &&
      notification.referenceId
    ) {
      try {
        const invitations =
          await getMyChannelInvitations();
        const invitation = invitations.find(
          (item) =>
            item.channelId ===
              notification.referenceId &&
            item.status === "PENDING"
        );
        if (invitation) {
          await acceptChannelInvitation(
            invitation.id
          );
          await queryClient.invalidateQueries({
            queryKey: ["workspaces"],
          });
          await queryClient.invalidateQueries({
            queryKey: ["channels"],
          });
          toast.success(
            `Joined #${invitation.channelName}.`
          );
        }
        setOpen(false);
      } catch {
        toast.error(
          "Unable to accept this channel invitation."
        );
      }
      return;
    }

    if (
      (notification.referenceType === "MESSAGE"
        || notification.referenceType === "DIRECT_MESSAGE")
      && notification.referenceId
    ) {
      if (notification.referenceType === "DIRECT_MESSAGE") {
        const context = await api.get<{
          messageId: number;
          conversationId: number;
        }>(`/v1/direct-messages/${notification.referenceId}/context`);
        const conversations = await getConversations();
        const conversation = conversations.find(
          (item) =>
            item.conversationId === context.data.conversationId
        );
        if (conversation) {
          clearActiveChannel();
          setActiveConversation(conversation);
          setOpen(false);
          window.setTimeout(() => {
            document
              .getElementById(
                `direct-message-${notification.referenceId}`
              )
              ?.scrollIntoView({
                behavior: "smooth",
                block: "center",
              });
          }, 350);
        }
        return;
      }
      const response = await api.get<{
        messageId: number;
        channelId: number;
        workspaceId: number;
      }>(`/v1/messages/${notification.referenceId}/context`);
      const workspaces = await getWorkspaces();
      const workspace = workspaces.find(
        (item) => item.id === response.data.workspaceId
      );
      if (!workspace) return;
      setActiveWorkspace(workspace);
      const channels = await getWorkspaceChannels(workspace.id);
      const channel = channels.find(
        (item) => item.id === response.data.channelId
      );
      if (!channel) return;
      setActiveConversation(null);
      setActiveChannel(channel);
      setOpen(false);
      window.setTimeout(() => {
        document
          .getElementById(`message-${notification.referenceId}`)
          ?.scrollIntoView({ behavior: "smooth", block: "center" });
      }, 350);
    }
  };

  return (
    <div
      ref={containerRef}
      className="relative"
    >
      <IconButton
        icon={<Bell size={18} />}
        aria-label="Notifications"
        active={open}
        onClick={() =>
          setOpen((current) => !current)
        }
      />

      {unreadCount > 0 && (
        <span className="pointer-events-none absolute -right-1 -top-1 min-w-5 rounded-full bg-cyan-500 px-1.5 text-center text-[11px] font-bold leading-5 text-slate-950">
          {unreadCount > 99
            ? "99+"
            : unreadCount}
        </span>
      )}

      {open && (
        <div className="absolute right-0 top-14 z-50 w-[min(24rem,calc(100vw-2rem))] overflow-hidden rounded-2xl border border-white/10 bg-slate-950/95 shadow-2xl backdrop-blur-xl">
          <div className="flex items-center justify-between border-b border-white/10 px-4 py-3">
            <div>
              <p className="font-semibold text-white">
                Notifications
              </p>
              <p className="text-xs text-slate-400">
                {unreadCount} unread
              </p>
            </div>

            <button
              type="button"
              disabled={
                unreadCount === 0 ||
                markAll.isPending
              }
              onClick={() => markAll.mutate()}
              className="flex items-center gap-1.5 text-xs font-medium text-cyan-300 disabled:opacity-40"
            >
              <CheckCheck size={15} />
              Mark all read
            </button>
          </div>

          <div className="max-h-96 overflow-y-auto">
            <div className="sticky top-0 z-10 flex gap-2 border-b border-white/10 bg-slate-950 px-4 py-2">
              {(["all", "unread"] as const).map((item) => (
                <button
                  key={item}
                  type="button"
                  onClick={() => setFilter(item)}
                  className={`rounded-full px-3 py-1 text-xs font-semibold capitalize ${
                    filter === item
                      ? "bg-cyan-500 text-slate-950"
                      : "bg-white/5 text-slate-300"
                  }`}
                >
                  {item}
                </button>
              ))}
            </div>
            {visibleNotifications.length === 0 ? (
              <p className="px-5 py-10 text-center text-sm text-slate-400">
                You’re all caught up.
              </p>
            ) : (
              visibleNotifications.map((notification) => (
                <button
                  key={notification.id}
                  type="button"
                  onClick={() => void openNotification(notification)}
                  className={`block w-full border-b border-white/5 px-4 py-3 text-left transition hover:bg-white/5 ${
                    notification.read
                      ? "opacity-65"
                      : "bg-cyan-500/[0.06]"
                  }`}
                >
                  <div className="flex gap-3">
                    <span
                      className={`mt-2 h-2 w-2 shrink-0 rounded-full ${
                        notification.read
                          ? "bg-slate-600"
                          : "bg-cyan-400"
                      }`}
                    />
                    <span>
                      <span className="block text-sm font-semibold text-white">
                        {notification.title}
                      </span>
                      <span className="mt-1 block text-sm leading-5 text-slate-300">
                        {notification.body}
                      </span>
                      <span className="mt-1.5 block text-[11px] text-slate-500">
                        {new Date(
                          notification.createdAt
                        ).toLocaleString()}
                      </span>
                      {((notification.type ===
                        "CHANNEL_INVITE" &&
                        notification.title ===
                          "Channel Invitation") ||
                        (notification.type ===
                          "WORKSPACE_INVITE" &&
                          notification.title ===
                            "Workspace Invitation")) && (
                        <span className="mt-2 block text-xs font-semibold text-cyan-300">
                          Click to accept invitation
                        </span>
                      )}
                    </span>
                  </div>
                </button>
              ))
            )}
          </div>
        </div>
      )}
    </div>
  );
}
