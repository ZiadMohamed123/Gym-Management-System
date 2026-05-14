package com.example.demo.controller;

import com.example.demo.dto.GymClassDto;
import com.example.demo.model.GymClass;
import com.example.demo.model.Member;
import com.example.demo.service.BookingService;
import com.example.demo.service.GymClassService;
import com.example.demo.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/classes")
@RequiredArgsConstructor
public class GymClassController {

    private final GymClassService gymClassService;
    private final BookingService bookingService;
    private final MemberService memberService;
    private final com.example.demo.repository.AppUserRepository appUserRepository;

    @GetMapping
    public String listClasses(Model model, Authentication authentication) {
        if (hasRole(authentication, "ROLE_TRAINER")) {
            model.addAttribute("classes", gymClassService.findAll().stream()
                    .filter(c -> authentication.getName().equals(c.getCreatedBy()))
                    .toList());
        } else {
            model.addAttribute("classes", gymClassService.findAll());
        }
        if (authentication != null) {
            model.addAttribute("currentUsername", authentication.getName());
        }
        if (hasRole(authentication, "ROLE_ADMIN") || hasRole(authentication, "ROLE_TRAINER")) {
            model.addAttribute("members", memberService.findAll());
        }
        if (hasRole(authentication, "ROLE_MEMBER")) {
            Member currentMember = memberService.findByEmail(authentication.getName()).orElse(null);
            if (currentMember != null) {
                model.addAttribute("currentMemberId", currentMember.getId());
                model.addAttribute("bookedClassIds",
                        bookingService.getMemberBookings(currentMember.getId()).stream()
                                .map(booking -> booking.getGymClass().getId())
                                .toList());
            }
        }
        return "classes/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model, Authentication authentication) {
        GymClassDto dto = new GymClassDto();
        if (hasRole(authentication, "ROLE_TRAINER")) {
            dto.setCreatedBy(authentication.getName());
        }
        if (hasRole(authentication, "ROLE_ADMIN")) {
            model.addAttribute("trainers", appUserRepository.findByRole(com.example.demo.model.enums.UserRole.TRAINER));
        }
        model.addAttribute("classDto", dto);
        return "classes/create";
    }

    @PostMapping("/create")
    public String createClass(@Valid @ModelAttribute("classDto") GymClassDto dto,
                              BindingResult result,
                              RedirectAttributes redirectAttributes,
                              Model model,
                              Authentication authentication) {
        if (hasRole(authentication, "ROLE_TRAINER")) {
            dto.setCreatedBy(authentication.getName());
        }
        if (dto.getCreatedBy() == null || dto.getCreatedBy().isBlank()) {
            dto.setCreatedBy(authentication.getName());
        }
        if (result.hasErrors()) {
            if (hasRole(authentication, "ROLE_ADMIN")) {
                model.addAttribute("trainers", appUserRepository.findByRole(com.example.demo.model.enums.UserRole.TRAINER));
            }
            return "classes/create";
        }
        gymClassService.create(dto);
        redirectAttributes.addFlashAttribute("successMessage", "Class created successfully!");
        return "redirect:/classes";
    }

    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model, Authentication authentication,
                                 RedirectAttributes redirectAttributes) {
        GymClass gymClass = gymClassService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Class not found"));
        if (hasRole(authentication, "ROLE_TRAINER")
                && !gymClass.getCreatedBy().equals(authentication.getName())) {
            redirectAttributes.addFlashAttribute("errorMessage", "You can only edit your own classes.");
            return "redirect:/classes";
        }
        GymClassDto dto = new GymClassDto();
        dto.setId(gymClass.getId());
        dto.setName(gymClass.getName());
        dto.setDescription(gymClass.getDescription());
        dto.setScheduleDateTime(gymClass.getScheduleDateTime());
        dto.setMaxCapacity(gymClass.getMaxCapacity());
        dto.setCreatedBy(gymClass.getCreatedBy());
        if (hasRole(authentication, "ROLE_ADMIN")) {
            model.addAttribute("trainers", appUserRepository.findByRole(com.example.demo.model.enums.UserRole.TRAINER));
        }
        model.addAttribute("classDto", dto);
        return "classes/update";
    }

    @PostMapping("/update")
    public String updateClass(@Valid @ModelAttribute("classDto") GymClassDto dto,
                              BindingResult result,
                              RedirectAttributes redirectAttributes,
                              Model model,
                              Authentication authentication) {
        if (hasRole(authentication, "ROLE_TRAINER")) {
            GymClass gymClass = gymClassService.findById(dto.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Class not found"));
            if (!gymClass.getCreatedBy().equals(authentication.getName())) {
                throw new AccessDeniedException("You can only update your own classes.");
            }
            dto.setCreatedBy(gymClass.getCreatedBy());
        }
        if (result.hasErrors()) {
            if (hasRole(authentication, "ROLE_ADMIN")) {
                model.addAttribute("trainers", appUserRepository.findByRole(com.example.demo.model.enums.UserRole.TRAINER));
            }
            return "classes/update";
        }
        gymClassService.update(dto);
        redirectAttributes.addFlashAttribute("successMessage", "Class updated successfully!");
        return "redirect:/classes";
    }

    @PostMapping("/cancel/{id}")
    public String cancelClass(@PathVariable Long id, RedirectAttributes redirectAttributes,
                              Authentication authentication) {
        if (hasRole(authentication, "ROLE_TRAINER")) {
            GymClass gymClass = gymClassService.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Class not found"));
            if (!gymClass.getCreatedBy().equals(authentication.getName())) {
                throw new AccessDeniedException("You can only cancel your own classes.");
            }
        }
        gymClassService.cancel(id);
        redirectAttributes.addFlashAttribute("successMessage", "Class cancelled.");
        return "redirect:/classes";
    }

    @GetMapping("/participants/{id}")
    public String viewParticipants(@PathVariable Long id, Model model, Authentication authentication) {
        GymClass gymClass = gymClassService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Class not found"));
        if (hasRole(authentication, "ROLE_TRAINER")
                && !gymClass.getCreatedBy().equals(authentication.getName())) {
            throw new AccessDeniedException("You can only view your own classes.");
        }
        model.addAttribute("gymClass", gymClass);
        model.addAttribute("participants", bookingService.getParticipants(id));
        return "classes/participants";
    }

    @PostMapping("/book")
    public String bookClass(@RequestParam Long classId,
                            @RequestParam(required = false) Long memberId,
                            RedirectAttributes redirectAttributes,
                            Authentication authentication) {
        if (hasRole(authentication, "ROLE_MEMBER")) {
            memberId = memberService.findByEmail(authentication.getName())
                    .map(Member::getId)
                    .orElseThrow(() -> new IllegalArgumentException("Member account not found"));
        } else if (memberId == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Please select a member to book.");
            return "redirect:/classes";
        }
        try {
            bookingService.bookClass(classId, memberId);
            redirectAttributes.addFlashAttribute("successMessage", "Class booked successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/classes";
    }

    @PostMapping("/booking/cancel/{bookingId}")
    public String cancelBooking(@PathVariable Long bookingId,
                                @RequestParam Long classId,
                                RedirectAttributes redirectAttributes) {
        bookingService.cancelBooking(bookingId);
        redirectAttributes.addFlashAttribute("successMessage", "Booking cancelled.");
        return "redirect:/classes/participants/" + classId;
    }

    private boolean hasRole(Authentication authentication, String role) {
        if (authentication == null) {
            return false;
        }
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            if (authority.getAuthority().equals(role)) {
                return true;
            }
        }
        return false;
    }
}
