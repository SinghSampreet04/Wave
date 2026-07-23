import clsx from "clsx";
import type { HTMLAttributes } from "react";

interface SkeletonProps
  extends HTMLAttributes<HTMLDivElement> {
  width?: string;
  height?: string;
  rounded?: "sm" | "md" | "lg" | "full";
}

const roundedClasses = {
  sm: "rounded",
  md: "rounded-md",
  lg: "rounded-lg",
  full: "rounded-full",
};

export default function Skeleton({
  width = "100%",
  height = "1rem",
  rounded = "md",
  className,
  style,
  ...props
}: SkeletonProps) {
  return (
    <div
      className={clsx(
        "animate-pulse bg-slate-700/50",
        roundedClasses[rounded],
        className
      )}
      style={{
        width,
        height,
        ...style,
      }}
      aria-hidden="true"
      {...props}
    />
  );
}