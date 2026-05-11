import { useAuthStore } from "@/hooks/use-auth-store"
import { router } from "expo-router"

export async function apiFetch(input: RequestInfo, init?: RequestInit): Promise<Response> {
  const response = await fetch(input, init)

  if (response.status === 401) {
    useAuthStore.getState().logOut()
    router.replace("/login")
  }

  return response
}
