import { useMemo } from "react";
import { useInfiniteQuery } from "@tanstack/react-query";

import { getChannelMessageHistory } from "../api/chat";

export function useMessages(
  channelId?: number
) {
  const query = useInfiniteQuery({
    queryKey: [
      "messages",
      channelId,
    ],

    queryFn: ({ pageParam }) =>
      getChannelMessageHistory(channelId!, pageParam),

    enabled: !!channelId,
    initialPageParam: undefined as number | undefined,
    getNextPageParam: (lastPage) =>
      lastPage.hasMore
        ? lastPage.nextCursor ?? undefined
        : undefined,
  });

  const messages = useMemo(
    () =>
      query.data
        ? [...query.data.pages]
            .reverse()
            .flatMap((page) => page.items)
        : undefined,
    [query.data]
  );

  return {
    ...query,
    data: messages,
  };
}
