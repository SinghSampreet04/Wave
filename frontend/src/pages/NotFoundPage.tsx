import { Link } from "react-router-dom";

export default function NotFoundPage() {
  return (
    <main className="flex min-h-screen items-center justify-center bg-[#040814] px-6 text-white">
      <div className="max-w-md text-center">
        <p className="text-sm font-semibold uppercase tracking-[0.25em] text-cyan-300">
          404
        </p>
        <h1 className="mt-4 text-4xl font-bold">Page not found</h1>
        <p className="mt-3 text-slate-400">
          The page you requested does not exist or is no longer available.
        </p>
        <Link
          to="/dashboard"
          className="mt-8 inline-flex rounded-2xl bg-cyan-500 px-6 py-3 font-semibold text-slate-950 transition hover:bg-cyan-400"
        >
          Return to Wave
        </Link>
      </div>
    </main>
  );
}
