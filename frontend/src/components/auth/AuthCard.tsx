import type { ReactNode } from "react";
import clsx from "clsx";

import { Card } from "../ui";

interface AuthCardProps {
  children: ReactNode;
  className?: string;
}

export default function AuthCard({
  children,
  className,
}: AuthCardProps) {
  return (
    <Card
      padding="none"
      className={clsx(
        "relative w-full max-w-md overflow-hidden rounded-3xl",
        "border border-white/10",
        "bg-white/5 backdrop-blur-3xl",
        "shadow-[0_20px_60px_rgba(0,0,0,0.45)]",
        className
      )}
    >
      {/* Top glow */}
      <div className="absolute inset-x-0 top-0 h-px bg-gradient-to-r from-transparent via-cyan-400/70 to-transparent" />

      {/* Background glow */}
      <div className="absolute -top-24 left-1/2 h-48 w-48 -translate-x-1/2 rounded-full bg-cyan-500/10 blur-3xl" />

      {/* Content */}
      <div className="relative p-10">
        {children}
      </div>
    </Card>
  );
}