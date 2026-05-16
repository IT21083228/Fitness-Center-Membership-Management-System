package org.example.fitnessmembershipmanagement.controller;

import org.example.fitnessmembershipmanagement.model.Trainer;
import org.example.fitnessmembershipmanagement.service.TrainerService;
import org.example.fitnessmembershipmanagement.util.ValidationUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class TrainerController {

    private final TrainerService trainerService;

    public TrainerController(TrainerService trainerService) {
        this.trainerService = trainerService;
    }

    @GetMapping("/trainers")
    public String trainerList(Model model) {
        model.addAttribute("trainers", trainerService.getAllTrainers());
        return "trainers";
    }

    @GetMapping("/trainers/add")
    public String addTrainerPage(Model model) {
        if (!model.containsAttribute("trainer")) {
            model.addAttribute("trainer", new Trainer());
        }
        return "trainer-form";
    }

/*    @GetMapping("/trainers/edit/{id}")
    public String editTrainerPage(@PathVariable String id, Model model) {
        Trainer trainer = trainerService.getTrainerById(id);

        if (trainer == null) {
            return "redirect:/trainers";
        }

        model.addAttribute("trainer", trainer);
        return "trainer-form";
    }*/
    @GetMapping("/trainers/edit/{id}")
    public String editTrainerPage(@PathVariable String id, Model model) {

        if (!model.containsAttribute("trainer")) {

            Trainer trainer = trainerService.getTrainerById(id);

            if (trainer == null) {
                return "redirect:/trainers";
            }

            model.addAttribute("trainer", trainer);
        }

        return "trainer-form";
    }

    @PostMapping("/trainers/save")
    public String saveTrainer(@ModelAttribute Trainer trainer,
                              RedirectAttributes redirectAttributes) {

        String error = validateTrainer(trainer);

        if (error != null) {
            redirectAttributes.addFlashAttribute("errorMessage", error);
            redirectAttributes.addFlashAttribute("trainer", trainer);

            if (trainer.getTrainerId() == null || trainer.getTrainerId().isEmpty()) {
                return "redirect:/trainers/add";
            }

            return "redirect:/trainers/edit/" + trainer.getTrainerId();
        }

        if (trainer.getTrainerId() == null || trainer.getTrainerId().isEmpty()) {
            trainerService.addTrainer(trainer);
            redirectAttributes.addFlashAttribute("successMessage", "Trainer added successfully!");
        } else {
            trainerService.updateTrainer(trainer);
            redirectAttributes.addFlashAttribute("successMessage", "Trainer updated successfully!");
        }

        return "redirect:/trainers";
    }

    @GetMapping("/trainers/delete/{id}")
    public String deleteTrainer(@PathVariable String id,
                                RedirectAttributes redirectAttributes) {
        trainerService.deleteTrainer(id);
        redirectAttributes.addFlashAttribute("successMessage", "Trainer deleted successfully!");
        return "redirect:/trainers";
    }

    private String validateTrainer(Trainer trainer) {
        if (!ValidationUtil.isValidNIC(trainer.getNic())) return "Invalid NIC format.";
        if (!ValidationUtil.isValidPhone(trainer.getPhone())) return "Phone number must start with 0 and contain 10 digits.";
        if (!ValidationUtil.isValidEmail(trainer.getEmail())) return "Invalid email format.";
        if (!ValidationUtil.isNotFutureDate(trainer.getJoinDate())) return "Join date cannot be in the future.";
        if (trainer.getYearsOfExperience() < 0) return "Experience cannot be negative.";

        if (trainerService.isNicExists(trainer.getNic(), trainer.getTrainerId())) return "Trainer NIC already exists.";
        if (trainerService.isEmailExists(trainer.getEmail(), trainer.getTrainerId())) return "Trainer email already exists.";

        return null;
    }
}