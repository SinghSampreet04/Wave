import { useRef, useState } from "react";
import { Pin, X } from "lucide-react";
import { useQuery, useQueryClient } from "@tanstack/react-query";
import toast from "react-hot-toast";

import { getPinnedMessages, unpinMessage } from "../api/chat";
import useClickOutside from "../../../hooks/useClickOutside";

export default function PinnedMessages({
  channelId,
}: {
  channelId: number;
}) {
  const [open, setOpen] = useState(false);
  const ref = useRef<HTMLDivElement>(null);
  const queryClient = useQueryClient();
  useClickOutside(ref, () => setOpen(false));
  const { data = [] } = useQuery({
    queryKey: ["pinned-messages", channelId],
    queryFn: () => getPinnedMessages(channelId),
  });

  return (
    <div ref={ref} className="relative">
      <button
        type="button"
        onClick={() => setOpen((current) => !current)}
        className="flex items-center gap-2 rounded-xl border border-white/10 bg-slate-900 px-3 py-2 text-sm text-slate-300"
      >
        <Pin size={15} />
        {data.length} pinned
      </button>
      {open && (
        <div className="absolute right-0 top-12 z-[90] max-h-80 w-80 overflow-y-auto rounded-2xl border border-white/15 bg-slate-950 p-2 shadow-2xl">
          {data.length === 0 ? (
            <p className="p-5 text-center text-sm text-slate-400">
              No pinned messages.
            </p>
          ) : (
            data.map((pin) => (
              <div key={pin.pinId} className="flex gap-2 rounded-xl p-3 hover:bg-white/5">
                <button
                  type="button"
                  onClick={() => {
                    document
                      .getElementById(`message-${pin.messageId}`)
                      ?.scrollIntoView({ behavior: "smooth", block: "center" });
                    setOpen(false);
                  }}
                  className="min-w-0 flex-1 text-left"
                >
                  <p className="truncate text-sm text-slate-200">
                    {pin.messageContent}
                  </p>
                  <p className="mt-1 text-[11px] text-slate-500">
                    Pinned by {pin.pinnedByUsername}
                  </p>
                </button>
                <button
                  type="button"
                  aria-label="Unpin message"
                  onClick={() => {
                    void unpinMessage(pin.messageId)
                      .then(() => queryClient.invalidateQueries({
                        queryKey: ["pinned-messages", channelId],
                      }))
                      .catch(() => toast.error("Unable to unpin message."));
                  }}
                >
                  <X size={14} className="text-slate-500" />
                </button>
              </div>
            ))
          )}
        </div>
      )}
    </div>
  );
}
