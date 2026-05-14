package com.example.demo.controller;

import com.example.demo.dto.TrainerCreateDto;
import com.example.demo.service.AdminUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/trainers")
@RequiredArgsConstructor
public class TrainerAdminController {

	private final AdminUserService adminUserService;

	@GetMapping("/create")
	public String showCreate(Model model) {
		model.addAttribute("trainerDto", new TrainerCreateDto());
		return "trainers/create";
	}

	@PostMapping("/create")
	public String create(@Valid @ModelAttribute("trainerDto") TrainerCreateDto dto,
						BindingResult result,
						RedirectAttributes redirectAttributes) {
		if (!dto.getPassword().equals(dto.getConfirmPassword())) {
			result.rejectValue("confirmPassword", "password.mismatch", "Passwords do not match");
		}
		if (result.hasErrors()) {
			return "trainers/create";
		}
		adminUserService.createTrainer(dto);
		redirectAttributes.addFlashAttribute("successMessage", "Trainer account created successfully!");
		return "redirect:/trainers/create";
	}
}
