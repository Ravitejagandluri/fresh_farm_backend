# Postman: Farmer Dashboard & Profile – Step-by-Step Test (Register to Logout)

**Base URL:** `http://localhost:8080/api`  
**Prerequisite:** Backend running (`mvnw spring-boot:run` or your IDE), PostgreSQL up with database `fresh_farm`.

---

## Step 1: Register as Farmer

**Purpose:** Create a new farmer account and get JWT token.

| Field    | Value |
|----------|--------|
| **Method** | `POST` |
| **URL**    | `http://localhost:8080/api/auth/signup` |
| **Headers** | `Content-Type` = `application/json` |

**Body (raw JSON):**
```json
{
  "name": "Ramesh Kumar",
  "email": "farmer1@test.com",
  "password": "farmer123",
  "role": "FARMER",
  "phone": "9876543210"
}
```

**Expected response (200):**
```json
{
  "id": 1,
  "name": "Ramesh Kumar",
  "email": "farmer1@test.com",
  "role": "FARMER",
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

**Action:** Copy the entire `token` value. You will use it in the **Authorization** header for all following steps as:  
`Bearer <paste_token_here>`.

---

## Step 2: Get Profile (Right After Register)

**Purpose:** See profile with only name/email (rest empty).

| Field    | Value |
|----------|--------|
| **Method** | `GET` |
| **URL**    | `http://localhost:8080/api/users/me` |
| **Headers** | `Content-Type` = `application/json`<br>`Authorization` = `Bearer <token_from_step_1>` |

**Expected response (200):**
```json
{
  "id": 1,
  "name": "Ramesh Kumar",
  "email": "farmer1@test.com",
  "phone": "9876543210",
  "farmName": null,
  "location": null,
  "farmingType": null,
  "farmSize": null,
  "irrigation": null,
  "mainCrops": null,
  "harvestFrequency": null,
  "isAvailable": true
}
```

**Note:** `id` is your farmer ID; use it in Step 4 for “farmer dashboard” products.

---

## Step 3: Update Profile (Edit Farm Details)

**Purpose:** Fill in farm details so they show on the profile page.

| Field    | Value |
|----------|--------|
| **Method** | `PUT` |
| **URL**    | `http://localhost:8080/api/users/me` |
| **Headers** | `Content-Type` = `application/json`<br>`Authorization` = `Bearer <token>` |

**Body (raw JSON):**
```json
{
  "phone": "9876543210",
  "farmName": "Green Valley Farm",
  "location": "Bengaluru, Karnataka",
  "farmingType": "Organic",
  "farmSize": "5 – 10 Acres",
  "irrigation": "Drip Irrigation",
  "mainCrops": "Rice, Vegetables",
  "harvestFrequency": "Weekly",
  "isAvailable": true
}
```

**Expected response (200):** Same structure as Step 2, but with all fields filled.

---

## Step 4: Get Profile Again (Verify Updated Details)

**Purpose:** Confirm profile page would show updated farm details.

| Field    | Value |
|----------|--------|
| **Method** | `GET` |
| **URL**    | `http://localhost:8080/api/users/me` |
| **Headers** | `Content-Type` = `application/json`<br>`Authorization` = `Bearer <token>` |

**Expected:** All profile fields (farm name, location, etc.) populated as in Step 3.

---

## Step 5: Add a Product (Farmer Dashboard – Add Product)

**Purpose:** Add a product to the database so it appears on farmer dashboard/inventory.

| Field    | Value |
|----------|--------|
| **Method** | `POST` |
| **URL**    | `http://localhost:8080/api/products/add` |
| **Headers** | `Content-Type` = `application/json`<br>`Authorization` = `Bearer <token>` |

**Body (raw JSON):**
```json
{
  "name": "Organic Rice",
  "description": "Fresh organic rice from local farm.",
  "price": 100,
  "quantity": 500,
  "imageUrl": ""
}
```

