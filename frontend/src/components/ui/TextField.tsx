import type { InputHTMLAttributes } from "react";

import FormError from "./FormError";
import Input from "./Input";
import Label from "./Label";

interface TextFieldProps
  extends InputHTMLAttributes<HTMLInputElement> {
  id: string;
  label: string;
  error?: string;
}

export default function TextField({
  id,
  label,
  error,
  className,
  ...props
}: TextFieldProps) {
  return (
    <div className="space-y-2">
      <Label htmlFor={id}>
        {label}
      </Label>

      <Input
        id={id}
        className={className}
        aria-invalid={!!error}
        aria-describedby={
          error ? `${id}-error` : undefined
        }
        {...props}
      />

      <FormError
        message={error}
        className="mt-0"
      />
    </div>
  );
}