package org.example.fitnessmembershipmanagement.controller;

import org.example.fitnessmembershipmanagement.model.GymClass;
import org.example.fitnessmembershipmanagement.model.Trainer;
import org.example.fitnessmembershipmanagement.service.GymClassService;
import org.example.fitnessmembershipmanagement.service.TrainerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
public class GymClassController {

    private final GymClassService gymClassService;
    private final TrainerService trainerService;

    public GymClassController(GymClassService gymClassService, TrainerService trainerService) {
        this.gymClassService = gymClassService;
        this.trainerService = trainerService;
    }

    @GetMapping("/classes")
    public String classList(@RequestParam(value = "keyword", required = false) String keyword,
                            Model model) {

        List<GymClass> classes = keyword != null && !keyword.trim().isEmpty()
                ? gymClassService.searchClasses(keyword)
                : gymClassService.getAllClasses();

        model.addAttribute("classes", classes);
        model.addAttribute("keyword", keyword);

        return "classes";
    }

    @GetMapping("/classes/add")
    public String addClassPage(Model model) {
        model.addAttribute("gymClass", new GymClass());
        model.addAttribute("availableTrainers", getAvailableTrainers());
        return "class-form";
    }

    @GetMapping("/classes/edit/{id}")
    public String editClassPage(@PathVariable String id, Model model) {
        GymClass gymClass = gymClassService.getClassById(id);

        if (gymClass == null) {
            return "redirect:/classes";
        }

        model.addAttribute("gymClass", gymClass);
        model.addAttribute("availableTrainers", getAvailableTrainers());
        return "class-form";
    }

    @PostMapping("/classes/save")
    public String saveClass(@ModelAttribute GymClass gymClass,
                            RedirectAttributes redirectAttributes) {

        Trainer trainer = trainerService.getTrainerById(gymClass.getTrainerId());

        if (trainer != null) {
            gymClass.setTrainerName(trainer.getName());
        }

        String error = validateClass(gymClass);

        if (error != null) {
            redirectAttributes.addFlashAttribute("errorMessage", error);

            if (gymClass.getClassId() == null || gymClass.getClassId().isEmpty()) {
                return "redirect:/classes/add";
            }

            return "redirect:/classes/edit/" + gymClass.getClassId();
        }

        if (gymClass.getClassId() == null || gymClass.getClassId().isEmpty()) {
            gymClassService.addClass(gymClass);
            redirectAttributes.addFlashAttribute("successMessage", "Class created successfully!");
        } else {
            gymClassService.updateClass(gymClass);
            redirectAttributes.addFlashAttribute("successMessage", "Class updated successfully!");
        }

        return "redirect:/classes";
    }

    @GetMapping("/classes/delete/{id}")
    public String deleteClass(@PathVariable String id,
                              RedirectAttributes redirectAttributes) {
        gymClassService.deleteClass(id);
        redirectAttributes.addFlashAttribute("successMessage", "Class deleted successfully!");
        return "redirect:/classes";
    }

    private List<Trainer> getAvailableTrainers() {
        List<Trainer> result = new ArrayList<>();

        for (Trainer trainer : trainerService.getAllTrainers()) {
            if ("Available".equalsIgnoreCase(trainer.getStatus())) {
                result.add(trainer);
            }
        }

        return result;
    }

    private String validateClass(GymClass gymClass) {
        if (gymClass.getTrainerId() == null || gymClass.getTrainerId().isEmpty()) {
            return "Please select a trainer.";
        }

        if (gymClass.getDateTime() == null || gymClass.getDateTime().isEmpty()) {
            return "Date and time is required.";
        }

        LocalDateTime selectedDateTime = LocalDateTime.parse(gymClass.getDateTime());

        if (selectedDateTime.isBefore(LocalDateTime.now())) {
            return "Class date and time cannot be in the past.";
        }

        if (gymClass.getTotalSlots() <= 0 || gymClass.getTotalSlots() > 10) {
            return "Total slots must be between 1 and 10.";
        }

        if (gymClass.getDuration() <= 0) {
            return "Duration must be greater than 0.";
        }

        return null;
    }
}