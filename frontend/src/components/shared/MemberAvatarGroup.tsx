import clsx from "clsx";

import { Avatar } from "../ui";

interface Member {
  id: string | number;
  name: string;
}

interface MemberAvatarGroupProps {
  members: Member[];

  max?: number;

  size?: "sm" | "md" | "lg";

  className?: string;
}

export default function MemberAvatarGroup({
  members,
  max = 4,
  size = "md",
  className,
}: MemberAvatarGroupProps) {
  const visibleMembers = members.slice(0, max);
  const remaining = members.length - visibleMembers.length;

  return (
    <div
      className={clsx(
        "flex items-center",
        className
      )}
    >
      {visibleMembers.map((member, index) => (
        <div
          key={member.id}
          className={clsx(
            index > 0 && "-ml-2"
          )}
        >
          <Avatar
            name={member.name}
            size={size}
          />
        </div>
      ))}

      {remaining > 0 && (
        <div className="-ml-2 flex h-8 w-8 items-center justify-center rounded-full border-2 border-slate-900 bg-slate-700 text-xs font-medium text-white">
          +{remaining}
        </div>
      )}
    </div>
  );
}