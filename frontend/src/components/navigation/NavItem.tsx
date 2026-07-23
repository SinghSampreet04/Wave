import clsx from "clsx";
import type {
  AnchorHTMLAttributes,
  ReactNode,
} from "react";

interface NavItemProps
  extends AnchorHTMLAttributes<HTMLAnchorElement> {
  icon?: ReactNode;

  active?: boolean;

  endContent?: ReactNode;
}

export default function NavItem({
  icon,
  active = false,
  endContent,
  children,
  className,
  ...props
}: NavItemProps) {
  return (
    <a
      className={clsx(
        "flex items-center justify-between rounded-lg px-3 py-2 transition-colors",
        active
          ? "bg-cyan-500/15 text-cyan-400"
          : "text-slate-300 hover:bg-white/5 hover:text-white",
        className
      )}
      {...props}
    >
      <span className="flex min-w-0 items-center gap-3">
        {icon}

        <span className="truncate">
          {children}
        </span>
      </span>

      {endContent}
    </a>
  );
}