import { useAuthStore } from "./use-auth-store";
import { ProfileSimple } from "@/types/profile-simple";
import { Minute } from "@/valueobject/Minute";
import { useQuery } from "@tanstack/react-query";

export function useProfileSimple() {
  const { token, isAuthenticated } = useAuthStore()

  return useQuery<ProfileSimple>({
    queryKey: ["profile-simple"],
    queryFn: async () => {
      const response = await fetch("/api/profile/simple", {
        method: "GET",
        headers: {
          "Authorization": `Bearer ${token}`,
          "Content-Type": "application/json"
        }
      })

      if (!response.ok) throw new Error("Falha ao buscar perfil")

      return response.json() as Promise<ProfileSimple>
    },
    enabled: isAuthenticated && !!token,
    staleTime: new Minute(10).toMilliseconds(),
    retry: true,
  })
}