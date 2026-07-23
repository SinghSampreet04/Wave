import clsx from "clsx";

interface FormErrorProps {
  message?: string;
  className?: string;
}

export default function FormError({
  message,
  className,
}: FormErrorProps) {
  if (!message) {
    return null;
  }

  return (
    <p
      role="alert"
      className={clsx(
        "mt-1 text-sm text-red-400",
        className
      )}
    >
      {message}
    </p>
  );
}