import clsx from "clsx";
import type {
  HTMLAttributes,
  ReactNode,
} from "react";

import { Avatar } from "../ui";
import OnlineIndicator from "./OnlineIndicator";
import Timestamp from "./Timestamp";
import type { PresenceStatus } from "./PresenceBadge";

interface UserCardProps
  extends HTMLAttributes<HTMLDivElement> {
  name: string;

  email?: string;

  subtitle?: ReactNode;

  status?: PresenceStatus;

  avatarSize?: "sm" | "md" | "lg";

  lastSeen?: Date | string | number;

  action?: ReactNode;
}

export default function UserCard({
  name,
  email,
  subtitle,
  status = "offline",
  avatarSize = "md",
  lastSeen,
  action,
  className,
  ...props
}: UserCardProps) {
  return (
    <div
      className={clsx(
        "flex items-center justify-between rounded-xl p-3 transition-colors hover:bg-white/5",
        className
      )}
      {...props}
    >
      <div className="flex min-w-0 items-center gap-3">
        <OnlineIndicator
          status={status}
          size={avatarSize}
        >
          <Avatar
            name={name}
            size={avatarSize}
          />
        </OnlineIndicator>

        <div className="min-w-0">
          <p className="truncate font-medium text-white">
            {name}
          </p>

          {email && (
            <p className="truncate text-sm text-slate-400">
              {email}
            </p>
          )}

          {subtitle && (
            <div className="mt-1 text-xs text-slate-500">
              {subtitle}
            </div>
          )}

          {lastSeen && (
            <Timestamp
              value={lastSeen}
              className="mt-1 block text-xs"
            />
          )}
        </div>
      </div>

      {action && (
        <div className="ml-4 flex items-center">
          {action}
        </div>
      )}
    </div>
  );
}