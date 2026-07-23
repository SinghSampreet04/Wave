import { AxiosError } from "axios";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { useNavigate, Link } from "react-router-dom";
import toast from "react-hot-toast";

import WaveLogo from "../../../assets/logos/WaveLogo";
import AuthCard from "../../../components/auth/AuthCard";
import Button from "../../../components/ui/Button";
import FormError from "../../../components/ui/FormError";
import Input from "../../../components/ui/Input";
import Label from "../../../components/ui/Label";
import PasswordInput from "../../../components/ui/PasswordInput";

import { useLogin } from "../hooks/useLogin";
import {
  loginSchema,
  type LoginFormValues,
} from "../schemas/loginSchema";

export default function LoginForm() {
  const navigate = useNavigate();

  const loginMutation = useLogin();

  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<LoginFormValues>({
    resolver: zodResolver(loginSchema),
    defaultValues: {
      email: "",
      password: "",
    },
  });

  const onSubmit = async (values: LoginFormValues) => {
    try {
      const response = await loginMutation.mutateAsync(values);

      toast.success(response.message);

      navigate("/dashboard");
    } catch (error: unknown) {
      let message = "Unable to sign in.";

      if (error instanceof AxiosError) {
        message =
          error.response?.data?.message ??
          "Unable to sign in.";
      }

      toast.error(message);
    }
  };

  return (
    <AuthCard>
      <div className="mb-10 flex flex-col items-center">
        <WaveLogo size={64} />

        <h1 className="mt-5 text-4xl font-extrabold tracking-tight text-white">
          Welcome Back
        </h1>

        <p className="mt-3 max-w-sm text-center text-sm leading-6 text-slate-400">
          Sign in to your Wave workspace and continue collaborating with your
          team in real time.
        </p>
      </div>

      <form
        onSubmit={handleSubmit(onSubmit)}
        className="space-y-6"
      >
        <div className="space-y-2">
          <Label htmlFor="email">
            Email Address
          </Label>

          <Input
            id="email"
            type="email"
            autoComplete="email"
            placeholder="name@company.com"
            {...register("email")}
          />

          <FormError message={errors.email?.message} />
        </div>

        <div className="space-y-2">
          <div className="flex items-center justify-between">
            <Label htmlFor="password">
              Password
            </Label>

            <Link
              to="/forgot-password"
              className="text-sm font-medium text-cyan-400 transition-colors hover:text-cyan-300"
            >
              Forgot password?
            </Link>
          </div>

          <PasswordInput
            id="password"
            autoComplete="current-password"
            placeholder="Enter your password"
            {...register("password")}
          />

          <FormError message={errors.password?.message} />
        </div>

        <div className="flex items-center justify-between">
          <label className="flex cursor-pointer items-center gap-3 text-sm text-slate-400">
            <input
              type="checkbox"
              className="h-4 w-4 rounded border-slate-600 bg-slate-900 text-cyan-500 focus:ring-cyan-500"
            />

            Remember me
          </label>

          <span className="rounded-full border border-emerald-500/20 bg-emerald-500/10 px-3 py-1 text-xs font-medium text-emerald-400">
            Secure Login
          </span>
        </div>

        <Button
          type="submit"
          isLoading={loginMutation.isPending}
        >
          Sign In
        </Button>

        <div className="relative py-1">
          <div className="absolute inset-0 flex items-center">
            <div className="w-full border-t border-white/10" />
          </div>

          <div className="relative flex justify-center">
            <span className="bg-[#081120] px-4 text-xs uppercase tracking-[0.3em] text-slate-500">
              OR
            </span>
          </div>
        </div>

        <button
          type="button"
          className="flex h-12 w-full items-center justify-center rounded-xl border border-white/10 bg-white/5 font-medium text-slate-200 transition-all duration-200 hover:border-cyan-400/30 hover:bg-white/10"
        >
          Continue with Google
        </button>

        <p className="text-center text-sm text-slate-400">
          Don't have an account?{" "}
          <Link
            to="/register"
            className="font-semibold text-cyan-400 transition-colors hover:text-cyan-300"
          >
            Create an account
          </Link>
        </p>
      </form>
    </AuthCard>
  );
}
