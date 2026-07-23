import clsx from "clsx";
import type { HTMLAttributes, ReactNode } from "react";

interface PageContainerProps
  extends HTMLAttributes<HTMLDivElement> {
  children: ReactNode;

  maxWidth?:
    | "full"
    | "7xl"
    | "6xl"
    | "5xl";

  padding?: boolean;
}

const maxWidths = {
  full: "max-w-none",
  "7xl": "max-w-7xl",
  "6xl": "max-w-6xl",
  "5xl": "max-w-5xl",
};

export default function PageContainer({
  children,
  maxWidth = "7xl",
  padding = true,
  className,
  ...props
}: PageContainerProps) {
  return (
    <div
      className={clsx(
        "mx-auto w-full",
        maxWidths[maxWidth],
        padding && "px-6 py-6",
        className
      )}
      {...props}
    >
      {children}
    </div>
  );
}