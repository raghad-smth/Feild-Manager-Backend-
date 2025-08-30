# Fields Manager API Documentation

## Overview
Fields Manager is a comprehensive sports field booking platform that provides APIs for players to book fields, manage teams, and leave reviews, while allowing administrators to manage the entire system.

**Base URL:** `http://localhost:8080`

## Authentication
Most endpoints require authentication via headers:
- `X-User-ID`: The ID of the authenticated user
- For admin endpoints, the user must have admin role

---

## 🔐 Authentication APIs

### Sign In
**POST** `/api/auth/signin`

Authenticate a user with email and password.

**Request Body:**
```json
{
  "email": "player@example.com",
  "password": "password123"
}
```

**Response (200):**
```json
{
  "success": true,
  "message": "Sign in successful",
  "user": {
    "id": 1,
    "name": "John Doe",
    "email": "player@example.com",
    "phone": "1234567890",
    "role": "player"
  }
}
```

**Response (401):**
```json
{
  "success": false,
  "message": "Invalid email or password"
}
```

### Sign Up
**POST** `/api/auth/signup`

Register a new user account.

**Request Body:**
```json
{
  "name": "John Doe",
  "email": "newplayer@example.com",
  "phone": "1234567890",
  "password": "password123"
}
```

**Response (201):**
```json
{
  "success": true,
  "message": "Account created successfully",
  "user": {
    "id": 2,
    "name": "John Doe",
    "email": "newplayer@example.com",
    "phone": "1234567890",
    "role": "player"
  }
}
```

### Logout
**POST** `/api/auth/logout`

**Response (200):**
```json
{
  "success": true,
  "message": "Logged out successfully"
}
```

### Reset Password
**POST** `/api/auth/reset-password`

**Request Body:**
```json
{
  "email": "player@example.com"
}
```

**Response (200):**
```json
{
  "success": true,
  "message": "Password reset instructions sent to your email"
}
```

### Change Password
**PUT** `/api/auth/change-password`

**Headers:**
- `X-User-ID: 1`

**Request Body:**
```json
{
  "oldPassword": "oldpass123",
  "newPassword": "newpass123"
}
```

**Response (200):**
```json
{
  "success": true,
  "message": "Password changed successfully"
}
```

---

## 👤 User Management APIs

### Get User Profile
**GET** `/api/users/{userId}`

**Headers:**
- `X-User-ID: 1`

**Response (200):**
```json
{
  "id": 1,
  "name": "John Doe",
  "email": "player@example.com",
  "phone": "1234567890",
  "role": "player"
}
```

### Check Email Existence
**GET** `/api/users/check-email?email=test@example.com`

**Response (200):**
```json
{
  "exists": true
}
```

### Update Profile
**PUT** `/api/users/{userId}/profile`

**Headers:**
- `X-User-ID: 1`

**Request Body (Form Data):**
```
name=John Updated
phone=9876543210
password=newpassword123
```

**Response (200):**
```json
{
  "success": true,
  "message": "Profile updated successfully",
  "user": {
    "id": 1,
    "name": "John Updated",
    "email": "player@example.com",
    "phone": "9876543210",
    "role": "player"
  }
}
```

---

## 👥 Team Management APIs

### Create Team
**POST** `/api/teams`

**Headers:**
- `X-User-ID: 1`

**Request Body:**
```json
{
  "name": "Lightning Bolts"
}
```

**Response (201):**
```json
{
  "success": true,
  "message": "Team created successfully",
  "data": {
    "id": 1,
    "name": "Lightning Bolts",
    "isActive": true
  }
}
```

### Get All Teams
**GET** `/api/teams`

**Response (200):**
```json
{
  "success": true,
  "message": "Teams retrieved successfully",
  "data": [
    {
      "id": 1,
      "name": "Lightning Bolts",
      "isActive": true
    },
    {
      "id": 2,
      "name": "Thunder Hawks",
      "isActive": true
    }
  ]
}
```

### Get Teams for Player
**GET** `/api/teams/player/{playerId}`

