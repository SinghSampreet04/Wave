import clsx from "clsx";
import type {
  HTMLAttributes,
  ReactNode,
} from "react";

interface CardProps
  extends HTMLAttributes<HTMLDivElement> {
  children: ReactNode;

  padding?: "none" | "sm" | "md" | "lg";

  hover?: boolean;
}

const PADDING = {
  none: "",
  sm: "p-4",
  md: "p-6",
  lg: "p-8",
};

export default function Card({
  children,
  padding = "md",
  hover = false,
  className,
  ...props
}: CardProps) {
  return (
    <div
      className={clsx(
        "glass-card",

        "rounded-premium",

        "border border-[var(--wave-border)]",

        "shadow-[var(--shadow-md)]",

        "overflow-hidden",

        PADDING[padding],

        hover && [
          "hover-lift",
          "cursor-pointer",
          "hover:border-cyan-400/30",
          "hover:shadow-[var(--shadow-xl)]",
        ],

        className
      )}
      {...props}
    >
      {children}
    </div>
  );
}