import { useCallback, useState } from "react";

export interface UseToggleReturn {
  value: boolean;

  setValue: (value: boolean) => void;

  toggle: () => void;

  open: () => void;

  close: () => void;
}

export default function useToggle(
  initialValue = false
): UseToggleReturn {
  const [value, setValue] = useState(initialValue);

  const toggle = useCallback(() => {
    setValue((previous) => !previous);
  }, []);

  const open = useCallback(() => {
    setValue(true);
  }, []);

  const close = useCallback(() => {
    setValue(false);
  }, []);

  return {
    value,
    setValue,
    toggle,
    open,
    close,
  };
}