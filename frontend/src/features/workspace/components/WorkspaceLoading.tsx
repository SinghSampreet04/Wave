export default function WorkspaceLoading() {
  return (
    <div className="space-y-3">
      {Array.from({ length: 4 }).map((_, index) => (
        <div
          key={index}
          className="animate-pulse rounded-2xl border border-white/5 bg-white/[0.02] p-4"
        >
          <div className="flex items-center gap-4">
            <div className="h-12 w-12 rounded-2xl bg-slate-800" />

            <div className="flex-1">
              <div className="h-4 w-32 rounded bg-slate-700" />
              <div className="mt-3 h-3 w-20 rounded bg-slate-800" />
            </div>
          </div>
        </div>
      ))}
    </div>
  );
}