import { useSyncExternalStore } from "react";

function subscribe(query: MediaQueryList, callback: () => void) {
  query.addEventListener("change", callback);

  return () => {
    query.removeEventListener("change", callback);
  };
}

export default function useMediaQuery(
  mediaQuery: string
): boolean {
  const getSnapshot = () =>
    window.matchMedia(mediaQuery).matches;

  const getServerSnapshot = () => false;

  return useSyncExternalStore(
    (callback) =>
      subscribe(
        window.matchMedia(mediaQuery),
        callback
      ),
    getSnapshot,
    getServerSnapshot
  );
}