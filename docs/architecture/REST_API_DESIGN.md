# Wave - REST API Design

# Base URL

```
/api/v1
```

---

# Authentication

| Method | Endpoint | Description |
|---------|----------|-------------|
| POST | /auth/register | Register a new user |
| POST | /auth/login | Login user |
| POST | /auth/refresh | Refresh access token |
| POST | /auth/logout | Logout user |

---

# Users

| Method | Endpoint | Description |
|---------|----------|-------------|
| GET | /users/me | Current user |
| PUT | /users/me | Update profile |
| GET | /users/{id} | User profile |

---

# Workspaces

| Method | Endpoint | Description |
|---------|----------|-------------|
| GET | /workspaces | Get user workspaces |
| POST | /workspaces | Create workspace |
| GET | /workspaces/{id} | Workspace details |
| PUT | /workspaces/{id} | Update workspace |
| DELETE | /workspaces/{id} | Delete workspace |

---

# Workspace Members

| Method | Endpoint | Description |
|---------|----------|-------------|
| GET | /workspaces/{id}/members | List members |
| POST | /workspaces/{id}/invite | Invite member |
| DELETE | /workspaces/{id}/members/{userId} | Remove member |

---

# Channels

| Method | Endpoint | Description |
|---------|----------|-------------|
| GET | /workspaces/{id}/channels | List channels |
| POST | /channels | Create channel |
| PUT | /channels/{id} | Update channel |
| DELETE | /channels/{id} | Delete channel |

---

# Messages

| Method | Endpoint | Description |
|---------|----------|-------------|
| GET | /channels/{id}/messages | Channel messages |
| POST | /messages | Send message |
| PUT | /messages/{id} | Edit message |
| DELETE | /messages/{id} | Delete message |

---

# Reactions

| Method | Endpoint | Description |
|---------|----------|-------------|
| POST | /messages/{id}/reactions | Add reaction |
| DELETE | /messages/{id}/reactions | Remove reaction |

---

# Files

| Method | Endpoint | Description |
|---------|----------|-------------|
| POST | /files/upload | Upload file |
| GET | /files/{id} | Download file |

---

# Notifications

| Method | Endpoint | Description |
|---------|----------|-------------|
| GET | /notifications | Get notifications |
| PUT | /notifications/{id}/read | Mark as read |

---

# Health

| Method | Endpoint | Description |
|---------|----------|-------------|
| GET | /health | Application health |