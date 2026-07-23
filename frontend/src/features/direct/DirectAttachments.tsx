import { Download, FileText } from "lucide-react";
import { useQuery } from "@tanstack/react-query";
import toast from "react-hot-toast";

import {
  downloadDirectAttachment,
  getDirectAttachments,
} from "./api";

export default function DirectAttachments({
  messageId,
}: {
  messageId: number;
}) {
  const { data = [] } = useQuery({
    queryKey: ["direct-attachments", messageId],
    queryFn: () => getDirectAttachments(messageId),
  });

  if (data.length === 0) return null;

  return (
    <div className="mt-2 space-y-1.5">
      {data.map((attachment) => (
        <button
          key={attachment.id}
          type="button"
          onClick={() =>
            void downloadDirectAttachment(attachment).catch(() =>
              toast.error("Unable to download attachment.")
            )
          }
          className="flex w-full items-center gap-2 rounded-xl border border-white/10 bg-slate-950/50 px-3 py-2 text-left text-xs text-slate-200 hover:border-cyan-400/30"
        >
          <FileText size={14} className="shrink-0 text-cyan-300" />
          <span className="min-w-0 flex-1 truncate">
            {attachment.originalFileName}
          </span>
          <span className="text-slate-500">
            {Math.ceil(attachment.fileSize / 1024)} KB
          </span>
          <Download size={13} />
        </button>
      ))}
    </div>
  );
}
