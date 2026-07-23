import clsx from "clsx";

interface LoadingSpinnerProps {
  size?: "sm" | "md" | "lg";
  className?: string;
}

const SIZES = {
  sm: "h-4 w-4 border-2",
  md: "h-5 w-5 border-2",
  lg: "h-8 w-8 border-[3px]",
};

export default function LoadingSpinner({
  size = "md",
  className,
}: LoadingSpinnerProps) {
  return (
    <span
      className={clsx(
        "inline-block",

        "animate-spin",

        "rounded-full",

        "border-current",

        "border-t-transparent",

        "opacity-90",

        "drop-shadow-[0_0_8px_rgba(34,211,238,0.25)]",

        SIZES[size],

        className
      )}
      aria-hidden="true"
      role="status"
    />
  );
}