import { useEffect } from "react";

import { useWorkspaces } from "../hooks/useWorkspaces";
import { useWorkspaceStore } from "../store/workspaceStore";
import { useWorkspaceUIStore } from "../store/workspaceUIStore";
import WorkspaceEmptyState from "./WorkspaceEmptyState";
import WorkspaceItem from "./WorkspaceItem";
import WorkspaceLoading from "./WorkspaceLoading";

export default function WorkspaceList() {
  const { data, isLoading, isError } = useWorkspaces();

  const activeWorkspace = useWorkspaceStore(
    (state) => state.activeWorkspace
  );

  const setActiveWorkspace = useWorkspaceStore(
    (state) => state.setActiveWorkspace
  );

  const openCreateWorkspaceModal =
    useWorkspaceUIStore(
      (state) => state.openCreateWorkspaceModal
    );

  useEffect(() => {
    if (
      !activeWorkspace &&
      data &&
      data.length > 0
    ) {
      setActiveWorkspace(data[0]);
    }
  }, [activeWorkspace, data, setActiveWorkspace]);

  if (isLoading) {
    return <WorkspaceLoading />;
  }

  if (isError) {
    return (
      <div className="rounded-xl border border-red-500/20 bg-red-500/10 p-4 text-sm text-red-400">
        Unable to load your workspaces.
      </div>
    );
  }

  if (!data || data.length === 0) {
    return (
      <WorkspaceEmptyState
        onCreateWorkspace={openCreateWorkspaceModal}
      />
    );
  }

  return (
    <div className="space-y-2">
      {data.map((workspace) => (
        <WorkspaceItem
          key={workspace.id}
          workspace={workspace}
          active={
            activeWorkspace?.id === workspace.id
          }
          onClick={() =>
            setActiveWorkspace(workspace)
          }
        />
      ))}
    </div>
  );
}