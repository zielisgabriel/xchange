import { create } from "zustand"
import { persist, createJSONStorage } from "expo-zustand-persist"
import { getItemAsync, setItemAsync, deleteItemAsync } from "expo-secure-store"

type UserState = {
  isAuthenticated: boolean;
  profileSimple: ProfileSimple | null;
  setProfileSimple: (profileSimple: ProfileSimple) => void;
  token: null | string;
  logIn: (token: string) => void;
  logOut: () => void;
}

import { Platform } from "react-native"
import { ProfileSimple } from "@/types/profile-simple";

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
      profileSimple: null,
      setProfileSimple: (profileSimple) => {
        set((state) => {
          return {
            ...state,
            profileSimple
          }
        })
      },
      token: null,
      logIn: (token) => {
        set((state) => {
          return {
            ...state,
            isAuthenticated: true,
            token
          }
        })
      },
      logOut: () => {
        set((state) => {
          return {
            ...state,
            isAuthenticated: false,
            token: null
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