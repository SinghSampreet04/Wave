import {
  useCallback,
  useState,
} from "react";

export default function useLocalStorage<T>(
  key: string,
  initialValue: T
) {
  const [storedValue, setStoredValue] = useState<T>(() => {
    try {
      const item = window.localStorage.getItem(key);

      return item !== null
        ? (JSON.parse(item) as T)
        : initialValue;
    } catch {
      return initialValue;
    }
  });

  const setValue = useCallback(
    (value: T | ((previous: T) => T)) => {
      setStoredValue((previous) => {
        const nextValue =
          value instanceof Function
            ? value(previous)
            : value;

        try {
          window.localStorage.setItem(
            key,
            JSON.stringify(nextValue)
          );
        } catch {
          // Ignore storage errors (e.g. quota exceeded)
        }

        return nextValue;
      });
    },
    [key]
  );

  const removeValue = useCallback(() => {
    try {
      window.localStorage.removeItem(key);
    } finally {
      setStoredValue(initialValue);
    }
  }, [initialValue, key]);

  return {
    value: storedValue,
    setValue,
    removeValue,
  };
}