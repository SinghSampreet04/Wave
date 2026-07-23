import { Plus, Sparkles } from "lucide-react";

interface WorkspaceEmptyStateProps {
  onCreateWorkspace: () => void;
}

export default function WorkspaceEmptyState({
  onCreateWorkspace,
}: WorkspaceEmptyStateProps) {
  return (
    <div className="rounded-2xl border border-dashed border-cyan-400/20 bg-white/[0.02] p-6 text-center">
      <div className="mx-auto flex h-14 w-14 items-center justify-center rounded-2xl bg-cyan-500/10 text-cyan-400">
        <Sparkles size={24} />
      </div>

      <h3 className="mt-5 text-sm font-semibold text-white">
        No workspaces yet
      </h3>

      <p className="mt-2 text-xs leading-6 text-slate-400">
        Create your first workspace and invite your team to start collaborating.
      </p>

      <button
        onClick={onCreateWorkspace}
        className="mt-6 flex w-full items-center justify-center gap-2 rounded-xl bg-gradient-to-r from-cyan-500 to-blue-600 px-4 py-3 text-sm font-semibold text-white transition-all duration-300 hover:scale-[1.02] hover:shadow-lg hover:shadow-cyan-500/20"
      >
        <Plus size={16} />
        Create Workspace
      </button>
    </div>
  );
}