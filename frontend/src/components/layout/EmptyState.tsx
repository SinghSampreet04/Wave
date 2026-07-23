import clsx from "clsx";
import type { HTMLAttributes, ReactNode } from "react";

interface EmptyStateProps
  extends HTMLAttributes<HTMLDivElement> {
  icon?: ReactNode;

  heading: ReactNode;

  description?: ReactNode;

  action?: ReactNode;
}

export default function EmptyState({
  icon,
  heading,
  description,
  action,
  className,
  ...props
}: EmptyStateProps) {
  return (
    <div
      className={clsx(
        "flex flex-col items-center justify-center rounded-2xl border border-dashed border-white/10 bg-white/[0.02] px-8 py-16 text-center",
        className
      )}
      {...props}
    >
      {icon && (
        <div className="mb-5 flex h-16 w-16 items-center justify-center rounded-full bg-cyan-500/10 text-cyan-400">
          {icon}
        </div>
      )}

      <h2 className="text-xl font-semibold text-white">
        {heading}
      </h2>

      {description && (
        <p className="mt-3 max-w-md text-sm leading-6 text-slate-400">
          {description}
        </p>
      )}

      {action && (
        <div className="mt-8">
          {action}
        </div>
      )}
    </div>
  );
}