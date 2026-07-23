import { AxiosError } from "axios";
import { Link, useNavigate, useSearchParams } from "react-router-dom";
import { useState, type FormEvent } from "react";
import toast from "react-hot-toast";

import WaveLogo from "../assets/logos/WaveLogo";
import AuthCard from "../components/auth/AuthCard";
import Button from "../components/ui/Button";
import PasswordInput from "../components/ui/PasswordInput";
import Label from "../components/ui/Label";
import AuthLayout from "../layouts/AuthLayout";
import { resetPassword } from "../features/auth/api/password";

export default function ResetPasswordPage() {
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();
  const [password, setPassword] = useState("");
  const [confirmation, setConfirmation] = useState("");
  const [isSubmitting, setIsSubmitting] = useState(false);
  const token = searchParams.get("token");

  const handleSubmit = async (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault();

    if (!token) {
      toast.error("This reset link is invalid.");
      return;
    }

    if (password.length < 8) {
      toast.error("Password must be at least 8 characters.");
      return;
    }

    if (password !== confirmation) {
      toast.error("Passwords do not match.");
      return;
    }

    setIsSubmitting(true);

    try {
      const message = await resetPassword(token, password);
      toast.success(message);
      navigate("/", { replace: true });
    } catch (error) {
      const message = error instanceof AxiosError
        ? error.response?.data?.message ?? "Unable to reset your password."
        : "Unable to reset your password.";

      toast.error(message);
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <AuthLayout>
      <AuthCard>
        <div className="mb-10 flex flex-col items-center">
          <WaveLogo size={64} />
          <h1 className="mt-5 text-4xl font-extrabold tracking-tight text-white">
            Choose a New Password
          </h1>
        </div>

        <form onSubmit={handleSubmit} className="space-y-6">
          <div className="space-y-2">
            <Label htmlFor="password">New Password</Label>
            <PasswordInput
              id="password"
              autoComplete="new-password"
              value={password}
              disabled={isSubmitting}
              onChange={(event) => setPassword(event.target.value)}
            />
          </div>

          <div className="space-y-2">
            <Label htmlFor="confirmation">Confirm New Password</Label>
            <PasswordInput
              id="confirmation"
              autoComplete="new-password"
              value={confirmation}
              disabled={isSubmitting}
              onChange={(event) => setConfirmation(event.target.value)}
            />
          </div>

          <Button type="submit" isLoading={isSubmitting} disabled={!token}>
            Update Password
          </Button>

          <p className="text-center text-sm text-slate-400">
            <Link to="/" className="font-semibold text-cyan-400 hover:text-cyan-300">
              Back to sign in
            </Link>
          </p>
        </form>
      </AuthCard>
    </AuthLayout>
  );
}
