import clsx from "clsx";
import type {
  HTMLAttributes,
  ReactNode,
} from "react";

import FileIcon from "./FileIcon";

interface FilePreviewProps
  extends HTMLAttributes<HTMLDivElement> {
  fileName: string;

  fileSize?: string;

  status?: ReactNode;

  actions?: ReactNode;
}

export default function FilePreview({
  fileName,
  fileSize,
  status,
  actions,
  className,
  ...props
}: FilePreviewProps) {
  return (
    <div
      className={clsx(
        "flex items-center justify-between rounded-xl border border-slate-800 bg-slate-900 p-3",
        className
      )}
      {...props}
    >
      <div className="flex min-w-0 items-center gap-3">
        <FileIcon
          fileName={fileName}
          size={24}
        />

        <div className="min-w-0">
          <p className="truncate font-medium text-white">
            {fileName}
          </p>

          <div className="flex items-center gap-2 text-sm text-slate-400">
            {fileSize && <span>{fileSize}</span>}

            {status && (
              <>
                {fileSize && <span>•</span>}
                {status}
              </>
            )}
          </div>
        </div>
      </div>

      {actions && (
        <div className="ml-4 flex items-center gap-2">
          {actions}
        </div>
      )}
    </div>
  );
}