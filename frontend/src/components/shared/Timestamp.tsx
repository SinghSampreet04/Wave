import clsx from "clsx";
import {
  useEffect,
  useMemo,
  useState,
  type HTMLAttributes,
} from "react";

interface TimestampProps
  extends HTMLAttributes<HTMLTimeElement> {
  value: Date | string | number;

  format?: "relative" | "time" | "date" | "datetime";

  locale?: string;
}

export default function Timestamp({
  value,
  format = "relative",
  locale,
  className,
  ...props
}: TimestampProps) {
  const date = useMemo(
    () => (value instanceof Date ? value : new Date(value)),
    [value]
  );

  const [now, setNow] = useState(() => new Date());

  useEffect(() => {
    if (format !== "relative") {
      return;
    }

    const interval = window.setInterval(() => {
      setNow(new Date());
    }, 60_000);

    return () => window.clearInterval(interval);
  }, [format]);

  const text = useMemo(() => {
    switch (format) {
      case "time":
        return date.toLocaleTimeString(locale, {
          hour: "numeric",
          minute: "2-digit",
        });

      case "date":
        return date.toLocaleDateString(locale, {
          year: "numeric",
          month: "short",
          day: "numeric",
        });

      case "datetime":
        return date.toLocaleString(locale, {
          year: "numeric",
          month: "short",
          day: "numeric",
          hour: "numeric",
          minute: "2-digit",
        });

      case "relative":
      default: {
        const diffSeconds = Math.round(
          (date.getTime() - now.getTime()) / 1000
        );

        const rtf = new Intl.RelativeTimeFormat(locale, {
          numeric: "auto",
        });

        const abs = Math.abs(diffSeconds);

        if (abs < 60) {
          return rtf.format(diffSeconds, "second");
        }

        const minutes = Math.round(diffSeconds / 60);

        if (Math.abs(minutes) < 60) {
          return rtf.format(minutes, "minute");
        }

        const hours = Math.round(minutes / 60);

        if (Math.abs(hours) < 24) {
          return rtf.format(hours, "hour");
        }

        const days = Math.round(hours / 24);

        if (Math.abs(days) < 7) {
          return rtf.format(days, "day");
        }

        return date.toLocaleDateString(locale, {
          month: "short",
          day: "numeric",
          year:
            date.getFullYear() !==
            now.getFullYear()
              ? "numeric"
              : undefined,
        });
      }
    }
  }, [date, format, locale, now]);

  return (
    <time
      dateTime={date.toISOString()}
      title={date.toLocaleString(locale)}
      className={clsx(
        "text-sm text-slate-400",
        className
      )}
      {...props}
    >
      {text}
    </time>
  );
}
