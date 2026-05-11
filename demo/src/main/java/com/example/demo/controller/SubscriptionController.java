package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/subscriptions")
public class SubscriptionController {
	@GetMapping("/assign")
	public String showAssign() {
		return "subscriptions/assign";
	}

	@GetMapping("/renew")
	public String showRenew() {
		return "subscriptions/renew";
	}
}
