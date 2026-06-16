import { create } from "zustand"
import { persist, createJSONStorage } from "expo-zustand-persist"
import { getItemAsync, setItemAsync, deleteItemAsync } from "expo-secure-store"
import { queryClient } from "@/lib/query-client"

type UserState = {
  isAuthenticated: boolean;
  onboardingFinished: boolean;
  accessToken: null | string;
  refreshToken: null | string;
  logIn: ({ accessToken, refreshToken }: { accessToken: string, refreshToken: string }) => void;
  logOut: () => void;
  startOnboarding: () => void;
  finishOnboarding: () => void;
}

import { Platform } from "react-native"

const authStorage = {
  getItem: async (key: string) => {
    if (Platform.OS === "web") return localStorage.getItem(key);
    return await getItemAsync(key);
  },
  setItem: async (key: string, value: string) => {
    if (Platform.OS === "web") return localStorage.setItem(key, value);
    return await setItemAsync(key, value);
  },
  removeItem: async (key: string) => {
    if (Platform.OS === "web") return localStorage.removeItem(key);
    return await deleteItemAsync(key);
  }
}

export const useAuthStore = create<UserState>(
  persist(
    (set) => ({
      isAuthenticated: false,
      onboardingFinished: true,
      accessToken: null,
      refreshToken: null,
      logIn: ({ accessToken, refreshToken }) => {
        // Drop any cached profile from a previous session so the onboarding
        // gate is decided by THIS user's fresh `/profile/simple` response.
        queryClient.removeQueries({ queryKey: ["profile-simple"] })
        set((state) => {
          return {
            ...state,
            isAuthenticated: true,
            accessToken,
            refreshToken
          }
        })
      },
      logOut: () => {
        queryClient.clear()
        set((state) => {
          return {
            ...state,
            isAuthenticated: false,
            onboardingFinished: false,
            accessToken: null,
            refreshToken: null
          }
        })
      },
      startOnboarding: () => {
        set((state) => {
          return {
            ...state,
            onboardingFinished: false
          }
        })
      },
      finishOnboarding: () => {
        set((state) => {
          return {
            ...state,
            onboardingFinished: true
          }
        })
      }
    }),
    {
      name: "auth-store",
      storage: createJSONStorage(() => authStorage),
      // `onboardingFinished` is server-derived (set by the profile query), not a
      // cached value. On native the secure-store hydration is async and replaces
      // the whole state when it resolves — which could clobber the value that
      // `startOnboarding()` just set and send a new user to the tabs instead of
      // onboarding. Keep the runtime `onboardingFinished` on hydration.
      merge: (persistedState, currentState) => ({
        ...currentState,
        ...(persistedState as Partial<UserState>),
        onboardingFinished: currentState.onboardingFinished,
      })
    }
  )
)