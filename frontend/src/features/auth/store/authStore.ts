import { create } from "zustand";

import {
  getToken,
  removeToken,
  setToken,
} from "../../../lib/token";

import type { UserResponse } from "../types/auth";

interface AuthState {
  accessToken: string | null;

  currentUser: UserResponse | null;

  isAuthenticated: boolean;
  initialized: boolean;

  login: (
    token: string,
    user: UserResponse
  ) => void;

  setCurrentUser: (
    user: UserResponse
  ) => void;
  setInitialized: (initialized: boolean) => void;

  logout: () => void;
}

export const useAuthStore = create<AuthState>((set) => ({
  accessToken: getToken(),

  currentUser: null,

  isAuthenticated: !!getToken(),
  initialized: false,

  login: (token, user) => {
    setToken(token);

    set({
      accessToken: token,
      currentUser: user,
      isAuthenticated: true,
    });
  },

  setCurrentUser: (user) => {
    set({
      currentUser: user,
      isAuthenticated: true,
    });
  },

  setInitialized: (initialized) => {
    set({ initialized });
  },

  logout: () => {
    removeToken();

    set({
      accessToken: null,
      currentUser: null,
      isAuthenticated: false,
    });
  },
}));

export const logoutUser = () => {
  useAuthStore.getState().logout();
};