**Headers:**
- `X-User-ID: 1`

**Response (200):**
```json
{
  "success": true,
  "message": "Player teams retrieved successfully",
  "data": [
    {
      "id": 1,
      "name": "Lightning Bolts",
      "isActive": true,
      "isAdmin": true
    }
  ]
}
```

### Get Team Details
**GET** `/api/teams/{teamId}`

**Headers:**
- `X-User-ID: 1`

**Response (200):**
```json
{
  "success": true,
  "message": "Team details retrieved successfully",
  "data": {
    "id": 1,
    "name": "Lightning Bolts",
    "isActive": true,
    "members": [
      {
        "id": 1,
        "player": {
          "id": 1,
          "name": "John Doe",
          "email": "player@example.com"
        },
        "isAdmin": true,
        "isActive": true
      }
    ]
  }
}
```

### Join Team
**POST** `/api/teams/{teamId}/join`

**Headers:**
- `X-User-ID: 2`

**Response (200):**
```json
{
  "success": true,
  "message": "Successfully joined the team"
}
```

### Leave Team
**DELETE** `/api/teams/{teamId}/leave`

**Headers:**
- `X-User-ID: 2`

**Response (200):**
```json
{
  "success": true,
  "message": "Successfully left the team"
}
```

---

## ⚽ Field Management APIs

### Browse All Fields
**GET** `/api/fields?page=0&size=10`

**Response (200):**
```json
{
  "success": true,
  "message": "Fields retrieved successfully",
  "data": {
    "content": [
      {
        "id": 1,
        "name": "Main Soccer Field",
        "images": "field1.jpg,field1_2.jpg",
        "playersCapacity": 22,
        "locationAddress": "123 Sports Complex, City Center",
        "isActive": true
      }
    ],
    "totalElements": 5,
    "totalPages": 1,
    "size": 10,
    "number": 0
  }
}
```

### Get Available Fields
**GET** `/api/fields/available`

**Response (200):**
```json
{
  "success": true,
  "message": "Available fields retrieved successfully",
  "data": [
    {
      "id": 1,
      "name": "Main Soccer Field",
      "images": "field1.jpg",
      "playersCapacity": 22,
      "locationAddress": "123 Sports Complex",
      "isActive": true,
      "availableSlots": [
        {
          "id": 1,
          "weekDay": {"id": 1, "name": "Monday"},
          "fromTime": "09:00:00",
          "toTime": "11:00:00",
          "price": 50.00
        }
      ]
    }
  ]
}
```

### Get Field Details
**GET** `/api/fields/{fieldId}`

**Response (200):**
```json
{
  "id": 1,
  "name": "Main Soccer Field",
  "images": "field1.jpg,field1_2.jpg",
  "playersCapacity": 22,
  "locationAddress": "123 Sports Complex, City Center",
  "isActive": true
}
```

### Create Field (Admin)
**POST** `/api/fields`

**Headers:**
- `X-User-ID: 1` (Admin user)

**Request Body:**
```json
{
  "name": "New Basketball Court",
  "images": "court1.jpg,court2.jpg",
  "playersCapacity": 10,
  "locationAddress": "456 Sports Arena, Downtown",
  "isActive": true
}
```

**Response (201):**
```json
{
  "success": true,
  "message": "Field created successfully",
  "data": {
    "id": 2,
    "name": "New Basketball Court",
    "images": "court1.jpg,court2.jpg",
    "playersCapacity": 10,
    "locationAddress": "456 Sports Arena, Downtown",
    "isActive": true
  }
}
```

### Update Field (Admin)
**PUT** `/api/fields/{fieldId}`

**Headers:**
- `X-User-ID: 1` (Admin user)

**Request Body:**
```json
{
  "name": "Updated Soccer Field",
  "playersCapacity": 24,
  "isActive": true
}
```

### Delete Field (Admin)
**DELETE** `/api/fields/{fieldId}`

**Headers:**
- `X-User-ID: 1` (Admin user)

**Response (200):**
```json
{
  "success": true,
  "message": "Field deleted successfully"
}
```

