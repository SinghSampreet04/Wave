import clsx from "clsx";
import type {
  HTMLAttributes,
  ReactNode,
} from "react";

interface MenuProps
  extends HTMLAttributes<HTMLDivElement> {
  children: ReactNode;
}

export default function Menu({
  children,
  className,
  ...props
}: MenuProps) {
  return (
    <div
      className={clsx(
        "rounded-premium",

        "min-w-[220px]",

        "overflow-hidden",

        "isolate border border-white/15 bg-slate-950/98",

        "shadow-2xl shadow-black/70 backdrop-blur-xl",

        "animate-scale-in",

        className
      )}
      {...props}
    >
      {children}
    </div>
  );
}
