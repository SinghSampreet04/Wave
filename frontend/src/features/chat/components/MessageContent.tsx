import type { ChatMessage } from "../types/chat";

interface MessageContentProps {
  message: ChatMessage;
}

export default function MessageContent({
  message,
}: MessageContentProps) {
  if (message.deleted) {
    return (
      <div className="rounded-2xl border border-dashed border-slate-700 bg-slate-900/30 px-4 py-3">
        <p className="italic text-slate-500">
          This message was deleted.
        </p>
      </div>
    );
  }

  const parts = message.content.split(/(@[A-Za-z0-9_]{2,30})/g);

  return (
    <div className="inline-block max-w-full rounded-2xl border border-white/5 bg-white/[0.03] px-4 py-3 transition-colors duration-200 group-hover:bg-white/[0.05]">
      <p className="whitespace-pre-wrap break-words text-[15px] leading-7 text-slate-200">
        {parts.map((part, index) =>
          part.startsWith("@") ? (
            <span
              key={`${part}-${index}`}
              className="rounded bg-cyan-500/15 px-1 font-semibold text-cyan-200"
            >
              {part}
            </span>
          ) : (
            part
          )
        )}
      </p>
    </div>
  );
}
