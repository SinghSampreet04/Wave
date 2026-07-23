import clsx from "clsx";
import type {
  HTMLAttributes,
  ReactNode,
} from "react";

interface NavGroupProps
  extends HTMLAttributes<HTMLDivElement> {
  heading: ReactNode;

  action?: ReactNode;

  children: ReactNode;
}

export default function NavGroup({
  heading,
  action,
  children,
  className,
  ...props
}: NavGroupProps) {
  return (
    <section
      className={clsx("space-y-2", className)}
      {...props}
    >
      <div className="flex items-center justify-between px-3">
        <h2 className="text-xs font-semibold uppercase tracking-wider text-slate-500">
          {heading}
        </h2>

        {action && (
          <div className="flex items-center">
            {action}
          </div>
        )}
      </div>

      <div className="space-y-1">
        {children}
      </div>
    </section>
  );
}