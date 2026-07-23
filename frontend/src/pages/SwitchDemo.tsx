import { useState } from "react";

import Switch from "../components/ui/Switch";

export default function SwitchDemo() {
  const [privateChannel, setPrivateChannel] =
    useState(false);

  return (
    <div className="flex min-h-screen items-center justify-center bg-slate-950">
      <div className="w-96 rounded-2xl border border-slate-800 bg-slate-900 p-8">
        <h1 className="mb-6 text-xl font-semibold text-white">
          Switch Demo
        </h1>

        <Switch
          label="Private Channel"
          checked={privateChannel}
          onCheckedChange={setPrivateChannel}
        />

        <p className="mt-6 text-sm text-slate-400">
          Current value:{" "}
          <span className="font-medium text-cyan-400">
            {privateChannel
              ? "Private"
              : "Public"}
          </span>
        </p>
      </div>
    </div>
  );
}