---

## ⏰ Field Time Slot APIs

### Get Field Slots
**GET** `/api/field-slots/field/{fieldId}`

**Response (200):**
```json
{
  "success": true,
  "message": "Field slots retrieved successfully",
  "data": [
    {
      "id": 1,
      "field": {
        "id": 1,
        "name": "Main Soccer Field"
      },
      "weekDay": {
        "id": 1,
        "name": "Monday"
      },
      "fromTime": "09:00:00",
      "toTime": "11:00:00",
      "price": 50.00
    }
  ]
}
```

### Get Slots by Day
**GET** `/api/field-slots/field/{fieldId}/day/{dayName}`

**Example:** `/api/field-slots/field/1/day/Monday`

**Response (200):**
```json
{
  "success": true,
  "message": "Field slots for Monday retrieved successfully",
  "data": [
    {
      "id": 1,
      "fromTime": "09:00:00",
      "toTime": "11:00:00",
      "price": 50.00
    }
  ]
}
```

### Create Time Slot (Admin)
**POST** `/api/field-slots`

**Headers:**
- `X-User-ID: 1` (Admin user)

**Request Body:**
```json
{
  "fieldId": 1,
  "weekDayId": 1,
  "fromTime": "14:00:00",
  "toTime": "16:00:00",
  "price": 60.00
}
```

**Response (201):**
```json
{
  "success": true,
  "message": "Field slot created successfully",
  "data": {
    "id": 2,
    "field": {"id": 1, "name": "Main Soccer Field"},
    "weekDay": {"id": 1, "name": "Monday"},
    "fromTime": "14:00:00",
    "toTime": "16:00:00",
    "price": 60.00
  }
}
```

---

## 📅 Booking Management APIs

### Get All Bookings
**GET** `/api/bookings?page=0&size=10&status=pending`

**Headers:**
- `X-User-ID: 1`

**Response (200):**
```json
{
  "success": true,
  "message": "Bookings retrieved successfully",
  "data": {
    "content": [
      {
        "id": 1,
        "player": {
          "id": 1,
          "name": "John Doe"
        },
        "team": {
          "id": 1,
          "name": "Lightning Bolts"
        },
        "date": "2024-01-15",
        "status": "pending",
        "fieldSlot": {
          "id": 1,
          "field": {"name": "Main Soccer Field"},
          "fromTime": "09:00:00",
          "toTime": "11:00:00"
        },
        "price": 50.00
      }
    ],
    "totalElements": 1,
    "totalPages": 1
  }
}
```

### Get Team Bookings
**GET** `/api/bookings/team/{teamId}`

**Headers:**
- `X-User-ID: 1`

**Response (200):**
```json
{
  "success": true,
  "message": "Team bookings retrieved successfully",
  "data": [
    {
      "id": 1,
      "date": "2024-01-15",
      "status": "confirmed",
      "fieldSlot": {
        "field": {"name": "Main Soccer Field"},
        "fromTime": "09:00:00",
        "toTime": "11:00:00"
      },
      "price": 50.00
    }
  ]
}
```

### Create Booking
**POST** `/api/bookings`

**Headers:**
- `X-User-ID: 1`

**Request Body:**
```json
{
  "teamId": 1,
  "fieldSlotId": 1,
  "date": "2024-01-20"
}
```

**Response (201):**
```json
{
  "success": true,
  "message": "Booking request submitted successfully",
  "data": {
    "id": 2,
    "date": "2024-01-20",
    "status": "pending",
    "price": 50.00
  }
}
```

### Update Booking Status (Admin)
**PUT** `/api/bookings/{bookingId}/status`

**Headers:**
- `X-User-ID: 1` (Admin user)

**Request Body:**
```json
{
  "status": "confirmed",
  "reason": "Payment confirmed"
}
```

**Response (200):**
```json
{
  "success": true,
  "message": "Booking status updated successfully"
}
```

### Submit Review for Booking
**POST** `/api/bookings/{bookingId}/review`

**Headers:**
- `X-User-ID: 1`

