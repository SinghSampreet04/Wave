import { useEffect, useRef, useState } from "react";

interface MessageEditorProps {
  initialValue: string;
  isSaving: boolean;
  onSave: (content: string) => void;
  onCancel: () => void;
}

export default function MessageEditor({
  initialValue,
  isSaving,
  onSave,
  onCancel,
}: MessageEditorProps) {
  const [content, setContent] = useState(initialValue);

  const inputRef =
    useRef<HTMLTextAreaElement>(null);

  useEffect(() => {
    inputRef.current?.focus();

    inputRef.current?.setSelectionRange(
      initialValue.length,
      initialValue.length
    );
  }, [initialValue]);

  const handleSave = () => {
    const trimmed = content.trim();

    if (!trimmed) {
      return;
    }

    if (trimmed === initialValue) {
      onCancel();
      return;
    }

    onSave(trimmed);
  };

  return (
    <div className="mt-2 space-y-3">
      <textarea
        ref={inputRef}
        rows={3}
        value={content}
        disabled={isSaving}
        onChange={(event) =>
          setContent(event.target.value)
        }
        onKeyDown={(event) => {
          if (
            event.key === "Enter" &&
            !event.shiftKey
          ) {
            event.preventDefault();
            handleSave();
          }

          if (event.key === "Escape") {
            event.preventDefault();
            onCancel();
          }
        }}
        className="
          w-full
          resize-none
          rounded-xl
          border
          border-cyan-400/30
          bg-slate-900/70
          p-3
          text-sm
          text-white
          outline-none
          transition
          focus:border-cyan-400
        "
      />

      <div className="flex items-center gap-2">
        <button
          type="button"
          disabled={isSaving}
          onClick={handleSave}
          className="
            rounded-lg
            bg-cyan-500
            px-4
            py-2
            text-sm
            font-medium
            text-white
            transition
            hover:bg-cyan-400
            disabled:opacity-50
          "
        >
          {isSaving
            ? "Saving..."
            : "Save"}
        </button>

        <button
          type="button"
          disabled={isSaving}
          onClick={onCancel}
          className="
            rounded-lg
            border
            border-white/10
            px-4
            py-2
            text-sm
            text-slate-300
            transition
            hover:bg-white/10
          "
        >
          Cancel
        </button>

        <span className="text-xs text-slate-500">
          Enter to save • Shift+Enter for a
          new line • Esc to cancel
        </span>
      </div>
    </div>
  );
}