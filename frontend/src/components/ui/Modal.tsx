import clsx from "clsx";
import type { ReactNode } from "react";

interface ModalProps {
  open: boolean;
  onClose: () => void;

  title?: string;

  children: ReactNode;

  footer?: ReactNode;

  className?: string;
}

export default function Modal({
  open,
  onClose,
  title,
  children,
  footer,
  className,
}: ModalProps) {
  if (!open) {
    return null;
  }

  return (
    <div
      className={clsx(
        "fixed inset-0 z-[var(--z-modal)]",

        "flex items-center justify-center",

        "bg-black/65",

        "backdrop-blur-md",

        "p-6",

        "animate-fade-in"
      )}
      onClick={onClose}
      role="dialog"
      aria-modal="true"
    >
      <div
        onClick={(e) => e.stopPropagation()}
        className={clsx(
          "glass-panel",

          "rounded-premium",

          "w-full",

          "max-w-lg",

          "overflow-hidden",

          "border border-[var(--wave-border)]",

          "shadow-[var(--shadow-xl)]",

          "animate-scale-in",

          className
        )}
      >
        {title && (
          <div
            className={clsx(
              "border-b border-[var(--wave-border)]",

              "px-6 py-5"
            )}
          >
            <h2 className="text-xl font-semibold text-[var(--wave-text)]">
              {title}
            </h2>
          </div>
        )}

        <div className="p-6">
          {children}
        </div>

        {footer && (
          <div
            className={clsx(
              "flex items-center justify-end gap-3",

              "border-t border-[var(--wave-border)]",

              "bg-white/[0.02]",

              "px-6 py-5"
            )}
          >
            {footer}
          </div>
        )}
      </div>
    </div>
  );
}