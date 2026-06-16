import { useAuthStore } from "./use-auth-store";
import { apiFetch } from "@/lib/api-fetch";
import { ProfileSimple } from "@/types/profile-simple";
import { useQuery } from "@tanstack/react-query";
import { useEffect } from "react";

export function useProfileSimple() {
  const { accessToken, isAuthenticated, startOnboarding, onboardingFinished } = useAuthStore()

  const result = useQuery<ProfileSimple>({
    queryKey: ["profile-simple"],
    queryFn: async () => {
      const response = await apiFetch({
        input: "/api/profile/simple",
        init: {
          method: "GET"
        }
      })

      if (!response.ok) throw new Error("Falha ao buscar perfil")

      return response.json() as Promise<ProfileSimple>
    },
    enabled: isAuthenticated && !!accessToken,
    // Onboarding status is derived from this response, so it must never be
    // served stale from cache — always refetch fresh for the current user.
    staleTime: 0,
    refetchOnMount: "always",
    retry: false,
  })

  useEffect(() => {
    if (!result.isSuccess || !result.data) return;

    if (!result.data.onboardingFinished && onboardingFinished) {
      useAuthStore.getState().startOnboarding();
      return;
    }
    
    if (result.data.onboardingFinished && !onboardingFinished) {
      useAuthStore.getState().finishOnboarding();
    }
  }, [result.isSuccess, result.data?.onboardingFinished, onboardingFinished]);

  return result
}