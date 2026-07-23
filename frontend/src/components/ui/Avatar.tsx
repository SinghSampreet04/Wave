import clsx from "clsx";

interface AvatarProps {
  name: string;
  imageUrl?: string;
  size?: "sm" | "md" | "lg";
  className?: string;
}

const COLORS = [
  "from-red-500 to-rose-500",
  "from-orange-500 to-amber-500",
  "from-amber-500 to-yellow-500",
  "from-yellow-500 to-lime-500",
  "from-lime-500 to-green-500",
  "from-green-500 to-emerald-500",
  "from-emerald-500 to-teal-500",
  "from-teal-500 to-cyan-500",
  "from-cyan-500 to-sky-500",
  "from-sky-500 to-blue-500",
  "from-blue-500 to-indigo-500",
  "from-indigo-500 to-violet-500",
  "from-violet-500 to-purple-500",
  "from-purple-500 to-pink-500",
  "from-pink-500 to-rose-500",
];

const SIZE_CLASSES = {
  sm: "h-8 w-8 text-sm",
  md: "h-10 w-10 text-base",
  lg: "h-14 w-14 text-lg",
};

function getColor(name: string) {
  let hash = 0;

  for (let i = 0; i < name.length; i++) {
    hash =
      name.charCodeAt(i) +
      ((hash << 5) - hash);
  }

  return COLORS[Math.abs(hash) % COLORS.length];
}

export default function Avatar({
  name,
  imageUrl,
  size = "md",
  className,
}: AvatarProps) {
  if (imageUrl) {
    return (
      <img
        src={imageUrl}
        alt={name}
        draggable={false}
        className={clsx(
          "rounded-full",

          "object-cover",

          "select-none",

          "border border-[var(--wave-border)]",

          "shadow-[var(--shadow-sm)]",

          SIZE_CLASSES[size],

          className
        )}
      />
    );
  }

  return (
    <div
      className={clsx(
        "flex items-center justify-center",

        "rounded-full",

        "bg-gradient-to-br",

        getColor(name),

        "font-semibold",

        "text-white",

        "select-none",

        "border border-white/10",

        "shadow-[var(--shadow-sm)]",

        SIZE_CLASSES[size],

        className
      )}
    >
      {name.trim().charAt(0).toUpperCase()}
    </div>
  );
}