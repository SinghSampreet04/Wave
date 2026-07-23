import type { ChatMessage } from "../types/chat";

interface MessageAvatarProps {
  message: ChatMessage;
  isGrouped: boolean;
}

const COLORS = [
  "bg-red-500",
  "bg-orange-500",
  "bg-amber-500",
  "bg-yellow-500",
  "bg-lime-500",
  "bg-green-500",
  "bg-emerald-500",
  "bg-teal-500",
  "bg-cyan-500",
  "bg-sky-500",
  "bg-blue-500",
  "bg-indigo-500",
  "bg-violet-500",
  "bg-purple-500",
  "bg-pink-500",
];

function getAvatarColor(username: string) {
  let hash = 0;

  for (let i = 0; i < username.length; i++) {
    hash = username.charCodeAt(i) + ((hash << 5) - hash);
  }

  return COLORS[Math.abs(hash) % COLORS.length];
}

export default function MessageAvatar({
  message,
  isGrouped,
}: MessageAvatarProps) {
  if (isGrouped) {
    return <div className="w-12 shrink-0" />;
  }

  const initial = message.senderUsername
    .charAt(0)
    .toUpperCase();

  return (
    <div className="w-12 shrink-0">
      <div
        className={`
          flex
          h-10
          w-10
          items-center
          justify-center
          rounded-full
          font-semibold
          text-white
          select-none
          ${getAvatarColor(message.senderUsername)}
        `}
      >
        {initial}
      </div>
    </div>
  );
}