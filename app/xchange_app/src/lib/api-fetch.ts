import { useAuthStore } from "@/hooks/use-auth-store"
import { router } from "expo-router"

interface ApiFetchProps {
  input: RequestInfo;
  init: RequestInit;
  isAuth?: boolean;
}

export async function apiFetch({
  input,
  init,
  isAuth = true
}: ApiFetchProps): Promise<Response> {
  const response = await handlerFetch({ input, init, isAuth })

  if (response.status === 401) {
    const response = await fetch("/api/auth/refresh", {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify({
        "refresh_token": useAuthStore.getState().refreshToken
      })
    })

    if (response.ok) {
      const data = await response.json()

      useAuthStore.getState().logIn({
        accessToken: data?.["access_token"],
        refreshToken: data?.["refresh_token"]
      })

      return await handlerFetch({ input, init, isAuth })
    }

    useAuthStore.getState().logOut()
    router.push("/login")
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