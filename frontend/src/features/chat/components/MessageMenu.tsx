import {
  Copy,
  Pencil,
  SmilePlus,
  Trash2,
  Pin,
} from "lucide-react";

import { ui } from "../../../shared/styles/ui";

interface MessageMenuProps {
  canManageMessage: boolean;
  onReact: () => void;
  onCopy: () => void;
  onEdit: () => void;
  onDelete: () => void;
  onPin: () => void;
}

export default function MessageMenu({
  canManageMessage,
  onReact,
  onCopy,
  onEdit,
  onDelete,
  onPin,
}: MessageMenuProps) {
  return (
    <div
      className={`
        ${ui.menu}
        absolute
        right-0
        top-10
        z-50
        min-w-[220px]
        overflow-hidden
        p-1
      `}
    >
      <button
        type="button"
        onClick={onReact}
        className={ui.menuItem}
      >
        <SmilePlus size={16} />
        <span>Add Reaction</span>
      </button>

      <button type="button" onClick={onPin} className={ui.menuItem}>
        <Pin size={16} />
        <span>Pin Message</span>
      </button>

      <button
        type="button"
        onClick={onCopy}
        className={ui.menuItem}
      >
        <Copy size={16} />
        <span>Copy Message</span>
      </button>

      {canManageMessage && (
        <>
          <button
            type="button"
            onClick={onEdit}
            className={ui.menuItem}
          >
            <Pencil size={16} />
            <span>Edit Message</span>
          </button>

          <button
            type="button"
            onClick={onDelete}
            className={`
              ${ui.menuItem}
              text-red-400
              hover:bg-red-500/10
            `}
          >
            <Trash2 size={16} />
            <span>Delete Message</span>
          </button>
        </>
      )}
    </div>
  );
}
