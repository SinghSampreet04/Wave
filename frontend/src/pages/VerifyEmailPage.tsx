import { AxiosError } from "axios";
import { Link, useSearchParams } from "react-router-dom";
import { useState } from "react";
import toast from "react-hot-toast";

import WaveLogo from "../assets/logos/WaveLogo";
import AuthCard from "../components/auth/AuthCard";
import Button from "../components/ui/Button";
import AuthLayout from "../layouts/AuthLayout";
import { verifyEmail } from "../features/auth/api/password";

export default function VerifyEmailPage() {
  const [searchParams] = useSearchParams();
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [verified, setVerified] = useState(false);
  const token = searchParams.get("token");

  const handleVerification = async () => {
    if (!token) {
      toast.error("This verification link is invalid.");
      return;
    }

    setIsSubmitting(true);

    try {
      const message = await verifyEmail(token);
      setVerified(true);
      toast.success(message);
    } catch (error) {
      const message = error instanceof AxiosError
        ? error.response?.data?.message ?? "Unable to verify this email."
        : "Unable to verify this email.";

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
            Verify Your Email
          </h1>
          <p className="mt-3 max-w-sm text-center text-sm leading-6 text-slate-400">
            {verified
              ? "Your email address is verified. You can now sign in."
              : "Confirm your email address to finish setting up your Wave account."}
          </p>
        </div>

        {verified ? (
          <Link
            to="/"
            className="flex h-12 w-full items-center justify-center rounded-xl bg-cyan-500 font-medium text-white hover:bg-cyan-400"
          >
            Continue to Sign In
          </Link>
        ) : (
          <Button
            type="button"
            isLoading={isSubmitting}
            disabled={!token}
            onClick={handleVerification}
          >
            Verify Email
          </Button>
        )}
      </AuthCard>
    </AuthLayout>
  );
}
