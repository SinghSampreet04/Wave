import clsx from "clsx";

export type PresenceStatus =
  | "online"
  | "away"
  | "busy"
  | "offline";

interface PresenceBadgeProps {
  status: PresenceStatus;

  size?: "sm" | "md" | "lg";

  className?: string;
}

const SIZES = {
  sm: "h-2 w-2",
  md: "h-3 w-3",
  lg: "h-4 w-4",
};

const COLORS = {
  online: "bg-emerald-500",
  away: "bg-amber-400",
  busy: "bg-red-500",
  offline: "bg-slate-500",
};

export default function PresenceBadge({
  status,
  size = "md",
  className,
}: PresenceBadgeProps) {
  return (
    <span
      aria-label={status}
      role="status"
      className={clsx(
        "inline-block",

        "rounded-full",

        "border-2 border-[var(--wave-bg-primary)]",

        "shadow-sm",

        SIZES[size],

        COLORS[status],

        status === "online" &&
          "animate-pulse",

        className
      )}
    />
  );
}