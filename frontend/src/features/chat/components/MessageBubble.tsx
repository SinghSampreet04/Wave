import { useRef } from "react";
import toast from "react-hot-toast";

import type { ChatMessage } from "../types/chat";

import useClickOutside from "../../../hooks/useClickOutside";

import { useAuthStore } from "../../auth/store/authStore";
import { useChatStore } from "../store/chatStore";
import { useChatUIStore } from "../store/chatUIStore";
import { useThreadStore } from "../store/threadStore";

import { useEditMessage } from "../hooks/useEditMessage";
import { useDeleteMessage } from "../hooks/useDeleteMessage";
import { useToggleReaction } from "../hooks/useToggleReaction";
import { pinMessage } from "../api/chat";
import { useQueryClient } from "@tanstack/react-query";

import EmojiPicker from "./EmojiPicker";
import MessageActions from "./MessageActions";
import { Avatar } from "../../../components/ui";
import MessageContent from "./MessageContent";
import MessageEditor from "./MessageEditor";
import MessageHeader from "./MessageHeader";
import MessageMenu from "./MessageMenu";
import MessageAttachments from "./MessageAttachments";

interface MessageBubbleProps {
  message: ChatMessage;
  isGrouped: boolean;
}

export default function MessageBubble({
  message,
  isGrouped,
}: MessageBubbleProps) {
  const menuRef = useRef<HTMLDivElement>(null);

  const currentUser = useAuthStore(
    (state) => state.currentUser
  );
  const queryClient = useQueryClient();

  const updateMessage = useChatStore(
    (state) => state.updateMessage
  );

  const openThread = useThreadStore(
    (state) => state.openThread
  );

  const {
    activeMenuId,
    activeEmojiPickerId,
    editingMessageId,
    setActiveMenu,
    setActiveEmojiPicker,
    setEditingMessage,
    closeAll,
  } = useChatUIStore();

  const {
    mutate: editMessage,
    isPending: isEditingMessage,
  } = useEditMessage();

  const {
    mutate: deleteMessage,
  } = useDeleteMessage();

  const {
    mutate: toggleReaction,
  } = useToggleReaction();

  const showMenu =
    activeMenuId === message.id;

  const showEmojiPicker =
    activeEmojiPickerId === message.id;

  const isEditing =
    editingMessageId === message.id;

  const canManageMessage =
    currentUser?.id === message.senderId;

  useClickOutside(menuRef, closeAll);

  const handleCopy = async () => {
    closeAll();

    if (message.deleted) {
      toast.error(
        "Deleted messages cannot be copied."
      );
      return;
    }

    try {
      await navigator.clipboard.writeText(
        message.content
      );

      toast.success(
        "Message copied to clipboard."
      );
    } catch {
      toast.error(
        "Unable to copy message."
      );
    }
  };

  const handleReaction = (
    emoji: string
  ) => {
    closeAll();

    toggleReaction(
      {
        messageId: message.id,
        emoji,
      }
    );
  };

  const handleReply = () => {
    closeAll();

    openThread(message);
  };

  const openReactionPicker = () => {
    setActiveEmojiPicker(message.id);
  };

  const toggleMenu = () => {
    if (showMenu) {
      closeAll();
    } else {
      setActiveMenu(message.id);
    }
  };

  const handleSave = (
    content: string
  ) => {
    editMessage(
      {
        messageId: message.id,
        content,
      },
      {
        onSuccess: (
          updatedMessage
        ) => {
          updateMessage(
            updatedMessage
          );

          setEditingMessage(
            null
          );
        },
      }
    );
  };

  const handleDelete = () => {
    closeAll();

    if (message.deleted) {
      return;
    }

    deleteMessage(message.id);
  };

  const handlePin = () => {
    closeAll();
    void pinMessage(message.id)
      .then(() => {
        void queryClient.invalidateQueries({
          queryKey: [
            "pinned-messages",
            message.channelId,
          ],
        });
        toast.success("Message pinned.");
      })
      .catch(() =>
        toast.error("Unable to pin message.")
      );
  };

  return (
    <div
      ref={menuRef}
      id={`message-${message.id}`}
      tabIndex={-1}
      className={`
        group
        relative
        flex
        gap-3
        px-5
        transition-colors
        duration-150
        hover:bg-white/5
        focus:bg-cyan-500/10
        focus:outline-none
        ${
          isGrouped
            ? "py-1"
            : "pt-4 pb-1"
        }
      `}
    >
      <div className="w-10 flex-shrink-0">
        {!isGrouped && (
          <Avatar
            name={
              message.senderUsername
            }
            size="md"
          />
        )}
      </div>

      <div className="relative min-w-0 flex-1">
        <MessageActions
          canManageMessage={
            canManageMessage
          }
          onReaction={
            openReactionPicker
          }
          onReply={
            handleReply
          }
          onCopy={handleCopy}
          onEdit={() =>
            setEditingMessage(
              message.id
            )
          }
          onDelete={
            handleDelete
          }
          onPin={handlePin}
          onMore={toggleMenu}
        />

        <MessageHeader
          message={message}
          isGrouped={isGrouped}
        />

        {isEditing ? (
          <MessageEditor
            initialValue={
              message.content
            }
            isSaving={
              isEditingMessage
            }
            onSave={
              handleSave
            }
            onCancel={() =>
              setEditingMessage(
                null
              )
            }
          />
        ) : (
          <>
            <MessageContent
              message={message}
            />
            <MessageAttachments messageId={message.id} />

            {message.reactions.length >
              0 && (
              <div className="mt-2 flex flex-wrap gap-2">
                {message.reactions.map(
                  (
                    reaction
                  ) => (
                    <button
                      key={
                        reaction.emoji
                      }
                      type="button"
                      onClick={() =>
                        handleReaction(
                          reaction.emoji
                        )
                      }
                      className={`
                        flex
                        items-center
                        gap-1
                        rounded-full
                        border
                        px-2.5
                        py-1
                        text-sm
                        transition-colors
                        ${
                          reaction.reactedByCurrentUser
                            ? "border-cyan-400 bg-cyan-500/20 text-cyan-200"
                            : "border-white/10 bg-white/5 text-gray-300 hover:bg-white/10"
                        }
                      `}
                    >
                      <span>
                        {
                          reaction.emoji
                        }
                      </span>

                      <span className="font-medium">
                        {
                          reaction.count
                        }
                      </span>
                    </button>
                  )
                )}
              </div>
            )}
          </>
        )}
      </div>

      <div className="relative">
        {showMenu && (
          <MessageMenu
            canManageMessage={
              canManageMessage
            }
            onReact={
              openReactionPicker
            }
            onCopy={
              handleCopy
            }
            onEdit={() => {
              closeAll();

              setEditingMessage(
                message.id
              );
            }}
            onDelete={
              handleDelete
            }
            onPin={handlePin}
          />
        )}

        {showEmojiPicker && (
          <EmojiPicker
            onSelect={
              handleReaction
            }
          />
        )}
      </div>
    </div>
  );
}
