export default function ChannelLoading() {
  return (
    <div className="space-y-3">
      {Array.from({ length: 4 }).map((_, index) => (
        <div
          key={index}
          className="animate-pulse rounded-2xl border border-white/5 bg-white/[0.02] p-4"
        >
          <div className="flex items-center gap-4">
            <div className="h-10 w-10 rounded-xl bg-slate-800" />

            <div className="flex-1">
              <div className="h-4 w-24 rounded bg-slate-700" />
              <div className="mt-2 h-3 w-16 rounded bg-slate-800" />
            </div>
          </div>
        </div>
      ))}
    </div>
  );
}