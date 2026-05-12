package com.example.demo.controller;

import com.example.demo.dto.AttendanceDto;
import com.example.demo.service.AttendanceService;
import com.example.demo.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalTime;

@Controller
@RequestMapping("/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;
    private final MemberService memberService;

    @GetMapping("/mark")
    public String showMarkAttendance(
            @RequestParam(required = false) String date,
            Model model) {

        LocalDate targetDate = (date != null && !date.isBlank())
                ? LocalDate.parse(date)
                : LocalDate.now();

        AttendanceDto dto = new AttendanceDto();
        dto.setDate(targetDate);
        dto.setTime(LocalTime.now().withSecond(0).withNano(0));

        model.addAttribute("attendanceDto", dto);
        model.addAttribute("members", memberService.findAll());
        model.addAttribute("todayRecords", attendanceService.findByDate(targetDate));
        model.addAttribute("selectedDate", targetDate);
        return "attendance/mark";
    }

    @PostMapping("/mark")
    public String markAttendance(@Valid @ModelAttribute("attendanceDto") AttendanceDto dto,
                                 BindingResult result,
                                 RedirectAttributes redirectAttributes,
                                 Model model) {
        if (result.hasErrors()) {
            model.addAttribute("members", memberService.findAll());
            model.addAttribute("todayRecords", attendanceService.findByDate(dto.getDate()));
            model.addAttribute("selectedDate", dto.getDate());
            return "attendance/mark";
        }
        try {
            attendanceService.markAttendance(dto);
            redirectAttributes.addFlashAttribute("successMessage", "Attendance marked successfully!");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/attendance/mark?date=" + dto.getDate();
    }
}
