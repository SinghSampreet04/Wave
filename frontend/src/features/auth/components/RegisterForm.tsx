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

import { useRegister } from "../hooks/useRegister";
import {
  registerSchema,
  type RegisterFormValues,
} from "../schemas/registerSchema";

export default function RegisterForm() {
  const navigate = useNavigate();

  const registerMutation = useRegister();

  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<RegisterFormValues>({
    resolver: zodResolver(registerSchema),
    defaultValues: {
      firstName: "",
      lastName: "",
      username: "",
      email: "",
      password: "",
      confirmPassword: "",
    },
  });

  const onSubmit = async (
    values: RegisterFormValues
  ) => {
    try {
      const request = {
        firstName: values.firstName,
        lastName: values.lastName,
        username: values.username,
        email: values.email,
        password: values.password,
      };

      const response =
        await registerMutation.mutateAsync(request);

      toast.success(response.message);

      navigate("/login");
    } catch (error: unknown) {

      let message = "Unable to create your account.";

      if (error instanceof AxiosError) {
        message =
          error.response?.data?.message ??
          error.message ??
          message;
      }

      toast.error(message);
    }
  };

  return (
    <AuthCard>
      <div className="mb-10 flex flex-col items-center">
        <WaveLogo size={64} />

        <h1 className="mt-5 text-4xl font-extrabold tracking-tight text-white">
          Create Account
        </h1>

        <p className="mt-3 max-w-sm text-center text-sm leading-6 text-slate-400">
          Join Wave and collaborate with your team in real time.
        </p>
      </div>

      <form
        onSubmit={handleSubmit(onSubmit)}
        className="space-y-6"
      >
        <div className="grid grid-cols-2 gap-4">
          <div className="space-y-2">
            <Label htmlFor="firstName">
              First Name
            </Label>

            <Input
              id="firstName"
              placeholder="John"
              {...register("firstName")}
            />

            <FormError
              message={errors.firstName?.message}
            />
          </div>

          <div className="space-y-2">
            <Label htmlFor="lastName">
              Last Name
            </Label>

            <Input
              id="lastName"
              placeholder="Doe"
              {...register("lastName")}
            />

            <FormError
              message={errors.lastName?.message}
            />
          </div>
        </div>

        <div className="space-y-2">
          <Label htmlFor="username">
            Username
          </Label>

          <Input
            id="username"
            placeholder="johndoe"
            autoComplete="username"
            {...register("username")}
          />

          <FormError
            message={errors.username?.message}
          />
        </div>

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

          <FormError
            message={errors.email?.message}
          />
        </div>

        <div className="space-y-2">
          <Label htmlFor="password">
            Password
          </Label>

          <PasswordInput
            id="password"
            autoComplete="new-password"
            placeholder="Create a password"
            {...register("password")}
          />

          <FormError
            message={errors.password?.message}
          />
        </div>

        <div className="space-y-2">
          <Label htmlFor="confirmPassword">
            Confirm Password
          </Label>

          <PasswordInput
            id="confirmPassword"
            autoComplete="new-password"
            placeholder="Confirm your password"
            {...register("confirmPassword")}
          />

          <FormError
            message={errors.confirmPassword?.message}
          />
        </div>

        <Button
          type="submit"
          isLoading={registerMutation.isPending}
        >
          Create Account
        </Button>

        <p className="text-center text-sm text-slate-400">
          Already have an account?{" "}
          <Link
            to="/login"
            className="font-semibold text-cyan-400 transition-colors hover:text-cyan-300"
          >
            Sign In
          </Link>
        </p>
      </form>
    </AuthCard>
  );
}
