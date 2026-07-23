import { useMemo } from "react";

import { useAuthStore } from "../../auth/store/authStore";
import { useChatStore } from "../store/chatStore";

export default function TypingIndicator() {
  const currentUser = useAuthStore(
    (state) => state.currentUser
  );

  const typingUsers = useChatStore(
    (state) => state.typingUsers
  );

  const otherTypingUsers = useMemo(
    () =>
      typingUsers.filter(
        (user) =>
          user.userId !== currentUser?.id
      ),
    [typingUsers, currentUser]
  );

  if (otherTypingUsers.length === 0) {
    return <div className="h-6 px-6" />;
  }

  const text =
    otherTypingUsers.length === 1
      ? `${otherTypingUsers[0].username} is typing...`
      : otherTypingUsers.length === 2
      ? `${otherTypingUsers[0].username} and ${otherTypingUsers[1].username} are typing...`
      : `${otherTypingUsers.length} people are typing...`;

  return (
    <div className="h-6 px-6 text-sm italic text-cyan-400">
      {text}
    </div>
  );
}