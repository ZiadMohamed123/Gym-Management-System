package com.example.demo.controller;

import com.example.demo.dto.MembershipPlanDto;
import com.example.demo.model.MembershipPlan;
import com.example.demo.model.enums.MembershipDuration;
import com.example.demo.model.enums.MembershipStatus;
import com.example.demo.service.MembershipPlanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/memberships")
@RequiredArgsConstructor
public class MembershipController {

    private final MembershipPlanService planService;

    @GetMapping("/plans")
    public String listPlans(Model model) {
        model.addAttribute("plans", planService.findAll());
        model.addAttribute("planDto", new MembershipPlanDto());
        model.addAttribute("durations", MembershipDuration.values());
        model.addAttribute("statuses", MembershipStatus.values());
        return "memberships/plans";
    }

    @PostMapping("/plans/create")
    public String createPlan(@Valid @ModelAttribute("planDto") MembershipPlanDto dto,
                             BindingResult result,
                             RedirectAttributes redirectAttributes,
                             Model model) {
        if (result.hasErrors()) {
            model.addAttribute("plans", planService.findAll());
            model.addAttribute("durations", MembershipDuration.values());
            model.addAttribute("statuses", MembershipStatus.values());
            model.addAttribute("showForm", true);
            return "memberships/plans";
        }
        planService.create(dto);
        redirectAttributes.addFlashAttribute("successMessage", "Membership plan created successfully!");
        return "redirect:/memberships/plans";
    }

    @GetMapping("/plans/edit/{id}")
    public String editPlan(@PathVariable Long id, Model model) {
        MembershipPlan plan = planService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Plan not found"));
        MembershipPlanDto dto = new MembershipPlanDto();
        dto.setId(plan.getId());
        dto.setName(plan.getName());
        dto.setDescription(plan.getDescription());
        dto.setDuration(plan.getDuration());
        dto.setPrice(plan.getPrice());
        dto.setStatus(plan.getStatus());
        model.addAttribute("planDto", dto);
        model.addAttribute("durations", MembershipDuration.values());
        model.addAttribute("statuses", MembershipStatus.values());
        return "memberships/edit-plan";
    }

    @PostMapping("/plans/update")
    public String updatePlan(@Valid @ModelAttribute("planDto") MembershipPlanDto dto,
                             BindingResult result,
                             RedirectAttributes redirectAttributes,
                             Model model) {
        if (result.hasErrors()) {
            model.addAttribute("durations", MembershipDuration.values());
            model.addAttribute("statuses", MembershipStatus.values());
            return "memberships/edit-plan";
        }
        planService.update(dto);
        redirectAttributes.addFlashAttribute("successMessage", "Plan updated successfully!");
        return "redirect:/memberships/plans";
    }

    @PostMapping("/plans/delete/{id}")
    public String deletePlan(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        planService.delete(id);
        redirectAttributes.addFlashAttribute("successMessage", "Plan deleted.");
        return "redirect:/memberships/plans";
    }
}
