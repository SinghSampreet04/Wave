import axios, { AxiosError, type InternalAxiosRequestConfig } from "axios";
import toast from "react-hot-toast";

import { getToken, setToken } from "../lib/token";
import { logoutUser } from "../features/auth/store/authStore";

const configuredApiBaseUrl =
  import.meta.env.VITE_API_BASE_URL ??
  "http://localhost:8082/api";

export const apiBaseUrl = configuredApiBaseUrl
  .replace(/\/+$/, "")
  .replace(/\/v1$/, "");

const api = axios.create({
  baseURL: apiBaseUrl,
  withCredentials: true,
  headers: {
    "Content-Type": "application/json",
  },
});

api.interceptors.request.use((config) => {
  const token = getToken();
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

type RetryableRequest = InternalAxiosRequestConfig & {
  _waveRetried?: boolean;
};

let sessionExpired = false;
let refreshPromise: Promise<string> | null = null;

export async function refreshAccessToken(): Promise<string> {
  if (!refreshPromise) {
    refreshPromise = axios
      .post<{ accessToken: string }>(
        `${apiBaseUrl}/v1/auth/refresh`,
        {},
        { withCredentials: true }
      )
      .then((response) => {
        const token = response.data.accessToken;
        setToken(token);
        sessionExpired = false;
        return token;
      })
      .finally(() => {
        refreshPromise = null;
      });
  }
  return refreshPromise;
}

export function markSessionActive() {
  sessionExpired = false;
}

api.interceptors.response.use(
  (response) => response,
  async (error: AxiosError) => {
    const status = error.response?.status;
    const originalRequest = error.config as RetryableRequest | undefined;
    const requestUrl = originalRequest?.url ?? "";
    const isPublicAuthRequest = requestUrl.startsWith("/v1/auth/");

    if (
      status === 401 &&
      getToken() &&
      !isPublicAuthRequest &&
      originalRequest &&
      !originalRequest._waveRetried
    ) {
      originalRequest._waveRetried = true;
      try {
        const token = await refreshAccessToken();
        originalRequest.headers.Authorization = `Bearer ${token}`;
        return api(originalRequest);
      } catch {
        if (!sessionExpired) {
          sessionExpired = true;
          logoutUser();
          toast.error("Your session has expired. Please sign in again.");
          window.location.replace("/login");
        }
      }
    }

    return Promise.reject(error);
  }
);

export default api;
