import {
  Copy,
  MessageSquare,
  MoreHorizontal,
  Pencil,
  Pin,
  SmilePlus,
  Trash2,
} from "lucide-react";

import { IconButton } from "../../../components/ui";
import { ui } from "../../../shared/styles/ui";

interface MessageActionsProps {
  canManageMessage: boolean;

  onReaction: () => void;

  onReply: () => void;

  onCopy: () => void;

  onEdit: () => void;

  onDelete: () => void;

  onPin: () => void;

  onMore: () => void;
}

export default function MessageActions({
  canManageMessage,
  onReaction,
  onReply,
  onCopy,
  onEdit,
  onDelete,
  onPin,
  onMore,
}: MessageActionsProps) {
  return (
    <div
      className={`
        ${ui.floating}
        ${ui.fade}
        absolute
        -top-4
        right-0
        z-20
        flex
        items-center
        gap-1
        p-1
        opacity-0
        group-hover:opacity-100
      `}
    >
      <IconButton
        icon={<SmilePlus size={16} />}
        onClick={onReaction}
        aria-label="Add reaction"
        title="Add reaction"
      />

      <IconButton
        icon={<MessageSquare size={16} />}
        onClick={onReply}
        aria-label="Reply in thread"
        title="Reply in thread"
      />

      <IconButton
        icon={<Pin size={16} />}
        onClick={onPin}
        aria-label="Pin message"
        title="Pin message"
      />

      <IconButton
        icon={<Copy size={16} />}
        onClick={onCopy}
        aria-label="Copy message"
        title="Copy message"
      />

      {canManageMessage && (
        <>
          <IconButton
            icon={<Pencil size={16} />}
            onClick={onEdit}
            aria-label="Edit message"
            title="Edit message"
          />

          <IconButton
            icon={<Trash2 size={16} />}
            variant="danger"
            onClick={onDelete}
            aria-label="Delete message"
            title="Delete message"
          />
        </>
      )}

      <IconButton
        icon={<MoreHorizontal size={16} />}
        onClick={onMore}
        aria-label="More message actions"
        title="More message actions"
      />
    </div>
  );
}
