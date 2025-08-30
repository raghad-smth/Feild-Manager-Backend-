# Fields Manager Backend

🏟️ **Fields Manager** is a comprehensive sports field booking platform that enables players to book fields, manage teams, leave reviews, and allows administrators to manage the entire system (This project was done during EgronX intern).


### Setup
1. **Clone the repository**
2. **Configure PostgreSQL database** (see Database Setup below)
3. **Run the application:**
   ```bash
   mvn spring-boot:run
   ```
4. **Test the health endpoint:**
   ```bash
   curl http://localhost:8080/api/test/health
   ```

## 📖 **API Documentation**

### 🎯 **For complete API documentation with examples, see:**
### **[📋 Api.md](./feilds/Api.md)**


## 🏗️ Architecture

### Core Features
- **🔐 Authentication System** - Sign up, sign in, password management
- **👥 Team Management** - Create, join, leave teams
- **⚽ Field Management** - Browse and book sports fields  
- **📅 Booking System** - Request, track, and manage bookings
- **⭐ Review System** - Rate and review field experiences
- **❓ Enquiry Management** - Customer support system
- **⚙️ Settings Management** - Platform configuration
- **👨‍💼 Admin Panel** - Complete administrative control

### Technology Stack
- **Backend:** Spring Boot 3.x
- **Database:** PostgreSQL
- **ORM:** Spring Data JPA / Hibernate
- **Build Tool:** Maven
