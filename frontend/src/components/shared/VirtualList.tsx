import {
  useMemo,
  useState,
  type ReactNode,
  type UIEvent,
} from "react";
import clsx from "clsx";

interface VirtualListProps<T> {
  items: T[];

  itemHeight: number;

  height: number;

  renderItem: (
    item: T,
    index: number
  ) => ReactNode;

  overscan?: number;

  className?: string;
}

export default function VirtualList<T>({
  items,
  itemHeight,
  height,
  renderItem,
  overscan = 5,
  className,
}: VirtualListProps<T>) {
  const [scrollTop, setScrollTop] = useState(0);

  const totalHeight = items.length * itemHeight;

  const startIndex = Math.max(
    0,
    Math.floor(scrollTop / itemHeight) - overscan
  );

  const endIndex = Math.min(
    items.length,
    Math.ceil((scrollTop + height) / itemHeight) +
      overscan
  );

  const visibleItems = useMemo(
    () => items.slice(startIndex, endIndex),
    [items, startIndex, endIndex]
  );

  const handleScroll = (
    event: UIEvent<HTMLDivElement>
  ) => {
    setScrollTop(event.currentTarget.scrollTop);
  };

  return (
    <div
      className={clsx(
        "overflow-auto",
        className
      )}
      style={{ height }}
      onScroll={handleScroll}
    >
      <div
        style={{
          height: totalHeight,
          position: "relative",
        }}
      >
        <div
          style={{
            position: "absolute",
            top: startIndex * itemHeight,
            left: 0,
            right: 0,
          }}
        >
          {visibleItems.map((item, index) => (
            <div
              key={startIndex + index}
              style={{
                height: itemHeight,
              }}
            >
              {renderItem(
                item,
                startIndex + index
              )}
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}