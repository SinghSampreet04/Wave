# Wave - Database Design

# Database

PostgreSQL 17

---

# Tables

## Authentication

users

Stores registered users.

refresh_tokens

Stores refresh tokens for JWT authentication.

---

## Workspaces

workspaces

Workspace information.

workspace_members

Users belonging to workspaces.

workspace_invites

Invitation links and pending invites.

---

## Channels

channels

Workspace channels.

channel_members

Members of private channels.

---

## Messaging

messages

Stores all chat messages.

message_reactions

Stores emoji reactions.

---

## Files

files

Uploaded files.

---

## Notifications

notifications

Stores user notifications.

---

# Entity Relationship

User

↓

Workspace

↓

Channel

↓

Message

↓

Reaction

↓

Notification

---

# Total Tables

1. users

2. refresh_tokens

3. workspaces

4. workspace_members

5. workspace_invites

6. channels

7. channel_members

8. messages

9. message_reactions

10. files

11. notifications