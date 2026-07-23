import clsx from "clsx";
import type { HTMLAttributes, ReactNode } from "react";

interface TopBarProps
  extends HTMLAttributes<HTMLElement> {
  leading?: ReactNode;

  center?: ReactNode;

  trailing?: ReactNode;
}

export default function TopBar({
  leading,
  center,
  trailing,
  className,
  ...props
}: TopBarProps) {
  return (
    <header
      className={clsx(
        "flex h-16 items-center justify-between border-b border-white/10 bg-[#0f172a] px-6",
        className
      )}
      {...props}
    >
      <div className="flex min-w-0 items-center gap-4">
        {leading}
      </div>

      <div className="mx-6 flex min-w-0 flex-1 justify-center">
        {center}
      </div>

      <div className="flex items-center gap-3">
        {trailing}
      </div>
    </header>
  );
}