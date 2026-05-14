package com.example.demo.controller;

import com.example.demo.dto.MemberSignupDto;
import com.example.demo.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class SignupController {

	private final MemberService memberService;

	@GetMapping("/signup")
	public String showSignup(Model model) {
		model.addAttribute("signupDto", new MemberSignupDto());
		return "signup";
	}

	@PostMapping("/signup")
	public String signup(@Valid @ModelAttribute("signupDto") MemberSignupDto dto,
						BindingResult result,
						RedirectAttributes redirectAttributes,
						Model model) {
		if (!dto.getPassword().equals(dto.getConfirmPassword())) {
			result.rejectValue("confirmPassword", "password.mismatch", "Passwords do not match");
		}
		if (result.hasErrors()) {
			return "signup";
		}
		try {
			memberService.registerSelf(dto);
			redirectAttributes.addAttribute("registered", "true");
			return "redirect:/login";
		} catch (IllegalArgumentException e) {
			model.addAttribute("errorMessage", e.getMessage());
			return "signup";
		}
	}
}
