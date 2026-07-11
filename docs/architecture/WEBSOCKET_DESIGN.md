# Wave - WebSocket Architecture

# Purpose

WebSockets provide real-time communication between clients and the server.

Wave uses WebSockets for messaging, typing indicators, presence updates, and notifications.

---

# Connection Flow

Client

↓

Connect to WebSocket

↓

Authenticate using JWT

↓

Subscribe to channels

↓

Receive live events

---

# Event Types

## Messaging

- Send Message
- Edit Message
- Delete Message

---

## Presence

- User Online
- User Offline
- User Away
- User Do Not Disturb

---

## Typing

- Start Typing
- Stop Typing

---

## Notifications

- Mention
- Workspace Invite
- Channel Invite
- Message Reaction

---

# STOMP Destinations

## Client Sends

/app/chat.send

/app/chat.edit

/app/chat.delete

/app/typing.start

/app/typing.stop

---

## Client Subscribes

/topic/channels/{channelId}

/topic/presence

/topic/notifications/{userId}

---

# Security

- JWT required before connection
- Validate user identity
- Reject invalid tokens
- Authorize workspace membership before subscribing