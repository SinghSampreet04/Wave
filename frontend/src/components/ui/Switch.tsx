import clsx from "clsx";
import type { ButtonHTMLAttributes } from "react";

interface SwitchProps
  extends Omit<
    ButtonHTMLAttributes<HTMLButtonElement>,
    "onChange"
  > {
  label?: string;

  checked: boolean;

  onCheckedChange: (
    checked: boolean
  ) => void;

  disabled?: boolean;
}

export default function Switch({
  label,
  checked,
  onCheckedChange,
  disabled = false,
  className,
  ...props
}: SwitchProps) {
  return (
    <div className="inline-flex items-center gap-3">
      {label && (
        <span className="text-sm font-medium text-slate-300">
          {label}
        </span>
      )}

      <button
        {...props}
        type="button"
        role="switch"
        aria-checked={checked}
        aria-label={props["aria-label"] ?? label}
        disabled={disabled}
        onClick={() =>
          !disabled &&
          onCheckedChange(!checked)
        }
        className={clsx(
          "relative inline-flex h-6 w-11 shrink-0 items-center",

          "rounded-full",

          "border border-transparent",

          "transition-all duration-200",

          "focus-visible:outline-none",

          "focus-visible:ring-2",

          "focus-visible:ring-cyan-400/30",

          checked
            ? [
                "bg-gradient-to-r",
                "from-cyan-500",
                "to-blue-600",
                "shadow-[var(--shadow-glow)]",
              ]
            : [
                "bg-white/10",
                "border-[var(--wave-border)]",
              ],

          disabled &&
            "cursor-not-allowed opacity-50",

          className
        )}
      >
        <span
          className={clsx(
            "inline-block h-5 w-5",

            "rounded-full",

            "bg-white",

            "shadow-md",

            "transition-all duration-200",

            checked
              ? "translate-x-5"
              : "translate-x-0.5"
          )}
        />
      </button>
    </div>
  );
}
