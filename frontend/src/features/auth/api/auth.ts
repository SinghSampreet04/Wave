import api from "../../../api/axios";

import type {
  LoginRequest,
  LoginResponse,
  RegisterRequest,
  RegisterResponse,
  UserResponse,
} from "../types/auth";

export async function login(
  request: LoginRequest
): Promise<LoginResponse> {
  const response = await api.post<LoginResponse>(
    "/v1/auth/login",
    request
  );

  return response.data;
}

export async function register(
  request: RegisterRequest
): Promise<RegisterResponse> {
  const response = await api.post<RegisterResponse>(
    "/v1/auth/register",
    request
  );

  return response.data;
}

export async function getCurrentUser(): Promise<UserResponse> {
  const response = await api.get<UserResponse>(
    "/v1/users/me"
  );

  return response.data;
}

export async function logoutSession(): Promise<void> {
  await api.post("/v1/auth/logout");
}

export async function updateCurrentUser(
  request: Pick<
    UserResponse,
    "firstName" | "lastName" | "username" | "bio" | "avatar"
  >
): Promise<UserResponse> {
  const response = await api.patch<UserResponse>("/v1/users/me", request);
  return response.data;
}

export async function updatePresence(
  status: Exclude<UserResponse["presencePreference"], "OFFLINE">
): Promise<void> {
  await api.patch("/v1/presence/me", { status });
}
