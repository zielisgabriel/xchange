import { Minute } from "@/valueobject/Minute";
import { QueryClient } from "@tanstack/react-query";

export const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      refetchIntervalInBackground: false,
      refetchOnWindowFocus: false,
      refetchInterval: new Minute(2).toMilliseconds()
    }
  }
})