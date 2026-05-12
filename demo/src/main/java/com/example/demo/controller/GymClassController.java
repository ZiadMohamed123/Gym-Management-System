package com.example.demo.controller;

import com.example.demo.dto.GymClassDto;
import com.example.demo.model.GymClass;
import com.example.demo.service.BookingService;
import com.example.demo.service.GymClassService;
import com.example.demo.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

    @GetMapping
    public String listClasses(Model model) {
        model.addAttribute("classes", gymClassService.findAll());
        model.addAttribute("members", memberService.findAll());
        return "classes/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("classDto", new GymClassDto());
        return "classes/create";
    }

    @PostMapping("/create")
    public String createClass(@Valid @ModelAttribute("classDto") GymClassDto dto,
                              BindingResult result,
                              RedirectAttributes redirectAttributes,
                              Model model) {
        if (result.hasErrors()) {
            return "classes/create";
        }
        gymClassService.create(dto);
        redirectAttributes.addFlashAttribute("successMessage", "Class created successfully!");
        return "redirect:/classes";
    }

    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        GymClass gymClass = gymClassService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Class not found"));
        GymClassDto dto = new GymClassDto();
        dto.setId(gymClass.getId());
        dto.setName(gymClass.getName());
        dto.setDescription(gymClass.getDescription());
        dto.setScheduleDateTime(gymClass.getScheduleDateTime());
        dto.setMaxCapacity(gymClass.getMaxCapacity());
        dto.setCreatedBy(gymClass.getCreatedBy());
        model.addAttribute("classDto", dto);
        return "classes/update";
    }

    @PostMapping("/update")
    public String updateClass(@Valid @ModelAttribute("classDto") GymClassDto dto,
                              BindingResult result,
                              RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "classes/update";
        }
        gymClassService.update(dto);
        redirectAttributes.addFlashAttribute("successMessage", "Class updated successfully!");
        return "redirect:/classes";
    }

    @PostMapping("/cancel/{id}")
    public String cancelClass(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        gymClassService.cancel(id);
        redirectAttributes.addFlashAttribute("successMessage", "Class cancelled.");
        return "redirect:/classes";
    }

    @GetMapping("/participants/{id}")
    public String viewParticipants(@PathVariable Long id, Model model) {
        GymClass gymClass = gymClassService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Class not found"));
        model.addAttribute("gymClass", gymClass);
        model.addAttribute("participants", bookingService.getParticipants(id));
        return "classes/participants";
    }

    @PostMapping("/book")
    public String bookClass(@RequestParam Long classId,
                            @RequestParam Long memberId,
                            RedirectAttributes redirectAttributes) {
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
}
