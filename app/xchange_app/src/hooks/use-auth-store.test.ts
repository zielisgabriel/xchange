jest.mock("expo-secure-store", () => ({
  getItemAsync: jest.fn(async () => null),
  setItemAsync: jest.fn(async () => undefined),
  deleteItemAsync: jest.fn(async () => undefined),
}))

import { useAuthStore } from "./use-auth-store"

describe("useAuthStore", () => {
  beforeEach(() => {
    useAuthStore.setState({
      isAuthenticated: false,
      onboardingFinished: true,
      accessToken: null,
      refreshToken: null,
    })
  })

  it("logIn authenticates the user and stores both tokens", () => {
    useAuthStore.getState().logIn({ accessToken: "access-1", refreshToken: "refresh-1" })

    const state = useAuthStore.getState()
    expect(state.isAuthenticated).toBe(true)
    expect(state.accessToken).toBe("access-1")
    expect(state.refreshToken).toBe("refresh-1")
  })

  it("logOut clears authentication, tokens and resets onboarding", () => {
    useAuthStore.getState().logIn({ accessToken: "access-1", refreshToken: "refresh-1" })

    useAuthStore.getState().logOut()

    const state = useAuthStore.getState()
    expect(state.isAuthenticated).toBe(false)
    expect(state.accessToken).toBeNull()
    expect(state.refreshToken).toBeNull()
    expect(state.onboardingFinished).toBe(false)
  })

  it("startOnboarding and finishOnboarding toggle the onboarding flag", () => {
    useAuthStore.getState().startOnboarding()
    expect(useAuthStore.getState().onboardingFinished).toBe(false)

    useAuthStore.getState().finishOnboarding()
    expect(useAuthStore.getState().onboardingFinished).toBe(true)
  })
})
