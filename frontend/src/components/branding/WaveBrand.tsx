import clsx from "clsx";

import WaveLogo from "../../assets/logos/WaveLogo";

interface WaveBrandProps {
  compact?: boolean;
  className?: string;
}

export default function WaveBrand({
  compact = false,
  className,
}: WaveBrandProps) {
  return (
    <div
      className={clsx(
        "inline-flex items-center gap-4",
        className
      )}
    >
      <div className="flex h-14 w-14 items-center justify-center rounded-2xl border border-cyan-400/20 bg-cyan-400/10 backdrop-blur-xl">
        <WaveLogo size={34} />
      </div>

      <div>
        <h1
          className={clsx(
            "font-bold tracking-tight text-white",
            compact ? "text-xl" : "text-2xl"
          )}
        >
          Wave
        </h1>

        <p
          className={clsx(
            "text-cyan-200",
            compact ? "text-xs" : "text-sm"
          )}
        >
          Modern Team Collaboration
        </p>
      </div>
    </div>
  );
}