import {
  ArrowRight,
  Bell,
  Lock,
  MessageSquare,
  Users,
} from "lucide-react";

import WaveBrand from "../branding/WaveBrand";

const features = [
  {
    icon: MessageSquare,
    title: "Real-time Messaging",
    description:
      "Collaborate instantly with teammates across every workspace.",
  },
  {
    icon: Users,
    title: "Private Channels",
    description:
      "Organize conversations with secure team spaces.",
  },
  {
    icon: Bell,
    title: "Smart Notifications",
    description:
      "Stay focused with meaningful alerts and mentions.",
  },
  {
    icon: Lock,
    title: "Enterprise Security",
    description:
      "JWT authentication, encrypted communication, and role-based access.",
  },
];

export default function AuthBrand() {
  return (
    <div className="hidden max-w-xl flex-col justify-center lg:flex">
      <WaveBrand />

      <h1 className="mt-10 text-6xl font-extrabold leading-tight tracking-tight text-white">
        Work together
        <br />
        without the chaos.
      </h1>

      <p className="mt-6 max-w-lg text-lg leading-8 text-slate-300">
        Wave combines messaging, collaboration, file
        sharing, and workspace management into one fast,
        secure platform built for modern teams.
      </p>

      <div className="mt-12 space-y-5">
        {features.map(
          ({ icon: Icon, title, description }) => (
            <div
              key={title}
              className="flex items-start gap-4 rounded-2xl border border-white/5 bg-white/[0.03] p-4 transition-all duration-300 hover:border-cyan-400/20 hover:bg-white/[0.05]"
            >
              <div className="flex h-12 w-12 items-center justify-center rounded-xl bg-gradient-to-br from-blue-600 to-cyan-500 text-white shadow-lg shadow-cyan-500/20">
                <Icon size={22} />
              </div>

              <div>
                <h3 className="font-semibold text-white">
                  {title}
                </h3>

                <p className="mt-1 text-sm leading-6 text-slate-400">
                  {description}
                </p>
              </div>
            </div>
          )
        )}
      </div>

      <div className="mt-10 inline-flex items-center gap-2 text-cyan-300">
        <span className="text-sm font-medium">
          Built with React, Spring Boot &
          WebSockets
        </span>

        <ArrowRight size={16} />
      </div>
    </div>
  );
}