**Request Body:**
```json
{
  "rating": 5,
  "comment": "Excellent field condition and great experience!"
}
```

**Response (201):**
```json
{
  "success": true,
  "message": "Review submitted successfully",
  "data": {
    "id": 1,
    "rating": 5,
    "comment": "Excellent field condition and great experience!",
    "createdAt": "2024-01-15T10:30:00"
  }
}
```

---

## ⭐ Reviews Management APIs

### Get All Reviews
**GET** `/api/reviews/visible?page=0&size=10`

**Response (200):**
```json
{
  "success": true,
  "message": "Reviews retrieved successfully",
  "data": {
    "content": [
      {
        "id": 1,
        "booking": {
          "id": 1,
          "fieldSlot": {
            "field": {"name": "Main Soccer Field"}
          }
        },
        "customer": {
          "id": 1,
          "name": "John Doe"
        },
        "rating": 5,
        "comment": "Excellent field condition!",
        "isHidden": false,
        "createdAt": "2024-01-15T10:30:00"
      }
    ],
    "totalElements": 1
  }
}
```

### Get Field Reviews
**GET** `/api/reviews/field/{fieldId}`

**Response (200):**
```json
{
  "success": true,
  "message": "Field reviews retrieved successfully",
  "data": [
    {
      "id": 1,
      "rating": 5,
      "comment": "Great field!",
      "customer": {"name": "John Doe"},
      "createdAt": "2024-01-15T10:30:00"
    }
  ]
}
```

### Get Rating Histogram
**GET** `/api/reviews/rating-histogram`

**Response (200):**
```json
{
  "success": true,
  "message": "Rating distribution retrieved successfully",
  "data": [
    {"rating": 5, "count": 10},
    {"rating": 4, "count": 5},
    {"rating": 3, "count": 2},
    {"rating": 2, "count": 1},
    {"rating": 1, "count": 0}
  ]
}
```

---

## 👥 Player Management APIs

### Activate Player in Team (Admin Only)
**PUT** `/api/players/team/{teamId}/player/{playerId}/activate`

**Headers:**
- `Admin-Id: {requestingUserId}` (Team admin user ID)

**Example:** `/api/players/team/1/player/2/activate`

**Response (200):**
```json
{
  "message": "Player activated successfully",
  "player": {
    "id": 1,
    "player": {"id": 2, "name": "John Doe"},
    "team": {"id": 1, "name": "Lightning Bolts"},
    "isAdmin": false,
    "isActive": true
  },
  "teamId": 1,
  "playerId": 2
}
```

### Deactivate Player in Team (Admin Only)
**PUT** `/api/players/team/{teamId}/player/{playerId}/deactivate`

**Headers:**
- `Admin-Id: {requestingUserId}` (Team admin user ID)

**Example:** `/api/players/team/1/player/2/deactivate`

**Response (200):**
```json
{
  "message": "Player deactivated successfully",
  "player": {
    "id": 1,
    "isActive": false
  },
  "teamId": 1,
  "playerId": 2
}
```

### Get Player Status in Team
**GET** `/api/players/team/{teamId}/player/{playerId}/status`

**Response (200):**
```json
{
  "playerId": 2,
  "teamId": 1,
  "isActive": true,
  "isAdmin": false,
  "playerDetails": {
    "id": 1,
    "player": {"id": 2, "name": "John Doe"},
    "team": {"id": 1, "name": "Lightning Bolts"},
    "isAdmin": false,
    "isActive": true
  }
}
```

### Get Player's Teams
**GET** `/api/players/player/{playerId}/teams`

**Response (200):**
```json
{
  "playerId": 2,
  "totalTeams": 2,
  "memberships": [
    {
      "id": 1,
      "team": {"id": 1, "name": "Lightning Bolts"},
      "isAdmin": true,
      "isActive": true
    },
    {
      "id": 2,
      "team": {"id": 2, "name": "Thunder Hawks"},
      "isAdmin": false,
      "isActive": false
    }
  ]
}
```

### Check Player Admin Status
**GET** `/api/players/team/{teamId}/player/{playerId}/is-admin`

