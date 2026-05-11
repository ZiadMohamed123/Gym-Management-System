# Gym Management System MVC Structure

This document explains the project structure, how the layers communicate, and how to implement the missing code.

## 1) Structure overview

The project follows a standard Spring MVC layout:

- controller: Handles HTTP requests and returns views or JSON.
- service: Holds business logic.
- repository: Data access layer.
- model: Domain entities and enums.
- dto: Request and response payload models.
- resources/templates: Thymeleaf views.

## 2) Folder layout

- src/main/java/com/example/demo/controller
  - Web controllers for pages (Thymeleaf views).
  - A separate REST controller exists for JSON APIs.
- src/main/java/com/example/demo/service
  - Service interfaces (business logic contracts).
- src/main/java/com/example/demo/repository
  - Repository interfaces (data access contracts).
- src/main/java/com/example/demo/model
  - Domain models and enums (Member, MembershipPlan, GymClass, etc.).
- src/main/java/com/example/demo/dto
  - Request objects for forms and APIs.
- src/main/resources/templates
  - Thymeleaf HTML pages.

## 3) How the layers communicate

Typical MVC flow:

1) A browser calls a controller route, for example GET /members/register.
2) The controller returns the view name (members/register).
3) Thymeleaf renders the corresponding HTML page.

When a form is submitted:

1) Controller receives form data as a DTO.
2) Controller calls a service method.
3) Service validates rules and calls repository methods.
4) Repository reads/writes to the database.
5) Service returns results to the controller.
6) Controller returns a view (or JSON for REST endpoints).

## 4) Current view mappings

These controllers already map to views:

- / -> index
- /members/register -> members/register
- /members/update -> members/update
- /members/profile -> members/profile
- /members/search -> members/search
- /members/status -> members/status
- /memberships/plans -> memberships/plans
- /subscriptions/assign -> subscriptions/assign
- /subscriptions/renew -> subscriptions/renew
- /classes -> classes/list
- /classes/create -> classes/create
- /classes/update -> classes/update
- /classes/participants -> classes/participants
- /attendance/mark -> attendance/mark

## 5) How to implement the code

Start from the requirements and implement in this order:

### Step 1: Define data models

Implement the model classes with fields and relationships. Examples:

- Member: id, fullName, phoneNumber, email, dateOfBirth, address, accountStatus
- MembershipPlan: id, status, duration, price
- MembershipSubscription: id, memberId, planId, startDate, endDate, paymentStatus
- GymClass: id, name, description, scheduleDateTime, maxCapacity
- ClassBooking: id, classId, memberId, bookedAt
- AttendanceRecord: id, memberId, date, time
- MemberStatusHistory: id, memberId, oldStatus, newStatus, changedAt

### Step 2: Add persistence

Add JPA and database configuration:

- Add spring-boot-starter-data-jpa and database driver.
- Annotate models with @Entity.
- Use @Id and @GeneratedValue for primary keys.
- Create repository interfaces extending JpaRepository.

### Step 3: Implement service logic

Implement each service with business rules:

- MemberService: register, update, search, getProfile
- MembershipPlanService: createPlan, listPlans, updatePlan
- SubscriptionService: assignPlan, renewSubscription, checkExpiration
- GymClassService: createClass, updateClass, cancelClass
- BookingService: bookClass, listParticipants
- AttendanceService: markAttendance, listAttendance
- NotificationService: notifyClassCancellation

### Step 4: Wire controllers

Update controllers to call services and return real data:

- For views, return a ModelAndView or Model attributes.
- For form submissions, accept DTOs and redirect on success.

### Step 5: Add validation

Use validation annotations in DTOs and handle errors:

- @NotBlank, @Email, @Past, @Size, @Min, etc.
- In controllers, use @Valid and BindingResult.

### Step 6: Implement membership expiration

Add a scheduled task:

- Use @Scheduled to check subscriptions daily.
- Update MembershipStatus to EXPIRED when endDate < today.

## 6) Suggested implementation order

1) Member registration and profile (FR-1, FR-2, FR-3)
2) Membership plans and subscriptions (FR-5, FR-6, FR-7)
3) Classes and booking (FR-8, FR-9, FR-10, FR-11)
4) Attendance (FR-12)

## 7) Notes

- Controllers for HTML pages should use @Controller, not @RestController.
- Keep REST APIs under a separate /api path if you want JSON access too.
- Views go in resources/templates and use names like members/register.
