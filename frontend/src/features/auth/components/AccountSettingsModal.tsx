import { useState } from "react";
import toast from "react-hot-toast";
import { X } from "lucide-react";

import { updateCurrentUser, updatePresence } from "../api/auth";
import { useAuthStore } from "../store/authStore";
import Button from "../../../components/ui/Button";
import Input from "../../../components/ui/Input";

export default function AccountSettingsModal({
  onClose,
}: {
  onClose: () => void;
}) {
  const user = useAuthStore((state) => state.currentUser);
  const setCurrentUser = useAuthStore((state) => state.setCurrentUser);
  const [saving, setSaving] = useState(false);
  const [form, setForm] = useState(() => ({
    firstName: user?.firstName ?? "",
    lastName: user?.lastName ?? "",
    username: user?.username ?? "",
    bio: user?.bio ?? "",
    avatar: user?.avatar ?? "",
    presencePreference: user?.presencePreference ?? "ONLINE",
  }));

  if (!user) return null;

  const save = async () => {
    setSaving(true);
    try {
      const updated = await updateCurrentUser({
        firstName: form.firstName,
        lastName: form.lastName,
        username: form.username,
        bio: form.bio,
        avatar: form.avatar,
      });
      await updatePresence(
        form.presencePreference as "ONLINE" | "AWAY" | "BUSY" | "INVISIBLE"
      );
      setCurrentUser({
        ...updated,
        presencePreference: form.presencePreference as typeof updated.presencePreference,
      });
      toast.success("Account settings saved.");
      onClose();
    } catch {
      toast.error("Unable to save account settings.");
    } finally {
      setSaving(false);
    }
  };

  return (
    <div
      role="dialog"
      aria-modal="true"
      aria-label="Account settings"
      className="fixed inset-0 z-[200] flex items-center justify-center bg-black/70 p-4"
    >
      <div className="max-h-[90vh] w-full max-w-lg overflow-y-auto rounded-3xl border border-white/15 bg-slate-950 p-6 shadow-2xl">
        <div className="flex items-center justify-between">
          <div>
            <h2 className="text-xl font-semibold">Account settings</h2>
            <p className="mt-1 text-sm text-slate-400">{user.email}</p>
          </div>
          <button type="button" onClick={onClose} aria-label="Close settings">
            <X className="text-slate-400" />
          </button>
        </div>

        <div className="mt-6 grid gap-4 sm:grid-cols-2">
          <Input
            label="First name"
            value={form.firstName}
            onChange={(event) =>
              setForm((current) => ({ ...current, firstName: event.target.value }))
            }
          />
          <Input
            label="Last name"
            value={form.lastName}
            onChange={(event) =>
              setForm((current) => ({ ...current, lastName: event.target.value }))
            }
          />
          <div className="sm:col-span-2">
            <Input
              label="Username"
              value={form.username}
              onChange={(event) =>
                setForm((current) => ({ ...current, username: event.target.value }))
              }
            />
          </div>
          <label className="sm:col-span-2 text-sm font-semibold text-slate-300">
            Presence
            <select
              value={form.presencePreference}
              onChange={(event) =>
                setForm((current) => ({
                  ...current,
                  presencePreference: event.target.value as typeof current.presencePreference,
                }))
              }
              className="mt-2 w-full rounded-2xl border border-white/10 bg-slate-900 px-4 py-3 text-white"
            >
              <option value="ONLINE">Online</option>
              <option value="AWAY">Away</option>
              <option value="BUSY">Busy</option>
              <option value="INVISIBLE">Invisible</option>
            </select>
          </label>
          <label className="sm:col-span-2 text-sm font-semibold text-slate-300">
            Bio
            <textarea
              maxLength={300}
              rows={3}
              value={form.bio}
              onChange={(event) =>
                setForm((current) => ({ ...current, bio: event.target.value }))
              }
              className="mt-2 w-full rounded-2xl border border-white/10 bg-slate-900 px-4 py-3 text-white"
            />
          </label>
        </div>

        <div className="mt-6 flex justify-end gap-3">
          <Button variant="ghost" fullWidth={false} onClick={onClose}>
            Cancel
          </Button>
          <Button fullWidth={false} isLoading={saving} onClick={save}>
            Save changes
          </Button>
        </div>
      </div>
    </div>
  );
}
