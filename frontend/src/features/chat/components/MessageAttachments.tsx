import { Download } from "lucide-react";
import { useQuery } from "@tanstack/react-query";

import api from "../../../api/axios";
import FilePreview from "../../../components/shared/FilePreview";

interface Attachment {
  id: number;
  originalFilename: string;
  contentType: string;
  fileSize: number;
}

function formatBytes(value: number) {
  if (value < 1024) return `${value} B`;
  if (value < 1024 * 1024) return `${(value / 1024).toFixed(1)} KB`;
  return `${(value / 1024 / 1024).toFixed(1)} MB`;
}

export default function MessageAttachments({
  messageId,
}: {
  messageId: number;
}) {
  const { data = [] } = useQuery({
    queryKey: ["message-files", messageId],
    queryFn: async () => {
      const response = await api.get<Attachment[]>(
        `/v1/files/message/${messageId}`
      );
      return response.data;
    },
  });

  if (data.length === 0) return null;

  const download = async (attachment: Attachment) => {
    const response = await api.get<Blob>(
      `/v1/files/download/${attachment.id}`,
      { responseType: "blob" }
    );
    const url = URL.createObjectURL(response.data);
    const anchor = document.createElement("a");
    anchor.href = url;
    anchor.download = attachment.originalFilename;
    anchor.click();
    URL.revokeObjectURL(url);
  };

  return (
    <div className="mt-2 max-w-xl space-y-2">
      {data.map((attachment) => (
        <FilePreview
          key={attachment.id}
          fileName={attachment.originalFilename}
          fileSize={formatBytes(attachment.fileSize)}
          actions={
            <button
              type="button"
              aria-label={`Download ${attachment.originalFilename}`}
              onClick={() => void download(attachment)}
              className="rounded-lg p-2 text-slate-400 hover:bg-white/10 hover:text-white"
            >
              <Download size={16} />
            </button>
          }
        />
      ))}
    </div>
  );
}
