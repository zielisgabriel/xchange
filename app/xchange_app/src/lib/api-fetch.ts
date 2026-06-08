import { useAuthStore } from "@/hooks/use-auth-store"
import { router } from "expo-router"

interface ApiFetchProps {
  input: RequestInfo;
  init: RequestInit;
  isAuth?: boolean;
}

let isRefreshing = false;
let failedQueue: { resolve: (token: string) => void, reject: (error: any) => void }[] = [];

const processQueue = (error: Error | null, token: string | null = null) => {
  failedQueue.forEach((prom) => {
    if (error) {
      prom.reject(error);
    } else {
      prom.resolve(token as string);
    }
  });
  failedQueue = [];
};

export async function apiFetch({
  input,
  init,
  isAuth = true
}: ApiFetchProps): Promise<Response> {
  const response = await handlerFetch({ input, init, isAuth })

  if (response.status === 401) {
    if (!isRefreshing) {
      isRefreshing = true;

      try {
        const refreshResponse = await fetch("/api/auth/refresh", {
          method: "POST",
          headers: {
            "Content-Type": "application/json"
          },
          body: JSON.stringify({
            "refresh_token": useAuthStore.getState().refreshToken
          })
        });

        if (refreshResponse.ok) {
          const data = await refreshResponse.json();
          const newAccessToken = data?.["access_token"];

          useAuthStore.getState().logIn({
            accessToken: newAccessToken,
            refreshToken: data?.["refresh_token"]
          });

          processQueue(null, newAccessToken);
          isRefreshing = false;

          return await handlerFetch({ input, init, isAuth });
        } else {
          useAuthStore.getState().logOut();
          router.push("/login");
          processQueue(new Error("Failed to refresh token"), null);
          isRefreshing = false;
          return response;
        }
      } catch (error) {
        useAuthStore.getState().logOut();
        router.push("/login");
        processQueue(error as Error, null);
        isRefreshing = false;
        return response;
      }
    } else {
      return new Promise<Response>((resolve, reject) => {
        failedQueue.push({
          resolve: () => {
            resolve(handlerFetch({ input, init, isAuth }));
          },
          reject: (err) => {
            reject(err);
          }
        });
      });
    }
  }

  return response
}

async function handlerFetch({
  input,
  init,
  isAuth
}: ApiFetchProps) {
  return await fetch(input, {
    headers: isAuth ? (
      {
        "Authorization": `Bearer ${useAuthStore.getState().accessToken}`,
        "Content-Type": "application/json;charset=utf-8",
        ...init.headers
      }
    ) : (
      {
        "Content-Type": "application/json;charset=utf-8",
        ...init.headers
      }
    ),
    ...init
  })
}