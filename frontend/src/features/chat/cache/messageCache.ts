import type {
  InfiniteData,
  QueryClient,
} from "@tanstack/react-query";

import type { MessageHistoryPage } from "../api/chat";
import type {
  ChatMessage,
  Reaction,
} from "../types/chat";

export function mergeReaction(
  reactions: Reaction[] | undefined,
  incoming: Reaction
): Reaction[] {
  const next = [...(reactions ?? [])];
  const index = next.findIndex(
    (reaction) =>
      reaction.emoji === incoming.emoji
  );

  if (incoming.count <= 0) {
    if (index !== -1) {
      next.splice(index, 1);
    }

    return next;
  }

  if (index === -1) {
    next.push(incoming);
  } else {
    next[index] = incoming;
  }

  return next;
}

function updateMessageReaction(
  message: ChatMessage,
  reaction: Reaction
): ChatMessage {
  if (message.id !== reaction.messageId) {
    return message;
  }

  return {
    ...message,
    reactions: mergeReaction(
      message.reactions,
      reaction
    ),
  };
}

export function updateReactionInMessageCache(
  queryClient: QueryClient,
  reaction: Reaction
) {
  queryClient.setQueriesData<
    InfiniteData<MessageHistoryPage>
  >(
    {
      queryKey: ["messages"],
    },
    (data) => {
      if (!data) {
        return data;
      }

      let changed = false;
      const pages = data.pages.map(
        (page) => {
          const items = page.items.map(
            (message) => {
              const updated =
                updateMessageReaction(
                  message,
                  reaction
                );

              if (updated !== message) {
                changed = true;
              }

              return updated;
            }
          );

          return changed
            ? {
                ...page,
                items,
              }
            : page;
        }
      );

      return changed
        ? {
            ...data,
            pages,
          }
        : data;
    }
  );
}
