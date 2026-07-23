interface WaveLogoProps {
  size?: number;
}

export default function WaveLogo({
  size = 72,
}: WaveLogoProps) {
  return (
    <svg
      width={size}
      height={size}
      viewBox="0 0 120 120"
      fill="none"
      xmlns="http://www.w3.org/2000/svg"
    >
      <defs>
        <linearGradient
          id="waveGradient"
          x1="0%"
          y1="0%"
          x2="100%"
          y2="100%"
        >
          <stop
            offset="0%"
            stopColor="#3B82F6"
          />

          <stop
            offset="50%"
            stopColor="#38BDF8"
          />

          <stop
            offset="100%"
            stopColor="#06B6D4"
          />
        </linearGradient>

        <filter
          id="waveGlow"
          x="-50%"
          y="-50%"
          width="200%"
          height="200%"
        >
          <feGaussianBlur
            stdDeviation="4"
            result="blur"
          />

          <feMerge>
            <feMergeNode in="blur" />
            <feMergeNode in="SourceGraphic" />
          </feMerge>
        </filter>
      </defs>

      {/* Background Circle */}
      <circle
        cx="60"
        cy="60"
        r="54"
        fill="#0F172A"
      />

      {/* Border */}
      <circle
        cx="60"
        cy="60"
        r="53"
        stroke="rgba(255,255,255,.08)"
        strokeWidth="2"
      />

      {/* Ambient Glow */}
      <circle
        cx="60"
        cy="60"
        r="42"
        fill="url(#waveGradient)"
        opacity="0.08"
      />

      {/* Main Wave */}
      <path
        d="
          M18 64
          C28 42 40 42 50 64
          C60 86 72 86 82 64
          C92 42 104 42 112 64
        "
        stroke="url(#waveGradient)"
        strokeWidth="8"
        strokeLinecap="round"
        strokeLinejoin="round"
        filter="url(#waveGlow)"
      />

      {/* Secondary Wave */}
      <path
        d="
          M24 78
          C34 62 44 62 54 78
          C64 94 74 94 84 78
          C94 62 102 62 108 78
        "
        stroke="url(#waveGradient)"
        strokeWidth="4"
        strokeLinecap="round"
        opacity=".45"
      />

      {/* Accent Dot */}
      <circle
        cx="92"
        cy="34"
        r="4"
        fill="#67E8F9"
      />
    </svg>
  );
}