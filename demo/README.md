# 🏋️ GymPro — Gym Management System

A full-featured Spring Boot MVC web application for managing a gym's members,
memberships, classes, bookings, and attendance.

---

## 🚀 How to Run

### Option 1 — Run the JAR directly (easiest)
```bash
java -jar gym-management.jar
```
Then open: http://localhost:8080

### Option 2 — Run from source with Maven
```bash
cd demo
./mvnw spring-boot:run
```

---

## 🔑 H2 Database Console
While the app is running, access the in-memory DB at:
- URL: http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:mem:gymdb`
- Username: `sa`
- Password: *(leave blank)*

---

## 📦 Project Structure

```
src/main/java/com/example/demo/
├── GymManagementApplication.java        ← Main entry point (@EnableScheduling)
│
├── model/                               ← JPA Entities
│   ├── Member.java
│   ├── MembershipPlan.java
│   ├── MembershipSubscription.java
│   ├── MemberStatusHistory.java
│   ├── GymClass.java
│   ├── ClassBooking.java
│   ├── AttendanceRecord.java
│   └── enums/
│       ├── AccountStatus.java           ← ACTIVE, INACTIVE, SUSPENDED
│       ├── MembershipStatus.java        ← ACTIVE, EXPIRED, SUSPENDED
│       ├── MembershipDuration.java      ← MONTHLY(1), QUARTERLY(3), YEARLY(12)
│       └── PaymentStatus.java           ← PAID, UNPAID, PENDING
│
├── dto/                                 ← Form/Request data transfer objects
│   ├── MemberRegistrationDto.java
│   ├── MemberUpdateDto.java
│   ├── MembershipPlanDto.java
│   ├── SubscriptionDto.java
│   ├── GymClassDto.java
│   └── AttendanceDto.java
│
├── repository/                          ← Spring Data JPA interfaces
│   ├── MemberRepository.java
│   ├── MembershipPlanRepository.java
│   ├── MembershipSubscriptionRepository.java
│   ├── MemberStatusHistoryRepository.java
│   ├── GymClassRepository.java
│   ├── ClassBookingRepository.java
│   └── AttendanceRecordRepository.java
│
├── service/                             ← Business logic interfaces
│   ├── MemberService.java
│   ├── MembershipPlanService.java
│   ├── SubscriptionService.java
│   ├── GymClassService.java
│   ├── BookingService.java
│   └── AttendanceService.java
│
├── service/impl/                        ← Business logic implementations
│   ├── MemberServiceImpl.java
│   ├── MembershipPlanServiceImpl.java
│   ├── SubscriptionServiceImpl.java
│   ├── GymClassServiceImpl.java
│   ├── BookingServiceImpl.java
│   └── AttendanceServiceImpl.java
│
├── controller/                          ← Spring MVC @Controller classes
│   ├── HomeController.java              ← / (dashboard)
│   ├── MemberController.java            ← /members/**
│   ├── MembershipController.java        ← /memberships/**
│   ├── SubscriptionController.java      ← /subscriptions/**
│   ├── GymClassController.java          ← /classes/**
│   └── AttendanceController.java        ← /attendance/**
│
├── scheduler/
│   └── SubscriptionExpirationScheduler.java  ← Runs at midnight daily
│
└── config/
    └── DataSeeder.java                  ← Seeds sample data on startup

src/main/resources/
├── application.properties
├── static/css/style.css                 ← Full dark industrial stylesheet
└── templates/                           ← Thymeleaf HTML views
    ├── fragments/layout.html            ← Sidebar + head + alerts fragments
    ├── index.html                       ← Dashboard
    ├── members/
    │   ├── register.html
    │   ├── search.html
    │   ├── profile.html
    │   ├── update.html
    │   └── status.html
    ├── memberships/
    │   ├── plans.html
    │   └── edit-plan.html
    ├── subscriptions/
    │   ├── assign.html
    │   └── renew.html
    ├── classes/
    │   ├── list.html
    │   ├── create.html
    │   ├── update.html
    │   └── participants.html
    └── attendance/
        └── mark.html
```

---

## 🗺️ URL Routes

| URL | Description |
|-----|-------------|
| `/` | Dashboard with live stats |
| `/members/register` | Register a new member (FR-1) |
| `/members/search` | Search all members (FR-3) |
| `/members/profile/{id}` | View full member profile |
| `/members/update/{id}` | Edit member info + status (FR-2) |
| `/members/status` | Member status overview (FR-4) |
| `/memberships/plans` | Create & list plans (FR-5) |
| `/memberships/plans/edit/{id}` | Edit a plan |
| `/subscriptions/assign` | Assign plan to member (FR-6) |
| `/subscriptions/renew/{memberId}` | Renew a subscription (FR-7) |
| `/classes` | View all classes + quick-book (FR-10) |
| `/classes/create` | Create a class (FR-8) |
| `/classes/update/{id}` | Edit a class (FR-9) |
| `/classes/participants/{id}` | View class participants (FR-11) |
| `/attendance/mark` | Mark daily attendance (FR-12) |
| `/h2-console` | In-memory database browser |

---

## ⚙️ Functional Requirements Covered

| ID | Requirement | Status |
|----|-------------|--------|
| FR-1 | Member Registration | ✅ |
| FR-2 | Update Member + Status History | ✅ |
| FR-3 | View Profile / Search | ✅ |
| FR-4 | Membership Status Display | ✅ |
| FR-5 | Create Membership Plans | ✅ |
| FR-6 | Assign Subscription | ✅ |
| FR-7 | Subscription Renewal | ✅ |
| FR-8 | Create Class | ✅ |
| FR-9 | Update / Cancel Class | ✅ |
| FR-10 | Class Booking (with capacity check) | ✅ |
| FR-11 | View Class Participants | ✅ |
| FR-12 | Daily Attendance Marking | ✅ |
| Auto | Daily expiration scheduler (@Scheduled) | ✅ |

---

## 🛠️ Tech Stack

- **Java 21** + **Spring Boot 3.2**
- **Spring MVC** — @Controller, @RestController
- **Spring Data JPA** + **Hibernate** — ORM
- **H2** — In-memory database (swap for MySQL/PostgreSQL easily)
- **Thymeleaf** — Server-side HTML templates
- **Bean Validation** — @Valid, @NotBlank, @Email, @Past, etc.
- **Lombok** — @Data, @Builder, @RequiredArgsConstructor
- **Spring Scheduling** — @Scheduled for nightly expiration check

---

## 🔄 Switch to MySQL (Production)

1. Replace H2 dependency in `pom.xml` with:
```xml
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
</dependency>
```

2. Update `application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/gymdb
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
spring.jpa.hibernate.ddl-auto=update
```

---

## 📊 Sample Data (Auto-seeded on startup)

- 3 Membership Plans: Monthly ($49.99), Quarterly ($129.99), Annual ($399.99)
- 4 Members with generated IDs (GYM-00001 … GYM-00004)
- Active subscriptions assigned to members
- 4 Upcoming gym classes
- 2 attendance records for today
