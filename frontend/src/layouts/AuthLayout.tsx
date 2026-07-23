import type { ReactNode } from "react";
import AuthBrand from "../components/auth/AuthBrand";

interface Props {
  children: ReactNode;
}

export default function AuthLayout({ children }: Props) {
  return (
    <div className="relative min-h-screen overflow-x-hidden bg-[#081120] text-white">
      {/* Background */}
      <div className="absolute inset-0 bg-[radial-gradient(circle_at_top,rgba(6,182,212,0.08),transparent_45%),radial-gradient(circle_at_bottom_right,rgba(59,130,246,0.08),transparent_40%)]" />

      {/* Grid */}
      <div
        className="absolute inset-0 opacity-[0.035]"
        style={{
          backgroundImage: `
            linear-gradient(rgba(255,255,255,.15) 1px, transparent 1px),
            linear-gradient(90deg, rgba(255,255,255,.15) 1px, transparent 1px)
          `,
          backgroundSize: "48px 48px",
        }}
      />

      {/* Glow 1 */}
      <div className="absolute -left-40 -top-40 h-[36rem] w-[36rem] rounded-full bg-cyan-500/20 blur-[180px]" />

      {/* Glow 2 */}
      <div className="absolute -right-40 bottom-0 h-[32rem] w-[32rem] rounded-full bg-blue-600/20 blur-[180px]" />

      {/* Glow 3 */}
      <div className="absolute left-1/2 top-1/3 h-96 w-96 -translate-x-1/2 rounded-full bg-sky-400/10 blur-[140px]" />

      {/* Content */}
      <div className="relative mx-auto flex min-h-screen w-full max-w-7xl items-center px-6 py-12 lg:px-10">
        <div className="grid w-full items-center gap-20 lg:grid-cols-2">
          {/* Left Side */}
          <div className="hidden lg:flex">
            <AuthBrand />
          </div>

          {/* Right Side */}
          <div className="flex justify-center lg:justify-end">
            {children}
          </div>
        </div>
      </div>
    </div>
  );
}
