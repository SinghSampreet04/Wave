import clsx from "clsx";
import type { LabelHTMLAttributes } from "react";

type LabelProps =
  LabelHTMLAttributes<HTMLLabelElement>;

export default function Label({
  children,
  className,
  ...props
}: LabelProps) {
  return (
    <label
      className={clsx(
        "block text-sm font-medium text-slate-300",
        className
      )}
      {...props}
    >
      {children}
    </label>
  );
}