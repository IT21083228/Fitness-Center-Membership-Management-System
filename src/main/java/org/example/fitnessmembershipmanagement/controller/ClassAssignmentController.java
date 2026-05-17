package org.example.fitnessmembershipmanagement.controller;

import org.example.fitnessmembershipmanagement.model.GymClass;
import org.example.fitnessmembershipmanagement.service.ClassAssignmentService;
import org.example.fitnessmembershipmanagement.service.GymClassService;
import org.example.fitnessmembershipmanagement.service.MemberService;
import org.example.fitnessmembershipmanagement.service.TrainerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ClassAssignmentController {

    private final GymClassService gymClassService;
    private final ClassAssignmentService classAssignmentService;
    private final MemberService memberService;
    private final TrainerService trainerService;

    public ClassAssignmentController(GymClassService gymClassService,
                                     ClassAssignmentService classAssignmentService,
                                     MemberService memberService,
                                     TrainerService trainerService) {
        this.gymClassService = gymClassService;
        this.classAssignmentService = classAssignmentService;
        this.memberService = memberService;
        this.trainerService = trainerService;
    }

    @GetMapping("/classes/assign")
    public String assignPage(@RequestParam(value = "classId", required = false) String classId,
                             Model model) {

        model.addAttribute("classes", gymClassService.getAllClasses());
        model.addAttribute("members", memberService.getAllMembers());
        model.addAttribute("trainers", trainerService.getAllTrainers());
        model.addAttribute("selectedClassId", classId);

        if (classId != null && !classId.isEmpty()) {
            GymClass selectedClass = gymClassService.getClassById(classId);
            model.addAttribute("selectedClass", selectedClass);
            model.addAttribute("assignments", classAssignmentService.getAssignmentsByClassId(classId));
        }

        return "class-assign";
    }

    @PostMapping("/classes/assign")
    public String assignMember(@RequestParam String classId,
                               @RequestParam String memberId,
                               RedirectAttributes redirectAttributes) {

        boolean assigned = classAssignmentService.assignMember(classId, memberId);

        if (assigned) {
            redirectAttributes.addFlashAttribute("successMessage", "Member assigned to class successfully!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Assignment failed. Member may already be assigned or slots are full.");
        }

        return "redirect:/classes/assign?classId=" + classId;
    }
}