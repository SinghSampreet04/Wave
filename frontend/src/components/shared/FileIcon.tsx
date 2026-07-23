import clsx from "clsx";
import {
  File,
  FileArchive,
  FileCode,
  FileImage,
  FileSpreadsheet,
  FileText,
  FileVideo,
  FileAudio,
} from "lucide-react";

interface FileIconProps {
  fileName: string;

  size?: number;

  className?: string;
}

export default function FileIcon({
  fileName,
  size = 20,
  className,
}: FileIconProps) {
  const extension =
    fileName.split(".").pop()?.toLowerCase() ?? "";

  const Icon = (() => {
    switch (extension) {
      case "jpg":
      case "jpeg":
      case "png":
      case "gif":
      case "svg":
      case "webp":
        return FileImage;

      case "mp4":
      case "mov":
      case "avi":
      case "mkv":
        return FileVideo;

      case "mp3":
      case "wav":
      case "ogg":
        return FileAudio;

      case "pdf":
        return FileText;

      case "doc":
      case "docx":
      case "txt":
        return FileText;

      case "xls":
      case "xlsx":
      case "csv":
        return FileSpreadsheet;

      case "zip":
      case "rar":
      case "7z":
      case "tar":
      case "gz":
        return FileArchive;

      case "java":
      case "kt":
      case "js":
      case "jsx":
      case "ts":
      case "tsx":
      case "json":
      case "xml":
      case "html":
      case "css":
      case "sql":
      case "yml":
      case "yaml":
      case "properties":
      case "md":
        return FileCode;

      default:
        return File;
    }
  })();

  return (
    <Icon
      size={size}
      className={clsx(
        "shrink-0 text-slate-400",
        className
      )}
    />
  );
}