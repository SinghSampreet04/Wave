import clsx from "clsx";
import type { HTMLAttributes } from "react";

import { LoadingSpinner } from "../ui";

interface LoadingOverlayProps
  extends HTMLAttributes<HTMLDivElement> {
  loading: boolean;

  message?: string;
}

export default function LoadingOverlay({
  loading,
  message = "Loading...",
  className,
  children,
  ...props
}: LoadingOverlayProps) {
  if (!loading) {
    return <>{children}</>;
  }

  return (
    <div
      className={clsx(
        "relative",
        className
      )}
      {...props}
    >
      {children}

      <div className="absolute inset-0 flex flex-col items-center justify-center gap-3 rounded-xl bg-slate-950/70 backdrop-blur-sm">
        <LoadingSpinner size="lg" />

        <p className="text-sm text-slate-300">
          {message}
        </p>
      </div>
    </div>
  );
}