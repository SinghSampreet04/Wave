import type { ChatMessage } from "../types/chat";

interface MessageHeaderProps {
  message: ChatMessage;
  isGrouped: boolean;
}

export default function MessageHeader({
  message,
  isGrouped,
}: MessageHeaderProps) {
  if (isGrouped) {
    return null;
  }

  const date = new Date(message.createdAt);

  const formattedTime = date.toLocaleTimeString([], {
    hour: "numeric",
    minute: "2-digit",
  });

  return (
    <div className="mb-2 flex flex-wrap items-center gap-2">
      <span className="text-sm font-semibold text-white">
        {message.senderUsername}
      </span>

      <span className="text-xs font-medium text-slate-500">
        {formattedTime}
      </span>

      {message.edited && (
        <span className="rounded-full border border-white/10 bg-white/5 px-2 py-0.5 text-[10px] font-medium uppercase tracking-wide text-slate-400">
          Edited
        </span>
      )}
    </div>
  );
}