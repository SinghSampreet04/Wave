import clsx from "clsx";
import { AlertTriangle } from "lucide-react";
import type { HTMLAttributes, ReactNode } from "react";

import { Button } from "../ui";

interface ErrorStateProps
  extends HTMLAttributes<HTMLDivElement> {
  heading?: ReactNode;

  description?: ReactNode;

  actionLabel?: string;

  onAction?: () => void;

  icon?: ReactNode;
}

export default function ErrorState({
  heading = "Something went wrong",
  description = "An unexpected error occurred.",
  actionLabel,
  onAction,
  icon,
  className,
  ...props
}: ErrorStateProps) {
  return (
    <div
      className={clsx(
        "flex flex-col items-center justify-center rounded-2xl border border-red-500/20 bg-red-500/5 px-8 py-12 text-center",
        className
      )}
      {...props}
    >
      <div className="mb-5 rounded-full bg-red-500/10 p-4 text-red-400">
        {icon ?? <AlertTriangle size={30} />}
      </div>

      <h2 className="text-xl font-semibold text-white">
        {heading}
      </h2>

      <p className="mt-3 max-w-md text-sm leading-6 text-slate-400">
        {description}
      </p>

      {actionLabel && onAction && (
        <div className="mt-8">
          <Button onClick={onAction}>
            {actionLabel}
          </Button>
        </div>
      )}
    </div>
  );
}