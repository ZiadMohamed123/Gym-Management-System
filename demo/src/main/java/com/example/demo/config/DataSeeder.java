package com.example.demo.config;

import com.example.demo.dto.*;
import com.example.demo.model.GymClass;
import com.example.demo.model.Member;
import com.example.demo.model.MembershipPlan;
import com.example.demo.model.enums.*;
import com.example.demo.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {

    private final MemberService memberService;
    private final MembershipPlanService planService;
    private final SubscriptionService subscriptionService;
    private final GymClassService gymClassService;
    private final AttendanceService attendanceService;

    @Override
    public void run(String... args) throws Exception {
        log.info("Seeding initial data...");

        // ---- Membership Plans ----
        MembershipPlanDto monthly = new MembershipPlanDto();
        monthly.setName("Monthly Basic");
        monthly.setDescription("Access to all gym equipment. Perfect for short-term commitment.");
        monthly.setDuration(MembershipDuration.MONTHLY);
        monthly.setPrice(new BigDecimal("49.99"));
        monthly.setStatus(MembershipStatus.ACTIVE);
        MembershipPlan plan1 = planService.create(monthly);

        MembershipPlanDto quarterly = new MembershipPlanDto();
        quarterly.setName("Quarterly Pro");
        quarterly.setDescription("Full access including group classes. Best value for 3 months.");
        quarterly.setDuration(MembershipDuration.QUARTERLY);
        quarterly.setPrice(new BigDecimal("129.99"));
        quarterly.setStatus(MembershipStatus.ACTIVE);
        MembershipPlan plan2 = planService.create(quarterly);

        MembershipPlanDto yearly = new MembershipPlanDto();
        yearly.setName("Annual Elite");
        yearly.setDescription("Unlimited access to all facilities, classes, and personal trainer sessions.");
        yearly.setDuration(MembershipDuration.YEARLY);
        yearly.setPrice(new BigDecimal("399.99"));
        yearly.setStatus(MembershipStatus.ACTIVE);
        MembershipPlan plan3 = planService.create(yearly);

        // ---- Members ----
        MemberRegistrationDto m1 = new MemberRegistrationDto();
        m1.setFullName("John Smith");
        m1.setEmail("john.smith@email.com");
        m1.setPhoneNumber("01012345678");
        m1.setDateOfBirth(LocalDate.of(1990, 5, 15));
        m1.setAddress("123 Main Street, Cairo");
        Member member1 = memberService.register(m1);

        MemberRegistrationDto m2 = new MemberRegistrationDto();
        m2.setFullName("Sarah Johnson");
        m2.setEmail("sarah.j@email.com");
        m2.setPhoneNumber("01087654321");
        m2.setDateOfBirth(LocalDate.of(1995, 8, 22));
        m2.setAddress("456 Nile Ave, Giza");
        Member member2 = memberService.register(m2);

        MemberRegistrationDto m3 = new MemberRegistrationDto();
        m3.setFullName("Ahmed Hassan");
        m3.setEmail("ahmed.h@email.com");
        m3.setPhoneNumber("01123456789");
        m3.setDateOfBirth(LocalDate.of(1988, 3, 10));
        m3.setAddress("789 Tahrir Square, Cairo");
        Member member3 = memberService.register(m3);

        MemberRegistrationDto m4 = new MemberRegistrationDto();
        m4.setFullName("Nour Ali");
        m4.setEmail("nour.ali@email.com");
        m4.setPhoneNumber("01234567890");
        m4.setDateOfBirth(LocalDate.of(2000, 12, 1));
        m4.setAddress("321 Zamalek, Cairo");
        Member member4 = memberService.register(m4);

        // ---- Assign Subscriptions ----
        SubscriptionDto sub1 = new SubscriptionDto();
        sub1.setMemberId(member1.getId());
        sub1.setPlanId(plan3.getId());
        sub1.setStartDate(LocalDate.now().minusMonths(2));
        sub1.setPaymentStatus(PaymentStatus.PAID);
        subscriptionService.assign(sub1);

        SubscriptionDto sub2 = new SubscriptionDto();
        sub2.setMemberId(member2.getId());
        sub2.setPlanId(plan2.getId());
        sub2.setStartDate(LocalDate.now().minusMonths(1));
        sub2.setPaymentStatus(PaymentStatus.PAID);
        subscriptionService.assign(sub2);

        SubscriptionDto sub3 = new SubscriptionDto();
        sub3.setMemberId(member3.getId());
        sub3.setPlanId(plan1.getId());
        sub3.setStartDate(LocalDate.now());
        sub3.setPaymentStatus(PaymentStatus.PENDING);
        subscriptionService.assign(sub3);

        // ---- Gym Classes ----
        GymClassDto c1 = new GymClassDto();
        c1.setName("Morning Yoga");
        c1.setDescription("Start your day with peaceful yoga stretches and breathing exercises.");
        c1.setScheduleDateTime(LocalDateTime.now().plusDays(1).withHour(7).withMinute(0));
        c1.setMaxCapacity(20);
        c1.setCreatedBy("Trainer Maya");
        gymClassService.create(c1);

        GymClassDto c2 = new GymClassDto();
        c2.setName("HIIT Cardio Blast");
        c2.setDescription("High-intensity interval training to burn fat and build endurance.");
        c2.setScheduleDateTime(LocalDateTime.now().plusDays(2).withHour(18).withMinute(0));
        c2.setMaxCapacity(15);
        c2.setCreatedBy("Trainer Carlos");
        gymClassService.create(c2);

        GymClassDto c3 = new GymClassDto();
        c3.setName("Strength & Power");
        c3.setDescription("Build muscle strength through compound lifts and resistance training.");
        c3.setScheduleDateTime(LocalDateTime.now().plusDays(3).withHour(10).withMinute(30));
        c3.setMaxCapacity(10);
        c3.setCreatedBy("Trainer Mike");
        gymClassService.create(c3);

        GymClassDto c4 = new GymClassDto();
        c4.setName("Zumba Dance");
        c4.setDescription("Fun and energetic dance workout combining Latin music and fitness.");
        c4.setScheduleDateTime(LocalDateTime.now().plusDays(4).withHour(16).withMinute(0));
        c4.setMaxCapacity(25);
        c4.setCreatedBy("Trainer Sofia");
        gymClassService.create(c4);

        // ---- Attendance ----
        AttendanceDto att1 = new AttendanceDto();
        att1.setMemberId(member1.getId());
        att1.setDate(LocalDate.now());
        att1.setTime(java.time.LocalTime.of(9, 30));
        att1.setNotes("Regular morning visit");
        attendanceService.markAttendance(att1);

        AttendanceDto att2 = new AttendanceDto();
        att2.setMemberId(member2.getId());
        att2.setDate(LocalDate.now());
        att2.setTime(java.time.LocalTime.of(11, 0));
        attendanceService.markAttendance(att2);

        log.info("Data seeding complete!");
    }
}
