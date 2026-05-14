package org.example.fitnessmembershipmanagement.controller;

import org.example.fitnessmembershipmanagement.model.Member;
import org.example.fitnessmembershipmanagement.service.MemberService;
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

        List<Member> members;

        if (keyword != null && !keyword.trim().isEmpty()) {
            members = memberService.searchMembers(keyword);
        } else {
            members = memberService.getAllMembers();
        }

        model.addAttribute("members", members);
        model.addAttribute("keyword", keyword);

        return "members";
    }

    @GetMapping("/members/add")
    public String addMemberPage(Model model) {
        model.addAttribute("member", new Member());
        return "member-form";
    }

    @PostMapping("/members/save")
    public String saveMember(@ModelAttribute Member member,
                             RedirectAttributes redirectAttributes) {

        if (member.getMemberId() == null || member.getMemberId().isEmpty()) {
            memberService.addMember(member);
            redirectAttributes.addFlashAttribute("successMessage", "Member added successfully!");
        } else {
            Member existingMember = memberService.getMemberById(member.getMemberId());

            if (existingMember != null) {
                member.setTrainerName(existingMember.getTrainerName());
            } else {
                member.setTrainerName("Not Assigned");
            }

            memberService.updateMember(member);
            redirectAttributes.addFlashAttribute("successMessage", "Member updated successfully!");
        }

        return "redirect:/members";
    }

    @GetMapping("/members/edit/{memberId}")
    public String editMemberPage(@PathVariable String memberId, Model model) {
        Member member = memberService.getMemberById(memberId);

        if (member == null) {
            return "redirect:/members";
        }

        model.addAttribute("member", member);
        return "member-form";
    }

    @GetMapping("/members/delete/{memberId}")
    public String deleteMember(@PathVariable String memberId,
                               RedirectAttributes redirectAttributes) {

        memberService.deleteMember(memberId);
        redirectAttributes.addFlashAttribute("successMessage", "Member deleted successfully!");

        return "redirect:/members";
    }

    @GetMapping("/members/trainer")
    public String trainerAssignmentPage(Model model) {
        model.addAttribute("members", memberService.getAllMembers());

        String[] trainers = {
                "Saman Perera",
                "Nimal Silva",
                "Kasun Fernando",
                "Amal Jayasinghe",
                "Ruwan Kumara"
        };

        model.addAttribute("trainers", trainers);

        return "trainer-assignment";
    }

    @PostMapping("/members/assign-trainer")
    public String assignTrainer(@RequestParam String memberId,
                                @RequestParam String trainerName,
                                RedirectAttributes redirectAttributes) {

        memberService.assignTrainer(memberId, trainerName);
        redirectAttributes.addFlashAttribute("successMessage", "Trainer assigned successfully!");

        return "redirect:/members/trainer";
    }
}