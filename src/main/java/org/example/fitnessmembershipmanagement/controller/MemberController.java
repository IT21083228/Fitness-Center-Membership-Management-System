package org.example.fitnessmembershipmanagement.controller;

import org.example.fitnessmembershipmanagement.model.Member;
import org.example.fitnessmembershipmanagement.service.MemberService;
import org.example.fitnessmembershipmanagement.util.ValidationUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/members")
    public String memberList(@RequestParam(value = "keyword", required = false) String keyword,
                             Model model) {

        List<Member> members = keyword != null && !keyword.trim().isEmpty()
                ? memberService.searchMembers(keyword)
                : memberService.getAllMembers();

        model.addAttribute("members", members);
        model.addAttribute("keyword", keyword);
        return "members";
    }

    @GetMapping("/members/add")
    public String addMemberPage(Model model) {
        if (!model.containsAttribute("member")) {
            model.addAttribute("member", new Member());
        }
        return "member-form";
    }

    @GetMapping("/members/edit/{id}")
    public String editMemberPage(@PathVariable String id, Model model) {
        Member member = memberService.getMemberById(id);

        if (member == null) {
            return "redirect:/members";
        }

        model.addAttribute("member", member);
        return "member-form";
    }

    @PostMapping("/members/save")
    public String saveMember(@ModelAttribute Member member,
                             RedirectAttributes redirectAttributes) {

        String error = validateMember(member);

        if (error != null) {
            redirectAttributes.addFlashAttribute("errorMessage", error);
            redirectAttributes.addFlashAttribute("member", member);

            return member.getMemberId() == null || member.getMemberId().isEmpty()
                    ? "redirect:/members/add"
                    : "redirect:/members/edit/" + member.getMemberId();
        }

        if (member.getMemberId() == null || member.getMemberId().isEmpty()) {
            memberService.addMember(member);
            redirectAttributes.addFlashAttribute("successMessage", "Member added successfully!");
        } else {
            memberService.updateMember(member);
            redirectAttributes.addFlashAttribute("successMessage", "Member updated successfully!");
        }

        return "redirect:/members";
    }

    @GetMapping("/members/delete/{id}")
    public String deleteMember(@PathVariable String id,
                               RedirectAttributes redirectAttributes) {
        memberService.deleteMember(id);
        redirectAttributes.addFlashAttribute("successMessage", "Member deleted successfully!");
        return "redirect:/members";
    }

    private String validateMember(Member member) {
        if (!ValidationUtil.isValidNIC(member.getNic())) return "Invalid NIC format.";
        if (!ValidationUtil.isValidPhone(member.getPhone())) return "Phone number must start with 0 and contain 10 digits.";
        if (!ValidationUtil.isValidEmail(member.getEmail())) return "Invalid email format.";
        if (!ValidationUtil.isNotFutureDate(member.getJoinDate())) return "Join date cannot be in the future.";

        if (memberService.isNicExists(member.getNic(), member.getMemberId())) return "NIC already exists.";
        if (memberService.isEmailExists(member.getEmail(), member.getMemberId())) return "Email already exists.";

        return null;
    }
}