# Wave Database Design

## Core Tables

### users

Stores all registered users.

---

### workspaces

A workspace is an organization where users collaborate.

Example:

- OpenAI
- Google
- Personal

---

### workspace_members

Connects users to workspaces.

A user can belong to many workspaces.

A workspace contains many users.

---

### channels

Channels belong to a workspace.

Examples:

- general
- backend
- frontend
- random

---

### messages

Stores every message.

Each message belongs to:

- one channel
- one sender

---

### message_reactions

Stores reactions.

Examples:

👍 ❤️ 😂 🔥 🎉

---

### attachments

Stores uploaded files.

Images

PDF

ZIP

Documents

---

### notifications

Stores unread notifications.

Examples

Someone mentioned you.

Someone replied to your message.

Someone invited you.

---

### direct_conversations

Private conversations between two users.

---

### direct_messages

Messages inside a private conversation.