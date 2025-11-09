


# 📄 DocumentFlow — Automated Document Approval Workflow API

**DocumentFlow** is a backend system designed to automate **document submission and approval workflows** within an organization.  
It leverages **Spring Boot**, **Spring Integration**, and **Spring Security (JWT)** to orchestrate asynchronous workflows, email notifications, and approval decisions — ensuring full auditability and traceability.

---

## 🚀 Features

- 📨 **Asynchronous Document Workflows** — Built with Spring Integration channels & flows.
- 📧 **Email Notifications** — Automatic email requests sent to approvers.
- ✅ **Approval / Rejection API** — Approvers respond via REST endpoints.
- 🔒 **JWT Security** — Protect all endpoints with token-based authentication.
- 🧾 **Audit Logging** — Every workflow step is tracked and stored.
- 🔁 **Retry & Error Handling** — Graceful handling of transient workflow errors.

---

## 🧩 Tech Stack

| Component | Technology |
|------------|-------------|
| **Backend Framework** | Spring Boot 3.x |
| **Workflow Engine** | Spring Integration |
| **Security** | Spring Security, JWT |
| **Database** | PostgreSQL / H2 |
| **Build Tool** | Maven |
| **Mail Service** | Spring Integration Mail (SMTP) |
| **Language** | Java 17+ |


## ⚙️ Architecture Overview



```
    +----------------+
    |     USER       |
    |  (Submitter)   |
    +-------+--------+
            |
            | REST: POST /api/documents/submit
            v
    +-------+--------+
    | DocumentService|
    +-------+--------+
            | (async message)
            v
  [documentSubmissionChannel]
            |
    +-------+--------+
    | Email Flow     |
    | (SMTP Notify)  |
    +-------+--------+
            |
            v
    +-------+--------+
    |   APPROVER     |
    +-------+--------+
            |
            | REST: POST /api/approvals/{id}/decision
            v
  [approvalDecisionChannel]
            |
    +-------+--------+
    | Status Update  |
    | & Audit Log    |
    +----------------+
```



---

## 🔐 User Roles

| Role | Description | Example Actions |
|------|--------------|----------------|
| `ROLE_USER` | Can submit documents for approval | Upload document |
| `ROLE_APPROVER` | Can approve or reject submitted documents | Send approval decision |
| `ROLE_ADMIN` | (Optional) Can view audit logs | View audit history |

---

## 🧠 Learning Objectives

- Master **Spring Integration** for event-driven architectures.
- Implement **message channels** and **integration flows**.
- Configure **email adapters** for SMTP notification delivery.
- Design **secure REST APIs** for human-in-the-loop decisions.
- Maintain **stateful workflows** with audit and trace logs.

---

## 🧾 API Endpoints

### 🔑 Authentication

| Method | Endpoint | Description |
|---------|-----------|-------------|
| `POST` | `/api/auth/signup` | Register new user or approver |
| `POST` | `/api/auth/login` | Login and receive JWT token |

---

### 📄 Document Submission

| Method | Endpoint | Role | Description |
|---------|-----------|------|-------------|
| `POST` | `/api/documents/submit` | `ROLE_USER` | Submit new document for approval |
---
**Request (multipart/form-data)**:

### Multipart/Form-Data Request

| Key        | Type  | Example       |
|-------------|-------|---------------|
| `title`     | text  | `Annual Report` |
| `file`      | file  | `report.pdf`  |
| `submitter` | text  | `ali`         |

---

### ✅ Approval Decision

| Method | Endpoint | Role | Description |
|---------|-----------|------|-------------|
| `POST` | `/api/approvals/{documentId}/decision` | `ROLE_APPROVER` | Submit approval or rejection decision |

**Request Body (JSON):**
```json
{
  "approver": "manager",
  "decision": "APPROVED",
  "comment": "Everything looks good."
}
````

---

### 📜 Audit Logs (optional)

| Method | Endpoint          | Role         | Description            |
| ------ | ----------------- | ------------ | ---------------------- |
| `GET`  | `/api/audit/logs` | `ROLE_ADMIN` | View all audit history |

---

## 🧰 Configuration

In your `application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/documentflow
spring.datasource.username=postgres
spring.datasource.password=12345
spring.jpa.hibernate.ddl-auto=update

spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your_email@gmail.com
spring.mail.password=your_app_password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true

jwt.secret=supersecretkey
jwt.expiration=86400000
```

---

## 🧪 Postman Usage

**Step 1.** Sign up as user and approver
**Step 2.** Login to get JWT token
**Step 3.** Use token in `Authorization: Bearer <token>` header
**Step 4.**

* `POST /api/documents/submit` → to upload a document
* `POST /api/approvals/{id}/decision` → to approve/reject

---

## 🛠️ Build & Run

### Using Maven

```bash
mvn clean install
mvn spring-boot:run
```

App runs at:
👉 [http://localhost:8080](http://localhost:8080)

---

## 🧾 Example Workflow

1. **User** uploads a document → status = `PENDING_APPROVAL`
2. **Spring Integration** sends email notification to approvers
3. **Approver** approves or rejects via REST API
4. Status updated → `APPROVED` / `REJECTED`
5. **Audit log** stores each event for traceability

---

## 🧑‍💻 Author

**DocumentFlow Backend Project**
* Developed by: Rufan Niyazli
* GitHub: [https://github.com/yourusername/documentflow](https://github.com/yourusername/documentflow)
## License

This project is licensed under the [MIT License](LICENSE).

---

## 🏁 Summary

> DocumentFlow transforms traditional manual approval processes into a **fully automated, auditable, and secure workflow** — built on the powerful **Spring Integration** framework.



