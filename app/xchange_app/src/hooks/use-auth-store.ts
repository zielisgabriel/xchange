import { create } from "zustand"
import { persist, createJSONStorage } from "expo-zustand-persist"
import { getItemAsync, setItemAsync, deleteItemAsync } from "expo-secure-store"

type UserState = {
  isAuthenticated: boolean;
  accessToken: null | string;
  refreshToken: null | string;
  logIn: ({ accessToken, refreshToken }: { accessToken: string, refreshToken: string }) => void;
  logOut: () => void;
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
      accessToken: null,
      refreshToken: null,
      logIn: ({ accessToken, refreshToken }) => {
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
        set((state) => {
          return {
            ...state,
            isAuthenticated: false,
            accessToken: null,
            refreshToken: null
          }
        })
      }
    }),
    {
      name: "auth-store",
      storage: createJSONStorage(() => authStorage)
    }
  )
)