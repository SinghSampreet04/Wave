import clsx from "clsx";
import type {
  HTMLAttributes,
  ReactNode,
} from "react";

export type StatusBadgeVariant =
  | "default"
  | "success"
  | "warning"
  | "danger"
  | "info";

interface StatusBadgeProps
  extends HTMLAttributes<HTMLSpanElement> {
  variant?: StatusBadgeVariant;

  children: ReactNode;
}

const VARIANTS = {
  default: clsx(
    "border-[var(--wave-border)]",
    "bg-white/[0.04]",
    "text-[var(--wave-text-secondary)]"
  ),

  success: clsx(
    "border-emerald-500/20",
    "bg-emerald-500/10",
    "text-emerald-400"
  ),

  warning: clsx(
    "border-amber-500/20",
    "bg-amber-500/10",
    "text-amber-400"
  ),

  danger: clsx(
    "border-red-500/20",
    "bg-red-500/10",
    "text-red-400"
  ),

  info: clsx(
    "border-cyan-500/20",
    "bg-cyan-500/10",
    "text-cyan-400"
  ),
};

export default function StatusBadge({
  variant = "default",
  children,
  className,
  ...props
}: StatusBadgeProps) {
  return (
    <span
      className={clsx(
        "inline-flex items-center justify-center",

        "rounded-full",

        "border",

        "px-2.5 py-1",

        "text-xs",

        "font-semibold",

        "tracking-wide",

        "select-none",

        "transition-all duration-200",

        VARIANTS[variant],

        className
      )}
      {...props}
    >
      {children}
    </span>
  );
}