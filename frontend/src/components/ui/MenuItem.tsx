import clsx from "clsx";
import type {
  ButtonHTMLAttributes,
  ReactNode,
} from "react";

interface MenuItemProps
  extends ButtonHTMLAttributes<HTMLButtonElement> {
  icon?: ReactNode;

  danger?: boolean;
}

export default function MenuItem({
  icon,
  children,
  danger = false,
  className,
  ...props
}: MenuItemProps) {
  return (
    <button
      className={clsx(
        "group",

        "flex w-full items-center gap-3",

        "px-4 py-2.5",

        "text-sm font-medium",

        "transition-all duration-200",

        "select-none",

        "focus-visible:outline-none",

        "focus-visible:bg-white/10",

        "disabled:pointer-events-none",

        "disabled:opacity-50",

        danger
          ? [
              "text-red-400",
              "hover:bg-red-500/10",
              "hover:text-red-300",
              "focus-visible:bg-red-500/10",
            ]
          : [
              "text-[var(--wave-text-secondary)]",
              "hover:bg-white/5",
              "hover:text-[var(--wave-text)]",
              "focus-visible:text-[var(--wave-text)]",
            ],

        className
      )}
      {...props}
    >
      {icon && (
        <span
          className={clsx(
            "flex h-5 w-5 items-center justify-center",

            "transition-colors duration-200",

            "group-hover:text-inherit"
          )}
        >
          {icon}
        </span>
      )}

      <span className="flex-1 text-left">
        {children}
      </span>
    </button>
  );
}