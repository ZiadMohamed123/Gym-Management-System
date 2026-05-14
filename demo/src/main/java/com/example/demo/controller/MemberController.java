package com.example.demo.controller;

import com.example.demo.dto.MemberRegistrationDto;
import com.example.demo.dto.MemberUpdateDto;
import com.example.demo.model.Member;
import com.example.demo.model.enums.AccountStatus;
import com.example.demo.service.AttendanceService;
import com.example.demo.service.BookingService;
import com.example.demo.service.GymClassService;
import com.example.demo.service.MemberService;
import com.example.demo.service.SubscriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final SubscriptionService subscriptionService;
    private final AttendanceService attendanceService;
    private final GymClassService gymClassService;
    private final BookingService bookingService;

    // ---- Register ----
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("memberDto", new MemberRegistrationDto());
        return "members/register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("memberDto") MemberRegistrationDto dto,
                           BindingResult result,
                           RedirectAttributes redirectAttributes,
                           Model model) {
        if (result.hasErrors()) {
            return "members/register";
        }
        try {
            Member member = memberService.register(dto);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Member registered successfully! Member ID: " + member.getMemberId());
            return "redirect:/members/search";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "members/register";
        }
    }

    // ---- Search ----
    @GetMapping("/search")
    public String search(@RequestParam(required = false) String keyword, Model model) {
        model.addAttribute("members", memberService.search(keyword));
        model.addAttribute("keyword", keyword);
        return "members/search";
    }

    // ---- Profile ----
    @GetMapping("/profile/{id}")
    public String profile(@PathVariable Long id, Model model) {
        Member member = memberService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Member not found: " + id));
        model.addAttribute("member", member);
        model.addAttribute("activeSubscription",
                subscriptionService.findActiveByMemberId(id).orElse(null));
        model.addAttribute("allSubscriptions", subscriptionService.findByMemberId(id));
        model.addAttribute("attendanceRecords", attendanceService.findByMemberId(id));
        model.addAttribute("availableClasses", gymClassService.findUpcoming());
        model.addAttribute("memberBookings", bookingService.getMemberBookings(id));
        return "members/profile";
    }

    @PostMapping("/profile/{id}/book")
    public String bookClassFromProfile(@PathVariable Long id,
                                       @RequestParam Long classId,
                                       RedirectAttributes redirectAttributes) {
        try {
            bookingService.bookClass(classId, id);
            redirectAttributes.addFlashAttribute("successMessage", "Class booked successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/members/profile/" + id;
    }

    @PostMapping("/profile/{memberId}/booking/cancel/{bookingId}")
    public String cancelBookingFromProfile(@PathVariable Long memberId,
                                           @PathVariable Long bookingId,
                                           RedirectAttributes redirectAttributes) {
        bookingService.cancelBooking(bookingId);
        redirectAttributes.addFlashAttribute("successMessage", "Booking cancelled.");
        return "redirect:/members/profile/" + memberId;
    }

    // ---- Update ----
    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        Member member = memberService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Member not found: " + id));

        MemberUpdateDto dto = new MemberUpdateDto();
        dto.setId(member.getId());
        dto.setFullName(member.getFullName());
        dto.setEmail(member.getEmail());
        dto.setPhoneNumber(member.getPhoneNumber());
        dto.setDateOfBirth(member.getDateOfBirth());
        dto.setAddress(member.getAddress());
        dto.setAccountStatus(member.getAccountStatus());

        model.addAttribute("memberDto", dto);
        model.addAttribute("statusOptions", AccountStatus.values());
        model.addAttribute("statusHistory", member.getStatusHistory());
        return "members/update";
    }

    @PostMapping("/update")
    public String update(@Valid @ModelAttribute("memberDto") MemberUpdateDto dto,
                         BindingResult result,
                         RedirectAttributes redirectAttributes,
                         Model model) {
        if (result.hasErrors()) {
            model.addAttribute("statusOptions", AccountStatus.values());
            return "members/update";
        }
        try {
            memberService.update(dto);
            redirectAttributes.addFlashAttribute("successMessage", "Member updated successfully!");
            return "redirect:/members/profile/" + dto.getId();
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("statusOptions", AccountStatus.values());
            return "members/update";
        }
    }

    // ---- Status Page ----
    @GetMapping("/status")
    public String status(Model model) {
        model.addAttribute("members", memberService.findAll());
        model.addAttribute("statusOptions", AccountStatus.values());
        return "members/status";
    }
}
