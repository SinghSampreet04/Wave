import {
  useEffect,
  useRef,
  useState,
  type FormEvent,
  type KeyboardEvent,
} from "react";
import { Paperclip, SendHorizontal, WifiOff, X } from "lucide-react";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import toast from "react-hot-toast";

import Button from "../../../components/ui/Button";
import { useChannelStore } from "../../channel/store/channelStore";
import { websocketService } from "../../../services/websocket/websocketService";
import { useWebSocketStore } from "../../websocket/store/websocketStore";
import { sendMessage, uploadMessageFile } from "../api/chat";
import { useChatStore } from "../store/chatStore";

export default function MessageComposer() {
  const [message, setMessage] = useState("");
  const [file, setFile] = useState<File | null>(null);
  const textareaRef = useRef<HTMLTextAreaElement>(null);
  const typingTimeout = useRef<ReturnType<typeof setTimeout> | null>(null);
  const isTyping = useRef(false);
  const activeChannel = useChannelStore((state) => state.activeChannel);
  const connected = useWebSocketStore((state) => state.connected);
  const addMessage = useChatStore((state) => state.addMessage);
  const queryClient = useQueryClient();
  const sendMutation = useMutation({
    mutationFn: async () => {
      if (!activeChannel) throw new Error("No channel selected.");
      const created = await sendMessage({
        channelId: activeChannel.id,
        content: message.trim() || (file ? `Shared ${file.name}` : ""),
      });
      if (file) await uploadMessageFile(created.id, file);
      return created;
    },
    onSuccess: (created) => {
      addMessage(created);
      setMessage("");
      setFile(null);
      void queryClient.invalidateQueries({
        queryKey: ["message-files", created.id],
      });
      requestAnimationFrame(resize);
    },
    onError: () => toast.error("Unable to send message. Your draft was kept."),
  });

  useEffect(() => {
    return () => {
      if (typingTimeout.current) {
        clearTimeout(typingTimeout.current);
      }
    };
  }, []);

  const resize = () => {
    const textarea = textareaRef.current;
    if (!textarea) return;
    textarea.style.height = "auto";
    textarea.style.height = `${Math.min(textarea.scrollHeight, 160)}px`;
  };

  const stopTyping = () => {
    if (!activeChannel || !connected || !isTyping.current) return;
    websocketService.sendTyping({
      channelId: activeChannel.id,
      typing: false,
    });
    isTyping.current = false;
  };

  const send = () => {
    if (!activeChannel || !connected || sendMutation.isPending) return;
    const trimmed = message.trim();
    if (!trimmed && !file) return;

    stopTyping();
    sendMutation.mutate();
  };

  const handleSubmit = (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    send();
  };

  const handleKeyDown = (event: KeyboardEvent<HTMLTextAreaElement>) => {
    if (event.key === "Enter" && !event.shiftKey) {
      event.preventDefault();
      send();
    }
  };

  const handleChange = (value: string) => {
    setMessage(value);
    requestAnimationFrame(resize);

    if (!activeChannel || !connected) return;
    if (!isTyping.current) {
      websocketService.sendTyping({
        channelId: activeChannel.id,
        typing: true,
      });
      isTyping.current = true;
    }
    if (typingTimeout.current) clearTimeout(typingTimeout.current);
    typingTimeout.current = setTimeout(stopTyping, 2000);
  };

  const canSend =
    !!activeChannel &&
    connected &&
    !sendMutation.isPending &&
    (message.trim().length > 0 || !!file);

  return (
    <form
      onSubmit={handleSubmit}
      className="border-t border-white/10 bg-slate-950/60 px-4 py-4 backdrop-blur-xl sm:px-6"
    >
      <div className="flex items-end gap-3 rounded-2xl border border-white/10 bg-slate-900/95 p-3 shadow-inner">
        <label
          className="flex h-11 w-11 shrink-0 cursor-pointer items-center justify-center rounded-xl border border-white/10 bg-slate-800 text-slate-300 hover:text-white"
          aria-label="Attach file"
        >
          <Paperclip size={18} />
          <input
            type="file"
            className="sr-only"
            onChange={(event) => {
              const selected = event.target.files?.[0] ?? null;
              if (selected && selected.size > 25 * 1024 * 1024) {
                toast.error("Files must be 25 MB or smaller.");
                return;
              }
              setFile(selected);
            }}
          />
        </label>
        <textarea
          ref={textareaRef}
          id="message"
          rows={1}
          maxLength={4000}
          value={message}
          disabled={!activeChannel}
          aria-label="Message"
          placeholder={
            activeChannel
              ? `Message #${activeChannel.name}…`
              : "Select a channel…"
          }
          onChange={(event) => handleChange(event.target.value)}
          onKeyDown={handleKeyDown}
          className="min-h-11 min-w-0 flex-1 resize-none overflow-y-auto rounded-xl border border-white/10 bg-slate-800 px-4 py-3 text-[15px] leading-5 text-white outline-none placeholder:text-slate-500 focus:border-cyan-400/50 focus:ring-2 focus:ring-cyan-400/20 disabled:opacity-50"
        />

        {!connected && activeChannel && (
          <div className="hidden shrink-0 items-center gap-2 rounded-xl border border-amber-500/20 bg-amber-500/10 px-3 py-2 text-xs text-amber-200 lg:flex">
            <WifiOff size={15} />
            Draft saved
          </div>
        )}

        <Button
          type="submit"
          fullWidth={false}
          disabled={!canSend}
          className="h-11 shrink-0 px-5"
        >
          <SendHorizontal size={18} />
          <span className="hidden sm:inline">Send</span>
        </Button>
      </div>
      {file && (
        <div className="mt-2 flex items-center gap-2 px-1 text-xs text-cyan-200">
          <Paperclip size={13} />
          <span className="max-w-xs truncate">{file.name}</span>
          <button type="button" onClick={() => setFile(null)} aria-label="Remove attachment">
            <X size={13} />
          </button>
        </div>
      )}
      <p className="mt-2 px-1 text-[11px] text-slate-500">
        Enter to send · Shift+Enter for a new line
      </p>
    </form>
  );
}
