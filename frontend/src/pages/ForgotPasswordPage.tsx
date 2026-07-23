import { AxiosError } from "axios";
import { Link } from "react-router-dom";
import { useState, type FormEvent } from "react";
import toast from "react-hot-toast";

import WaveLogo from "../assets/logos/WaveLogo";
import AuthCard from "../components/auth/AuthCard";
import Button from "../components/ui/Button";
import Input from "../components/ui/Input";
import Label from "../components/ui/Label";
import AuthLayout from "../layouts/AuthLayout";
import { requestPasswordReset } from "../features/auth/api/password";

export default function ForgotPasswordPage() {
  const [email, setEmail] = useState("");
  const [isSubmitting, setIsSubmitting] = useState(false);

  const handleSubmit = async (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault();

    if (!email.trim()) {
      toast.error("Enter your email address.");
      return;
    }

    setIsSubmitting(true);

    try {
      const message = await requestPasswordReset(email.trim());
      toast.success(message);
    } catch (error) {
      const message = error instanceof AxiosError
        ? error.response?.data?.message ?? "Unable to send a reset email."
        : "Unable to send a reset email.";

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
            Reset Password
          </h1>
          <p className="mt-3 max-w-sm text-center text-sm leading-6 text-slate-400">
            Enter your account email and we’ll send a secure reset link.
          </p>
        </div>

        <form onSubmit={handleSubmit} className="space-y-6">
          <div className="space-y-2">
            <Label htmlFor="email">Email Address</Label>
            <Input
              id="email"
              type="email"
              autoComplete="email"
              placeholder="name@company.com"
              value={email}
              disabled={isSubmitting}
              onChange={(event) => setEmail(event.target.value)}
            />
          </div>

          <Button type="submit" isLoading={isSubmitting}>
            Send Reset Link
          </Button>

          <p className="text-center text-sm text-slate-400">
            Remembered your password?{" "}
            <Link to="/" className="font-semibold text-cyan-400 hover:text-cyan-300">
              Sign in
            </Link>
          </p>
        </form>
      </AuthCard>
    </AuthLayout>
  );
}
