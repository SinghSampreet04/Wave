export interface LoginRequest {
  email: string;
  password: string;
}

export interface LoginResponse {
  message: string;
  accessToken: string;
}

export interface RegisterRequest {
  firstName: string;
  lastName: string;
  username: string;
  email: string;
  password: string;
}

export interface RegisterResponse {
  message: string;
}

export interface UserResponse {
  id: number;
  firstName: string;
  lastName: string;
  username: string;
  email: string;
  role: string;
  avatar: string | null;
  bio: string | null;
  presencePreference: "ONLINE" | "AWAY" | "BUSY" | "INVISIBLE" | "OFFLINE";
}
