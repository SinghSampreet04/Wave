import clsx from "clsx";
import type { HTMLAttributes, ReactNode } from "react";

interface SidebarProps
  extends HTMLAttributes<HTMLElement> {
  children?: ReactNode;

  header?: ReactNode;

  footer?: ReactNode;

  width?: "sm" | "md" | "lg";
}

const widths = {
  sm: "w-56",
  md: "w-72",
  lg: "w-80",
};

export default function Sidebar({
  children,
  header,
  footer,
  width = "md",
  className,
  ...props
}: SidebarProps) {
  return (
    <aside
      className={clsx(
        "flex h-full flex-col border-r border-white/10 bg-[#111827]",
        widths[width],
        className
      )}
      {...props}
    >
      {header && (
        <div className="border-b border-white/10 p-4">
          {header}
        </div>
      )}

      <div className="flex-1 overflow-y-auto">
        {children}
      </div>

      {footer && (
        <div className="border-t border-white/10 p-4">
          {footer}
        </div>
      )}
    </aside>
  );
}