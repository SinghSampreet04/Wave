interface EmojiPickerProps {
  onSelect: (emoji: string) => void;
}

const EMOJIS = [
  "😀",
  "😂",
  "❤️",
  "👍",
  "🎉",
  "🔥",
  "👏",
  "😮",
  "😢",
  "🤔",
  "👀",
  "🚀",
];

export default function EmojiPicker({
  onSelect,
}: EmojiPickerProps) {
  return (
    <div
      className="
        absolute
        right-0
        top-12
        z-50
        w-64
        rounded-2xl
        border
        border-white/10
        bg-slate-900/95
        p-3
        shadow-2xl
        backdrop-blur-xl
      "
    >
      <div className="grid grid-cols-6 gap-2">
        {EMOJIS.map((emoji) => (
          <button
            key={emoji}
            type="button"
            onClick={() => onSelect(emoji)}
            className="
              flex
              h-10
              w-10
              items-center
              justify-center
              rounded-xl
              text-xl
              transition-all
              duration-200
              hover:scale-110
              hover:bg-cyan-500/20
              active:scale-95
            "
          >
            {emoji}
          </button>
        ))}
      </div>
    </div>
  );
}