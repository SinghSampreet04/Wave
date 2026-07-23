import {
  forwardRef,
  useState,
  type InputHTMLAttributes,
} from "react";

import clsx from "clsx";
import { Eye, EyeOff } from "lucide-react";

import IconButton from "./IconButton";

type PasswordInputProps =
  InputHTMLAttributes<HTMLInputElement>;

const PasswordInput = forwardRef<
  HTMLInputElement,
  PasswordInputProps
>(function PasswordInput(
  { className, ...props },
  ref
) {
  const [showPassword, setShowPassword] =
    useState(false);

  return (
    <div className="relative w-full">
      <input
        ref={ref}
        type={
          showPassword
            ? "text"
            : "password"
        }
        className={clsx(
          "w-full",

          "rounded-premium",

          "glass-card",

          "border border-[var(--wave-border)]",

          "bg-transparent",

          "px-4 py-3 pr-12",

          "text-[var(--wave-text)]",

          "placeholder:text-[var(--wave-text-muted)]",

          "shadow-[var(--shadow-sm)]",

          "transition-all duration-200",

          "focus:outline-none",

          "focus:border-cyan-400/40",

          "focus:ring-2",

          "focus:ring-cyan-400/20",

          "focus:shadow-[var(--shadow-glow)]",

          "disabled:pointer-events-none",

          "disabled:opacity-60",

          className
        )}
        {...props}
      />

      <div className="absolute inset-y-0 right-2 flex items-center">
        <IconButton
          type="button"
          variant="ghost"
          size="sm"
          icon={
            showPassword ? (
              <EyeOff size={18} />
            ) : (
              <Eye size={18} />
            )
          }
          aria-label={
            showPassword
              ? "Hide password"
              : "Show password"
          }
          aria-pressed={showPassword}
          onClick={() =>
            setShowPassword(
              (value) => !value
            )
          }
        />
      </div>
    </div>
  );
});

export default PasswordInput;