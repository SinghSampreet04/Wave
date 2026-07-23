import clsx from "clsx";
import type {
  ButtonHTMLAttributes,
  ReactNode,
} from "react";

interface IconButtonProps
  extends ButtonHTMLAttributes<HTMLButtonElement> {
  icon: ReactNode;

  variant?: "ghost" | "primary" | "danger";

  size?: "sm" | "md" | "lg";

  active?: boolean;
}

const VARIANTS = {
  ghost: clsx(
    "glass-card",
    "text-[var(--wave-text-secondary)]",
    "border border-transparent",
    "hover:border-cyan-400/20",
    "hover:text-white"
  ),

  primary: clsx(
    "text-white",
    "bg-gradient-to-br from-cyan-500 to-blue-600",
    "shadow-lg shadow-cyan-500/20",
    "hover:shadow-cyan-500/35"
  ),

  danger: clsx(
    "text-red-400",
    "glass-card",
    "border border-transparent",
    "hover:border-red-500/30",
    "hover:bg-red-500/10",
    "hover:text-red-300"
  ),
};

const SIZES = {
  sm: "h-8 w-8 text-sm",

  md: "h-10 w-10 text-base",

  lg: "h-12 w-12 text-lg",
};

export default function IconButton({
  icon,
  className,
  variant = "ghost",
  size = "md",
  active = false,
  disabled,
  ...props
}: IconButtonProps) {
  return (
    <button
      type="button"
      disabled={disabled}
      className={clsx(
        "inline-flex items-center justify-center",

        "rounded-premium",

        "select-none",

        "transition-all duration-200",

        "hover:-translate-y-0.5",

        "active:translate-y-0",

        "active:scale-[0.96]",

        "focus-visible:outline-none",

        "focus-visible:ring-2",

        "focus-visible:ring-cyan-400/30",

        "disabled:pointer-events-none",

        "disabled:opacity-60",

        VARIANTS[variant],

        SIZES[size],

        active && [
          "border-cyan-400/30",
          "bg-cyan-500/15",
          "text-white",
          "shadow-[var(--shadow-glow)]",
        ],

        className
      )}
      {...props}
    >
      {icon}
    </button>
  );
}