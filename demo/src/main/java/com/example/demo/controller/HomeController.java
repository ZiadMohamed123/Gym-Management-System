package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.service.AttendanceService;
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
}
