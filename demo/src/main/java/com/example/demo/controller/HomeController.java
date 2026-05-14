package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.core.Authentication;

import com.example.demo.model.Member;
import com.example.demo.service.AttendanceService;
import com.example.demo.service.BookingService;
import com.example.demo.service.GymClassService;
import com.example.demo.service.MemberService;
import com.example.demo.service.SubscriptionService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberService memberService;
    private final SubscriptionService subscriptionService;
    private final GymClassService gymClassService;
    private final AttendanceService attendanceService;
    private final BookingService bookingService;

    @GetMapping("/")
    public String landing(Model model) {
        model.addAttribute("totalMembers", memberService.countTotal());
        model.addAttribute("activeSubscriptions", subscriptionService.countActiveSubscriptions());
        model.addAttribute("totalClasses", gymClassService.countTotal());
        model.addAttribute("todayVisits", attendanceService.countTodayVisits());
        return "landing";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("totalMembers", memberService.countTotal());
        model.addAttribute("activeMembers", memberService.countActive());
        model.addAttribute("activeSubscriptions", subscriptionService.countActiveSubscriptions());
        model.addAttribute("totalClasses", gymClassService.countTotal());
        model.addAttribute("todayVisits", attendanceService.countTodayVisits());
        model.addAttribute("recentMembers", memberService.findAll()
                .stream().limit(5).toList());
        model.addAttribute("upcomingClasses", gymClassService.findUpcoming()
                .stream().limit(4).toList());
        return "index";
    }

    @GetMapping("/trainer/home")
    public String trainerHome(Authentication authentication, Model model) {
        String username = authentication.getName();
        model.addAttribute("trainerName", username);
        model.addAttribute("myClasses", gymClassService.findByCreator(username));
        return "trainer/home";
    }

    @GetMapping("/member/home")
    public String memberHome(Authentication authentication, Model model) {
        Member member = memberService.findByEmail(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException("Member account not found"));
        model.addAttribute("member", member);
        model.addAttribute("memberBookings", bookingService.getMemberBookings(member.getId()));
        model.addAttribute("upcomingClasses", gymClassService.findUpcoming());
        return "member/home";
    }
}
