package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/classes")
public class AdminClassController {
	@GetMapping("/update")
	public String showUpdate() {
		return "classes/update";
	}
}