**Response (200):**
```json
{
  "playerId": 2,
  "teamId": 1,
  "isAdminOfThisTeam": true,
  "isAdminOfAnyTeam": true
}
```

### Get Team Players
**GET** `/api/players/team/{teamId}/players?activeOnly=true`

**Query Parameters:**
- `activeOnly`: boolean (default: false) - If true, returns only active players

**Response (200):**
```json
{
  "teamId": 1,
  "activeOnly": true,
  "totalPlayers": 3,
  "players": [
    {
      "id": 1,
      "player": {"id": 1, "name": "Team Captain"},
      "isAdmin": true,
      "isActive": true
    },
    {
      "id": 2,
      "player": {"id": 2, "name": "Player Two"},
      "isAdmin": false,
      "isActive": true
    }
  ]
}
```

---

## 👨‍💼 Admin Review Management APIs

### Get Admin Reviews
**GET** `/api/admin/reviews?page=0&size=10&status=visible`

**Headers:**
- `X-User-ID: 1` (Admin user)

**Response (200):**
```json
{
  "success": true,
  "message": "Reviews retrieved successfully",
  "data": {
    "content": [
      {
        "id": 1,
        "rating": 5,
        "comment": "Excellent field!",
        "customer": {"name": "John Doe"},
        "isHidden": false,
        "createdAt": "2024-01-15T10:30:00"
      }
    ],
    "totalElements": 1
  }
}
```

### Get Review Statistics
**GET** `/api/admin/reviews/statistics`

**Headers:**
- `X-User-ID: 1` (Admin user)

**Response (200):**
```json
{
  "success": true,
  "message": "Review statistics retrieved successfully",
  "data": {
    "totalReviews": 15,
    "visibleReviews": 14,
    "hiddenReviews": 1,
    "hiddenPercentage": 6.67
  }
}
```

### Bulk Hide Reviews
**POST** `/api/admin/reviews/bulk-hide`

**Headers:**
- `X-User-ID: 1` (Admin user)

**Request Body:**
```json
{
  "reviewIds": [1, 2, 3]
}
```

**Response (200):**
```json
{
  "success": true,
  "message": "Bulk hide operation completed",
  "data": {
    "successCount": 3,
    "failCount": 0,
    "message": "Bulk hide completed. Success: 3, Failed: 0"
  }
}
```

### Bulk Delete Reviews
**POST** `/api/admin/reviews/bulk-delete`

**Headers:**
- `X-User-ID: 1` (Admin user)

**Request Body:**
```json
{
  "reviewIds": [1, 2]
}
```

**Response (200):**
```json
{
  "success": true,
  "message": "Bulk delete operation completed",
  "data": {
    "successCount": 2,
    "failCount": 0,
    "message": "Bulk delete completed. Success: 2, Failed: 0"
  }
}
```

---

## ❓ Enquiry Management APIs

### Submit Enquiry
**POST** `/api/enquiries`

**Headers:**
- `X-User-ID: 1`

**Request Body:**
```json
{
  "content": "I would like to know about group booking discounts for tournaments."
}
```

**Response (201):**
```json
{
  "success": true,
  "message": "Enquiry submitted successfully",
  "data": {
    "id": 1,
    "content": "I would like to know about group booking discounts for tournaments.",
    "status": "open",
    "isHidden": false,
    "createdAt": "2024-01-15T10:30:00"
  }
}
```

### Get All Enquiries (Admin)
**GET** `/api/enquiries?page=0&size=10&status=open`

**Headers:**
- `X-User-ID: 1` (Admin user)

**Response (200):**
```json
{
  "success": true,
  "message": "Enquiries retrieved successfully",
  "data": {
    "content": [
      {
        "id": 1,
        "content": "Question about booking policies",
        "customer": {"name": "John Doe"},
        "status": "open",
        "adminResponse": null,
        "isHidden": false,
        "createdAt": "2024-01-15T10:30:00"
      }
    ],
    "totalElements": 1
  }
}
```

