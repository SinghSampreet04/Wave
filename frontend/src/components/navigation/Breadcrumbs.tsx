import clsx from "clsx";
import { Fragment, type HTMLAttributes, type ReactNode } from "react";
import { ChevronRight } from "lucide-react";

export interface BreadcrumbItem {
  label: ReactNode;
  href?: string;
}

interface BreadcrumbsProps
  extends HTMLAttributes<HTMLElement> {
  items: BreadcrumbItem[];
}

export default function Breadcrumbs({
  items,
  className,
  ...props
}: BreadcrumbsProps) {
  return (
    <nav
      aria-label="Breadcrumb"
      className={clsx(
        "flex items-center text-sm text-slate-400",
        className
      )}
      {...props}
    >
      {items.map((item, index) => {
        const isLast = index === items.length - 1;

        return (
          <Fragment key={index}>
            {item.href && !isLast ? (
              <a
                href={item.href}
                className="transition-colors hover:text-white"
              >
                {item.label}
              </a>
            ) : (
              <span
                className={clsx(
                  isLast
                    ? "font-medium text-white"
                    : "text-slate-400"
                )}
              >
                {item.label}
              </span>
            )}

            {!isLast && (
              <ChevronRight
                size={16}
                className="mx-2 text-slate-500"
              />
            )}
          </Fragment>
        );
      })}
    </nav>
  );
}