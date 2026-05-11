package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/classes")
public class TrainerClassController {
	@GetMapping("/create")
	public String showCreate() {
		return "classes/create";
	}
}
