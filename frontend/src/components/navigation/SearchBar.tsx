import { Search, X } from "lucide-react";
import clsx from "clsx";
import {
  forwardRef,
  type InputHTMLAttributes,
} from "react";

import { Input, IconButton } from "../ui";

type SearchBarProps = Omit<
  InputHTMLAttributes<HTMLInputElement>,
  "type"
>;

const SearchBar = forwardRef<
  HTMLInputElement,
  SearchBarProps
>(function SearchBar({
    value,
    onChange,
    placeholder = "Search...",
    className,
    ...props
  }, ref) {
  const hasValue =
    typeof value === "string" &&
    value.length > 0;

  return (
    <div
      className={clsx(
        "relative w-full",
        className
      )}
    >
      <Search
        size={18}
        className="pointer-events-none absolute left-3 top-1/2 -translate-y-1/2 text-slate-400"
      />

      <Input
        ref={ref}
        {...props}
        type="text"
        value={value}
        onChange={onChange}
        placeholder={placeholder}
        className="pl-10 pr-20"
      />

      <div className="absolute right-2 top-1/2 flex -translate-y-1/2 items-center gap-1">
        {hasValue && (
          <IconButton
            icon={<X size={14} />}
            aria-label="Clear search"
            variant="ghost"
            size="sm"
            onClick={() => {
              if (props.disabled) return;

              const target =
                document.activeElement as HTMLInputElement | null;

              target?.focus();

              const event = {
                target: {
                  value: "",
                },
              } as React.ChangeEvent<HTMLInputElement>;

              onChange?.(event);
            }}
          />
        )}

        <span className="rounded border border-white/10 bg-slate-800 px-2 py-1 text-[10px] font-medium uppercase tracking-wide text-slate-400">
          ⌘K
        </span>
      </div>
    </div>
  );
});

export default SearchBar;
