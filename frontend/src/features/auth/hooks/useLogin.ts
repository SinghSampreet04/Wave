import { useMutation } from "@tanstack/react-query";

import {
  getCurrentUser,
  login as loginApi,
} from "../api/auth";

import { useAuthStore } from "../store/authStore";

import { setToken } from "../../../lib/token";
import { markSessionActive } from "../../../api/axios";

export function useLogin() {
  const login = useAuthStore((state) => state.login);

  return useMutation({
    mutationFn: loginApi,

    onSuccess: async (response) => {
      // Store the token first so Axios includes it in the next request.
      setToken(response.accessToken);
      markSessionActive();

      // Fetch the currently authenticated user.
      const currentUser = await getCurrentUser();

      // Store both the token and user in Zustand.
      login(response.accessToken, currentUser);
    },
  });
}
