import clsx from "clsx";
import type {
  ButtonHTMLAttributes,
  ReactNode,
} from "react";

import LoadingSpinner from "./LoadingSpinner";

interface ButtonProps
  extends ButtonHTMLAttributes<HTMLButtonElement> {
  children: ReactNode;

  variant?:
    | "primary"
    | "secondary"
    | "danger"
    | "ghost";

  size?: "sm" | "md" | "lg";

  isLoading?: boolean;

  fullWidth?: boolean;
}

const VARIANTS = {
  primary: clsx(
    "text-white",
    "bg-gradient-to-r from-cyan-500 to-blue-600",
    "hover:from-cyan-400 hover:to-blue-500",
    "shadow-lg shadow-cyan-500/20",
    "hover:shadow-cyan-500/35"
  ),

  secondary: clsx(
    "glass-card",
    "text-white",
    "border border-[var(--wave-border)]",
    "hover:border-cyan-400/30"
  ),

  danger: clsx(
    "text-white",
    "bg-gradient-to-r from-red-500 to-rose-600",
    "hover:from-red-400 hover:to-rose-500",
    "shadow-lg shadow-red-500/20"
  ),

  ghost: clsx(
    "bg-transparent",
    "text-[var(--wave-text-secondary)]",
    "hover:bg-white/5",
    "hover:text-white"
  ),
};

const SIZES = {
  sm: "h-9 px-4 text-sm",

  md: "h-11 px-5",

  lg: "h-12 px-6 text-base",
};

export default function Button({
  children,
  variant = "primary",
  size = "md",
  isLoading = false,
  disabled,
  fullWidth = true,
  className,
  ...props
}: ButtonProps) {
  return (
    <button
      type="button"
      disabled={disabled || isLoading}
      className={clsx(
        "inline-flex items-center justify-center gap-2",

        "rounded-premium",

        "font-semibold",

        "select-none",

        "transition-all duration-200",

        "hover:-translate-y-0.5",

        "active:translate-y-0 active:scale-[0.985]",

        "focus-visible:outline-none",

        "focus-visible:ring-2",

        "focus-visible:ring-cyan-400/40",

        "disabled:pointer-events-none",

        "disabled:opacity-60",

        VARIANTS[variant],

        SIZES[size],

        fullWidth && "w-full",

        className
      )}
      {...props}
    >
      {isLoading && <LoadingSpinner size="sm" />}

      {children}
    </button>
  );
}