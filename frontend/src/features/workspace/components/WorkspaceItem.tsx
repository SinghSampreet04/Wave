import type { Workspace } from "../types/workspace";
import { Trash2 } from "lucide-react";

interface WorkspaceItemProps {
  workspace: Workspace;
  active?: boolean;
  onClick?: () => void;
  canDelete?: boolean;
  isDeleting?: boolean;
  onDelete?: () => void;
}

export default function WorkspaceItem({
  workspace,
  active = false,
  onClick,
  canDelete = false,
  isDeleting = false,
  onDelete,
}: WorkspaceItemProps) {
  const initials = workspace.name
    .split(" ")
    .map((word) => word.charAt(0))
    .join("")
    .slice(0, 2)
    .toUpperCase();

  return (
    <div
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

      <button
        type="button"
        onClick={onClick}
        className="flex w-full items-center gap-4 p-4 text-left"
      >
        <div
          className={[
            "flex h-12 w-12 flex-shrink-0 items-center justify-center rounded-2xl text-sm font-bold text-white shadow-lg transition-transform duration-300",
            active
              ? "bg-gradient-to-br from-cyan-500 to-blue-600"
              : "bg-gradient-to-br from-slate-600 to-slate-700 group-hover:scale-105",
          ].join(" ")}
        >
          {initials}
        </div>

        <div className="min-w-0 flex-1">
          <p
            className={[
              "truncate font-semibold transition-colors",
              active
                ? "text-white"
                : "text-slate-200 group-hover:text-white",
            ].join(" ")}
          >
            {workspace.name}
          </p>

          <p className="mt-1 truncate text-xs text-slate-400">
            {active
              ? "Active Workspace"
              : `Owner • ${workspace.ownerUsername}`}
          </p>
        </div>
      </button>

      {canDelete && (
        <button
          type="button"
          aria-label={`Delete workspace ${workspace.name}`}
          title="Delete workspace"
          disabled={isDeleting}
          onClick={onDelete}
          className="absolute right-2 top-2 z-10 rounded-lg p-2 text-slate-500 opacity-0 transition hover:bg-red-500/10 hover:text-red-300 focus:opacity-100 disabled:opacity-40 group-hover:opacity-100"
        >
          <Trash2 size={15} />
        </button>
      )}
    </div>
  );
}
