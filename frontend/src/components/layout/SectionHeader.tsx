import clsx from "clsx";
import type { HTMLAttributes, ReactNode } from "react";

interface SectionHeaderProps
  extends HTMLAttributes<HTMLDivElement> {
  heading: ReactNode;
  subtitle?: ReactNode;
  actions?: ReactNode;
}

export default function SectionHeader({
  heading,
  subtitle,
  actions,
  className,
  ...props
}: SectionHeaderProps) {
  return (
    <div
      className={clsx(
        "flex items-start justify-between gap-4",
        className
      )}
      {...props}
    >
      <div className="min-w-0">
        <h1 className="text-2xl font-bold text-white">
          {heading}
        </h1>

        {subtitle && (
          <p className="mt-1 text-sm text-slate-400">
            {subtitle}
          </p>
        )}
      </div>

      {actions && (
        <div className="flex items-center gap-2">
          {actions}
        </div>
      )}
    </div>
  );
}