### Get My Enquiries
**GET** `/api/enquiries/my`

**Headers:**
- `X-User-ID: 1`

**Response (200):**
```json
{
  "success": true,
  "message": "Your enquiries retrieved successfully",
  "data": [
    {
      "id": 1,
      "content": "My question about booking",
      "status": "open",
      "adminResponse": null,
      "createdAt": "2024-01-15T10:30:00"
    }
  ]
}
```

### Respond to Enquiry (Admin)
**PUT** `/api/enquiries/{enquiryId}/respond`

**Headers:**
- `X-User-ID: 1` (Admin user)

**Request Body:**
```json
{
  "response": "Thank you for your enquiry. We offer 10% discount for group bookings of 5+ teams."
}
```

**Response (200):**
```json
{
  "success": true,
  "message": "Response added successfully"
}
```

### Update Enquiry Status (Admin)
**PUT** `/api/enquiries/{enquiryId}/status`

**Headers:**
- `X-User-ID: 1` (Admin user)

**Request Body:**
```json
{
  "status": "closed"
}
```

---

## ⚙️ Settings Management APIs

### Get Public Settings
**GET** `/api/settings`

**Response (200):**
```json
{
  "success": true,
  "message": "Settings retrieved successfully",
  "data": {
    "name": "Fields Manager",
    "logoUrl": "https://example.com/logo.png",
    "aboutImageUrl": "https://example.com/about.jpg",
    "aboutDescription": "Welcome to Fields Manager - your sports field booking platform",
    "termsAndConditions": "Terms and conditions...",
    "facebookUrl": "https://facebook.com/fieldsmanager",
    "whatsappNumber": "+1234567890",
    "phoneNumber": "+1234567890",
    "secondPhoneNumber": "+0987654321"
  }
}
```

### Get About Information
**GET** `/api/settings/about`

**Response (200):**
```json
{
  "success": true,
  "message": "About information retrieved successfully",
  "data": {
    "aboutImageUrl": "https://example.com/about.jpg",
    "aboutDescription": "Welcome to Fields Manager - your sports field booking platform"
  }
}
```

### Get Contact Information
**GET** `/api/settings/contact`

**Response (200):**
```json
{
  "success": true,
  "message": "Contact information retrieved successfully",
  "data": {
    "facebookUrl": "https://facebook.com/fieldsmanager",
    "whatsappNumber": "+1234567890",
    "phoneNumber": "+1234567890",
    "secondPhoneNumber": "+0987654321"
  }
}
```

### Update Settings (Admin)
**PUT** `/api/settings`

**Headers:**
- `X-User-ID: 1` (Admin user)

**Request Body:**
```json
{
  "name": "Updated Fields Manager",
  "logoUrl": "https://example.com/new-logo.png",
  "aboutDescription": "Updated description",
  "phoneNumber": "+1111111111"
}
```

**Response (200):**
```json
{
  "success": true,
  "message": "Settings updated successfully",
  "data": {
    "preview": {
      "name": "Updated Fields Manager",
      "logoUrl": "https://example.com/new-logo.png"
    },
    "changes": ["name", "logoUrl"]
  }
}
```

---

## 🔧 Utility APIs

### Health Check
**GET** `/api/test/health`

**Response (200):**
```json
{
  "status": "UP",
  "message": "Application is healthy"
}
```

### Database Connection Test
**GET** `/api/test/db`

**Response (200):**
```json
{
  "status": "Connected",
  "message": "Database connection is working"
}
```

---

## Error Responses

### Common Error Codes

**400 Bad Request:**
```json
{
  "success": false,
  "message": "Invalid request parameters"
}
```

**401 Unauthorized:**
```json
{
  "success": false,
  "message": "Authentication required"
}
```

**403 Forbidden:**
```json
{
  "success": false,
  "message": "Access denied. Admin role required."
}
```

**404 Not Found:**
```json
{
  "success": false,
  "message": "Resource not found"
}
```

**500 Internal Server Error:**
```json
{
  "success": false,
  "message": "An unexpected error occurred. Please try again later."
}
```


---




