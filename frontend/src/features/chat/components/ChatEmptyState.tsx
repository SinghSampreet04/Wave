export default function ChatEmptyState() {
  return (
    <div className="flex flex-1 flex-col items-center justify-center px-8 text-center">
      <div className="text-7xl">💬</div>

      <h2 className="mt-6 text-2xl font-bold">
        No messages yet
      </h2>

      <p className="mt-3 max-w-md text-slate-400">
        Be the first person to start the conversation in this
        channel.
      </p>
    </div>
  );
}