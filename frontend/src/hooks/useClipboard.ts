import { useCallback, useState } from "react";

interface UseClipboardReturn {
  copied: boolean;
  copy: (text: string) => Promise<boolean>;
  reset: () => void;
}

export default function useClipboard(
  resetDelay = 2000
): UseClipboardReturn {
  const [copied, setCopied] = useState(false);

  const copy = useCallback(
    async (text: string) => {
      try {
        await navigator.clipboard.writeText(text);

        setCopied(true);

        window.setTimeout(() => {
          setCopied(false);
        }, resetDelay);

        return true;
      } catch {
        setCopied(false);
        return false;
      }
    },
    [resetDelay]
  );

  const reset = useCallback(() => {
    setCopied(false);
  }, []);

  return {
    copied,
    copy,
    reset,
  };
}