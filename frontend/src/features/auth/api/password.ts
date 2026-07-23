import api from "../../../api/axios";

export async function requestPasswordReset(email: string): Promise<string> {
  const response = await api.post<string>("/v1/password-reset/forgot", {
    email,
  });

  return response.data;
}

export async function resetPassword(
  token: string,
  newPassword: string
): Promise<string> {
  const response = await api.post<string>("/v1/password-reset/reset", {
    token,
    newPassword,
  });

  return response.data;
}

export async function verifyEmail(token: string): Promise<string> {
  const response = await api.get<string>("/v1/email-verification/verify", {
    params: { token },
  });

  return response.data;
}
