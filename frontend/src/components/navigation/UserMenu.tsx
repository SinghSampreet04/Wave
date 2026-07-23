import { ChevronDown, LogOut, Settings, User } from "lucide-react";
import { useEffect, useRef, useState } from "react";
import clsx from "clsx";

import { Avatar, Button, Menu, MenuItem } from "../ui";
import useClickOutside from "../../hooks/useClickOutside";

interface UserMenuProps {
  name: string;
  email?: string;

  onProfile?: () => void;
  onSettings?: () => void;
  onLogout?: () => void;

  className?: string;
}

export default function UserMenu({
  name,
  email,
  onProfile,
  onSettings,
  onLogout,
  className,
}: UserMenuProps) {
  const [open, setOpen] = useState(false);
  const containerRef = useRef<HTMLDivElement>(null);
  useClickOutside(containerRef, () => setOpen(false));

  useEffect(() => {
    const closeOnEscape = (event: KeyboardEvent) => {
      if (event.key === "Escape") setOpen(false);
    };
    window.addEventListener("keydown", closeOnEscape);
    return () => window.removeEventListener("keydown", closeOnEscape);
  }, []);

  return (
    <div
      ref={containerRef}
      className={clsx("relative", className)}
    >
      <Button
        variant="ghost"
        fullWidth={false}
        aria-haspopup="menu"
        aria-expanded={open}
        onClick={() => setOpen((value) => !value)}
        className="flex items-center gap-2"
      >
        <Avatar
          name={name}
          size="sm"
        />

        <div className="hidden text-left md:block">
          <div className="text-sm font-medium">
            {name}
          </div>

          {email && (
            <div className="text-xs text-slate-400">
              {email}
            </div>
          )}
        </div>

        <ChevronDown
          size={16}
          className={clsx(
            "transition-transform",
            open && "rotate-180"
          )}
        />
      </Button>

      {open && (
        <div className="absolute right-0 z-[100] mt-2">
          <Menu role="menu">
            <MenuItem
              icon={<User size={16} />}
              onClick={() => {
                setOpen(false);
                onProfile?.();
              }}
            >
              Profile
            </MenuItem>

            <MenuItem
              icon={<Settings size={16} />}
              onClick={() => {
                setOpen(false);
                onSettings?.();
              }}
            >
              Settings
            </MenuItem>

            <MenuItem
              icon={<LogOut size={16} />}
              danger
              onClick={() => {
                setOpen(false);
                onLogout?.();
              }}
            >
              Sign Out
            </MenuItem>
          </Menu>
        </div>
      )}
    </div>
  );
}
