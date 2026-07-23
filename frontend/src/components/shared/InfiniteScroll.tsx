import {
  useEffect,
  useRef,
  type ReactNode,
} from "react";

interface InfiniteScrollProps {
  children: ReactNode;

  hasMore: boolean;

  loading?: boolean;

  onLoadMore: () => void;

  rootMargin?: string;

  className?: string;
}

export default function InfiniteScroll({
  children,
  hasMore,
  loading = false,
  onLoadMore,
  rootMargin = "200px",
  className,
}: InfiniteScrollProps) {
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
        rootMargin,
      }
    );

    observer.observe(sentinel);

    return () => observer.disconnect();
  }, [
    hasMore,
    loading,
    onLoadMore,
    rootMargin,
  ]);

  return (
    <div className={className}>
      {children}

      <div
        ref={sentinelRef}
        aria-hidden
      />
    </div>
  );
}