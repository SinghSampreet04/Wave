import clsx from "clsx";
import type { ReactNode } from "react";

import {
  PresenceBadge,
  type PresenceStatus,
} from "./";

interface OnlineIndicatorProps {
  children: ReactNode;

  status: PresenceStatus;

  size?: "sm" | "md" | "lg";

  className?: string;
}

const offsets = {
  sm: "-bottom-0.5 -right-0.5",
  md: "-bottom-1 -right-1",
  lg: "-bottom-1 -right-1",
};

export default function OnlineIndicator({
  children,
  status,
  size = "md",
  className,
}: OnlineIndicatorProps) {
  return (
    <div
      className={clsx(
        "relative inline-flex",
        className
      )}
    >
      {children}

      <div
        className={clsx(
          "absolute",
          offsets[size]
        )}
      >
        <PresenceBadge
          status={status}
          size={size}
        />
      </div>
    </div>
  );
}