import { Hash } from "lucide-react";

export default function ChannelEmptyState() {
  return (
    <div className="rounded-2xl border border-dashed border-white/10 bg-white/[0.02] p-6 text-center">
      <div className="mx-auto flex h-12 w-12 items-center justify-center rounded-xl bg-slate-800 text-slate-400">
        <Hash size={22} />
      </div>

      <h3 className="mt-4 text-sm font-semibold text-white">
        No channels yet
      </h3>

      <p className="mt-2 text-xs leading-6 text-slate-400">
        Create a channel to organize conversations inside this workspace.
      </p>
    </div>
  );
}