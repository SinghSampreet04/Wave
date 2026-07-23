import clsx from "clsx";
import {
  forwardRef,
  type InputHTMLAttributes,
} from "react";

interface InputProps
  extends InputHTMLAttributes<HTMLInputElement> {
  label?: string;

  error?: string;

  fullWidth?: boolean;
}

const Input = forwardRef<
  HTMLInputElement,
  InputProps
>(function Input({
    label,
    error,
    fullWidth = true,
    className,
    ...props
  }, ref) {
  return (
    <div className={clsx(fullWidth && "w-full")}>
      {label && (
        <label className="mb-2 block text-sm font-semibold text-[var(--wave-text-secondary)]">
          {label}
        </label>
      )}

      <input
        ref={ref}
        className={clsx(
          "w-full",

          "rounded-premium",

          "glass-card",

          "border border-[var(--wave-border)]",

          "bg-transparent",

          "px-4 py-3",

          "text-[var(--wave-text)]",

          "placeholder:text-[var(--wave-text-muted)]",

          "shadow-[var(--shadow-sm)]",

          "transition-all duration-200",

          "focus:outline-none",

          "focus:border-cyan-400/40",

          "focus:ring-2",

          "focus:ring-cyan-400/20",

          "focus:shadow-[var(--shadow-glow)]",

          "disabled:cursor-not-allowed",

          "disabled:opacity-60",

          error && [
            "border-red-500/70",
            "focus:border-red-400",
            "focus:ring-red-500/20",
          ],

          className
        )}
        {...props}
      />

      {error && (
        <p className="mt-2 text-sm font-medium text-red-400">
          {error}
        </p>
      )}
    </div>
  );
});

export default Input;
