import {
  useEffect,
  useRef,
  type RefObject,
} from "react";

interface UseInfiniteScrollOptions {
  hasMore: boolean;
  loading?: boolean;
  onLoadMore: () => void;
  root?: Element | null;
  rootMargin?: string;
  threshold?: number;
}

export default function useInfiniteScroll(
  {
    hasMore,
    loading = false,
    onLoadMore,
    root = null,
    rootMargin = "200px",
    threshold = 0,
  }: UseInfiniteScrollOptions,
): RefObject<HTMLDivElement | null> {
  const sentinelRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    const sentinel = sentinelRef.current;

    if (!sentinel) {
      return;
    }

    const observer = new IntersectionObserver(
      ([entry]) => {
        if (
          entry.isIntersecting &&
          hasMore &&
          !loading
        ) {
          onLoadMore();
        }
      },
      {
        root,
        rootMargin,
        threshold,
      },
    );

    observer.observe(sentinel);

    return () => observer.disconnect();
  }, [
    hasMore,
    loading,
    onLoadMore,
    root,
    rootMargin,
    threshold,
  ]);

  return sentinelRef;
}