**Expected response (200):**
```json
{
  "id": 1,
  "name": "Organic Rice",
  "description": "Fresh organic rice from local farm.",
  "price": 100.0,
  "quantity": 500,
  "imageUrl": "",
  "farmerId": 1,
  "farmerName": "Ramesh Kumar",
  "createdAt": "2026-02-15T10:30:00.123Z"
}
```

---

## Step 6: Get Farmer’s Products (Farmer Dashboard / Inventory)

**Purpose:** Simulate what the farmer dashboard and inventory screens load.

| Field    | Value |
|----------|--------|
| **Method** | `GET` |
| **URL**    | `http://localhost:8080/api/products/farmer/1` |
| **Headers** | `Content-Type` = `application/json`<br>`Authorization` = `Bearer <token>` |

Replace `1` with your farmer `id` from Step 1 or Step 2.

**Expected response (200):** Array of products, e.g. the one added in Step 5.

---

## Step 7: (Optional) Login Again – Get New Token

**Purpose:** Test login flow; use this token for further steps if you didn’t save the first one.

| Field    | Value |
|----------|--------|
| **Method** | `POST` |
| **URL**    | `http://localhost:8080/api/auth/login` |
| **Headers** | `Content-Type` = `application/json` |

**Body (raw JSON):**
```json
{
  "email": "farmer1@test.com",
  "password": "farmer123"
}
```

**Expected response (200):** Same shape as Step 1 (id, name, email, role, token). Use the new `token` for subsequent requests.

---

## Step 8: “Logout” (Verify Token No Longer Used)

**Purpose:** Backend is stateless (JWT); “logout” is done on the client by discarding the token. Here you verify that **without** a valid token, protected APIs fail.

**Option A – Omit Authorization header**

- **Request:** `GET` `http://localhost:8080/api/users/me`  
- **Headers:** Only `Content-Type: application/json` (no `Authorization`).  
- **Expected:** `401 Unauthorized` (or empty/error body).

**Option B – Wrong or expired token**

- **Request:** Same URL.  
- **Headers:** `Authorization` = `Bearer invalid_or_expired_token`.  
- **Expected:** `401` or `403`.

After this, the farmer is effectively “logged out” for that client (e.g. Postman) because the token is not sent or is invalid.

---

## Quick Checklist (Order of Calls)

| # | Action              | Method | Endpoint                  | Auth required |
|---|---------------------|--------|---------------------------|----------------|
| 1 | Register farmer     | POST   | `/api/auth/signup`        | No             |
| 2 | Get profile         | GET    | `/api/users/me`           | Yes (Bearer)   |
| 3 | Update profile      | PUT    | `/api/users/me`           | Yes (Bearer)   |
| 4 | Get profile again   | GET    | `/api/users/me`           | Yes (Bearer)   |
| 5 | Add product         | POST   | `/api/products/add`       | Yes (Bearer)   |
| 6 | Get my products     | GET    | `/api/products/farmer/{id}` | Yes (Bearer) |
| 7 | Login (optional)    | POST   | `/api/auth/login`         | No             |
| 8 | Logout test         | GET    | `/api/users/me` (no token) | No token      |

---

## Postman Tips

1. **Environment variable for token**  
   - After Step 1 or 7, set `token` = response’s `token`.  
   - In **Authorization** tab, Type = **Bearer Token**, Token = `{{token}}`.

2. **Environment variable for base URL**  
   - e.g. `baseUrl` = `http://localhost:8080/api`.  
   - URL = `{{baseUrl}}/auth/signup`, `{{baseUrl}}/users/me`, etc.

3. **Farmer ID**  
   - Save `id` from signup/login or GET profile (e.g. as `farmerId`) and use `{{baseUrl}}/products/farmer/{{farmerId}}` for Step 6.

If any step returns 401, check that the **Authorization** header is set to `Bearer <your_token>` and the token is from a successful signup or login.
