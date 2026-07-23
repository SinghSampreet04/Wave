import api from "../../api/axios";
import type { Notification } from "./types";

export interface NotificationPage {
  items: Notification[];
  page: number;
  size: number;
  totalElements: number;
  totalPages: number;
  hasMore: boolean;
}

export async function getNotifications(): Promise<
  NotificationPage
> {
  const response = await api.get<NotificationPage>(
    "/v1/notifications?size=100"
  );
  return response.data;
}

export async function markNotificationRead(
  notificationId: number
): Promise<void> {
  await api.patch(
    `/v1/notifications/${notificationId}/read`
  );
}

export async function markAllNotificationsRead(): Promise<void> {
  await api.patch(
    "/v1/notifications/read-all"
  );
}
