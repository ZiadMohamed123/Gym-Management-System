package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/members")
public class AdminMemberController {
	@GetMapping("/register")
	public String showRegistration() {
		return "members/register";
	}

	@GetMapping("/update")
	public String showUpdate() {
		return "members/update";
	}

	@GetMapping("/search")
	public String showSearch() {
		return "members/search";
	}
}
