import { useThreadStore } from "../store/threadStore";

import ChatHeader from "./ChatHeader";
import MessageComposer from "./MessageComposer";
import MessageList from "./MessageList";
import ThreadPanel from "./ThreadPanel";
import TypingIndicator from "./TypingIndicator";

export default function ChatWindow() {
  const isOpen = useThreadStore(
    (state) => state.isOpen
  );

  return (
    <div className="flex min-h-0 flex-1 overflow-hidden rounded-3xl border border-white/10 bg-white/5 backdrop-blur-xl">
      <div className="flex min-w-0 flex-1 flex-col">
        <ChatHeader compact={isOpen} />

        <div className="flex min-h-0 flex-1 flex-col">
          <MessageList />

          <TypingIndicator />

          <MessageComposer />
        </div>
      </div>

      {isOpen && <ThreadPanel />}
    </div>
  );
}
