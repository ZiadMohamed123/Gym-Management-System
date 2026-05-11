package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/memberships")
public class MembershipPlanController {
	@GetMapping("/plans")
	public String showPlans() {
		return "memberships/plans";
	}
}
