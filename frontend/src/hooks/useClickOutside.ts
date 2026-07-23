import { useEffect } from "react";
import type { RefObject } from "react";

export default function useClickOutside<T extends HTMLElement>(
  ref: RefObject<T | null>,
  handler: () => void,
  enabled = true
) {
  useEffect(() => {
    if (!enabled) {
      return;
    }

    function handlePointerUp(
      event: MouseEvent | TouchEvent
    ) {
      const element = ref.current;

      if (!element) {
        return;
      }

      const target = event.target;

      if (!(target instanceof Node)) {
        return;
      }

      if (!element.contains(target)) {
        handler();
      }
    }

    document.addEventListener(
      "mouseup",
      handlePointerUp
    );

    document.addEventListener(
      "touchend",
      handlePointerUp
    );

    return () => {
      document.removeEventListener(
        "mouseup",
        handlePointerUp
      );

      document.removeEventListener(
        "touchend",
        handlePointerUp
      );
    };
  }, [ref, handler, enabled]);
}