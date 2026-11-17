package ie.dbs.gymtrainingmanagement.Exercise;

import ie.dbs.gymtrainingmanagement.Common.FieldValidationException;
import ie.dbs.gymtrainingmanagement.Equipment.EquipmentService;
import ie.dbs.gymtrainingmanagement.Equipment.MuscleGroup;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/exercises")
public class ExerciseController {
    private final ExerciseService exerciseService;
    private final EquipmentService equipmentService;

    public ExerciseController(ExerciseService exerciseService, EquipmentService equipmentService) {
        this.exerciseService = exerciseService;
        this.equipmentService = equipmentService;
    }

    @GetMapping
    public String listExercises(Model model) {
        model.addAttribute("exercises", exerciseService.getExercises());
        return "exercise/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("exerciseForm", new ExerciseForm());
        addFormData(model);
        return "exercise/create";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Exercise exercise = exerciseService.getExerciseById(id);
        if (exercise == null) return "redirect:/exercises";

        Long equipmentId = exercise.getEquipment() == null ? null : exercise.getEquipment().getId();
        ExerciseForm exerciseForm = new ExerciseForm(
                exercise.getName(), exercise.getMuscleGroup(), equipmentId, exercise.getDescription());

        model.addAttribute("exerciseForm", exerciseForm);
        model.addAttribute("exerciseId", id);
        addFormData(model);
        return "exercise/edit";
    }

    @PostMapping
    public String createExercise(
            @Valid @ModelAttribute("exerciseForm") ExerciseForm exerciseForm,
            BindingResult bindingResult,
            Model model) {

        if (!bindingResult.hasErrors()) {
            try {
                exerciseService.createExercise(exerciseForm);
                return "redirect:/exercises";
            } catch (FieldValidationException e) {
                bindingResult.rejectValue(e.getField(), "invalid", e.getMessage());
            }
        }

        addFormData(model);
        return "exercise/create";
    }

    @PostMapping("/{id}")
    public String updateExercise(
            @PathVariable Long id,
            @Valid @ModelAttribute("exerciseForm") ExerciseForm exerciseForm,
            BindingResult bindingResult,
            Model model) {

        if (!bindingResult.hasErrors()) {
            try {
                // If the exercise no longer exists the service returns null, and we go back to the list.
                exerciseService.updateExercise(id, exerciseForm);
                return "redirect:/exercises";
            } catch (FieldValidationException e) {
                bindingResult.rejectValue(e.getField(), "invalid", e.getMessage());
            }
        }

        model.addAttribute("exerciseId", id);
        addFormData(model);
        return "exercise/edit";
    }

    @PostMapping("/{id}/delete")
    public String deleteExercise(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            exerciseService.deleteExercise(id);
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("error",
                    "This exercise cannot be deleted because it is used in a workout plan.");
        }
        return "redirect:/exercises";
    }

    private void addFormData(Model model) {
        model.addAttribute("muscleGroups", MuscleGroup.values());
        model.addAttribute("equipmentList", equipmentService.getEquipment());
    }
}
