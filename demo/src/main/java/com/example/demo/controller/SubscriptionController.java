package com.example.demo.controller;

import com.example.demo.dto.SubscriptionDto;
import com.example.demo.model.enums.MembershipStatus;
import com.example.demo.model.enums.PaymentStatus;
import com.example.demo.service.MemberService;
import com.example.demo.service.MembershipPlanService;
import com.example.demo.service.SubscriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
@RequestMapping("/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;
    private final MemberService memberService;
    private final MembershipPlanService planService;

    @GetMapping("/assign")
    public String showAssign(Model model) {
        model.addAttribute("subscriptionDto", new SubscriptionDto());
        model.addAttribute("members", memberService.findAll());
        model.addAttribute("plans", planService.findByStatus(MembershipStatus.ACTIVE));
        model.addAttribute("paymentStatuses", PaymentStatus.values());
        model.addAttribute("today", LocalDate.now());
        return "subscriptions/assign";
    }

    @PostMapping("/assign")
    public String assign(@Valid @ModelAttribute("subscriptionDto") SubscriptionDto dto,
                         BindingResult result,
                         RedirectAttributes redirectAttributes,
                         Model model) {
        if (result.hasErrors()) {
            model.addAttribute("members", memberService.findAll());
            model.addAttribute("plans", planService.findByStatus(MembershipStatus.ACTIVE));
            model.addAttribute("paymentStatuses", PaymentStatus.values());
            model.addAttribute("today", LocalDate.now());
            return "subscriptions/assign";
        }
        try {
            subscriptionService.assign(dto);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Subscription assigned successfully!");
            return "redirect:/members/profile/" + dto.getMemberId();
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("members", memberService.findAll());
            model.addAttribute("plans", planService.findByStatus(MembershipStatus.ACTIVE));
            model.addAttribute("paymentStatuses", PaymentStatus.values());
            model.addAttribute("today", LocalDate.now());
            return "subscriptions/assign";
        }
    }

    @GetMapping("/renew/{memberId}")
    public String showRenew(@PathVariable Long memberId, Model model) {
        SubscriptionDto dto = new SubscriptionDto();
        dto.setMemberId(memberId);
        dto.setStartDate(LocalDate.now());
        model.addAttribute("subscriptionDto", dto);
        model.addAttribute("member", memberService.findById(memberId).orElseThrow());
        model.addAttribute("plans", planService.findByStatus(MembershipStatus.ACTIVE));
        model.addAttribute("paymentStatuses", PaymentStatus.values());
        model.addAttribute("today", LocalDate.now());
        return "subscriptions/renew";
    }

    @PostMapping("/renew")
    public String renew(@Valid @ModelAttribute("subscriptionDto") SubscriptionDto dto,
                        BindingResult result,
                        RedirectAttributes redirectAttributes,
                        Model model) {
        if (result.hasErrors()) {
            model.addAttribute("plans", planService.findByStatus(MembershipStatus.ACTIVE));
            model.addAttribute("paymentStatuses", PaymentStatus.values());
            model.addAttribute("member", memberService.findById(dto.getMemberId()).orElseThrow());
            return "subscriptions/renew";
        }
        try {
            subscriptionService.renew(null, dto);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Subscription renewed successfully!");
            return "redirect:/members/profile/" + dto.getMemberId();
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("plans", planService.findByStatus(MembershipStatus.ACTIVE));
            model.addAttribute("paymentStatuses", PaymentStatus.values());
            model.addAttribute("member", memberService.findById(dto.getMemberId()).orElseThrow());
            return "subscriptions/renew";
        }
    }
}
