export interface Notification {
  id: number;
  type: string;
  title: string;
  body: string;
  referenceId: number | null;
  referenceType: string | null;
  read: boolean;
  createdAt: string;
}
