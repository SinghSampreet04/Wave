import { Hash, Lock } from "lucide-react";

import type { Channel } from "../types/channel";

interface ChannelItemProps {
  channel: Channel;
  active: boolean;
  onClick: () => void;
}

export default function ChannelItem({
  channel,
  active,
  onClick,
}: ChannelItemProps) {
  const Icon = channel.isPrivate ? Lock : Hash;

  return (
    <button
      type="button"
      aria-pressed={active}
      onClick={onClick}
      className={[
        "group relative w-full overflow-hidden rounded-2xl border text-left transition-all duration-300",
        active
          ? "border-cyan-400/30 bg-gradient-to-r from-cyan-500/15 to-blue-500/10 shadow-lg shadow-cyan-500/10"
          : "border-transparent bg-white/[0.02] hover:border-white/10 hover:bg-white/[0.05]",
      ].join(" ")}
    >
      {active && (
        <div className="absolute inset-y-3 left-0 w-1 rounded-r-full bg-cyan-400" />
      )}

      <div className="flex items-center gap-4 p-4">
        <div
          className={[
            "flex h-10 w-10 flex-shrink-0 items-center justify-center rounded-xl transition-all duration-300",
            active
              ? "bg-gradient-to-br from-cyan-500 to-blue-600 text-white shadow-lg shadow-cyan-500/20"
              : "bg-slate-800 text-slate-400 group-hover:bg-slate-700 group-hover:text-white",
          ].join(" ")}
        >
          <Icon size={18} />
        </div>

        <div className="min-w-0 flex-1">
          <p
            className={[
              "truncate font-medium transition-colors",
              active
                ? "text-white"
                : "text-slate-200 group-hover:text-white",
            ].join(" ")}
          >
            {channel.name}
          </p>

          <p className="mt-1 text-xs text-slate-400">
            {channel.isPrivate
              ? "Private Channel"
              : "Public Channel"}
          </p>
        </div>
      </div>
    </button>
  );
}