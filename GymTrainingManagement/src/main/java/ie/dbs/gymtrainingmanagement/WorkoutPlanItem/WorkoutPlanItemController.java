package ie.dbs.gymtrainingmanagement.WorkoutPlanItem;

import ie.dbs.gymtrainingmanagement.Common.FieldValidationException;
import ie.dbs.gymtrainingmanagement.Exercise.ExerciseService;
import ie.dbs.gymtrainingmanagement.WorkoutPlan.WorkoutPlan;
import ie.dbs.gymtrainingmanagement.WorkoutPlan.WorkoutPlanService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

// The rows of a plan live under the plan's own URL: /workout-plans/{planId}/items/...
@Controller
@RequestMapping("/workout-plans/{planId}/items")
public class WorkoutPlanItemController {
    private final WorkoutPlanItemService itemService;
    private final WorkoutPlanService workoutPlanService;
    private final ExerciseService exerciseService;

    public WorkoutPlanItemController(WorkoutPlanItemService itemService,
                                     WorkoutPlanService workoutPlanService,
                                     ExerciseService exerciseService) {
        this.itemService = itemService;
        this.workoutPlanService = workoutPlanService;
        this.exerciseService = exerciseService;
    }

    @GetMapping("/new")
    public String showCreateForm(@PathVariable Long planId, Model model) {
        WorkoutPlan plan = workoutPlanService.getWorkoutPlanById(planId);
        if (plan == null) return "redirect:/workout-plans";

        // 3 x 10 is the most common prescription, so it is the starting point
        WorkoutPlanItemForm form = new WorkoutPlanItemForm();
        form.setSets(3);
        form.setReps(10);

        model.addAttribute("workoutPlanItemForm", form);
        addFormData(plan, model);
        return "workoutplanitem/create";
    }

    @GetMapping("/{itemId}/edit")
    public String showEditForm(@PathVariable Long planId, @PathVariable Long itemId, Model model) {
        WorkoutPlan plan = workoutPlanService.getWorkoutPlanById(planId);
        if (plan == null) return "redirect:/workout-plans";

        WorkoutPlanItem item = itemService.getItemById(planId, itemId);
        if (item == null) return "redirect:/workout-plans/" + planId;

        WorkoutPlanItemForm form = new WorkoutPlanItemForm(
                item.getExercise().getId(), item.getSets(), item.getReps(), item.getWeightKg(),
                item.getRestSeconds(), item.getDayLabel(), item.getOrderIndex());

        model.addAttribute("workoutPlanItemForm", form);
        model.addAttribute("itemId", itemId);
        addFormData(plan, model);
        return "workoutplanitem/edit";
    }

    @PostMapping
    public String createItem(
            @PathVariable Long planId,
            @Valid @ModelAttribute("workoutPlanItemForm") WorkoutPlanItemForm workoutPlanItemForm,
            BindingResult bindingResult,
            Model model) {

        WorkoutPlan plan = workoutPlanService.getWorkoutPlanById(planId);
        if (plan == null) return "redirect:/workout-plans";

        if (!bindingResult.hasErrors()) {
            try {
                itemService.createItem(planId, workoutPlanItemForm);
                return "redirect:/workout-plans/" + planId;
            } catch (FieldValidationException e) {
                bindingResult.rejectValue(e.getField(), "invalid", e.getMessage());
            }
        }

        addFormData(plan, model);
        return "workoutplanitem/create";
    }

    @PostMapping("/{itemId}")
    public String updateItem(
            @PathVariable Long planId,
            @PathVariable Long itemId,
            @Valid @ModelAttribute("workoutPlanItemForm") WorkoutPlanItemForm workoutPlanItemForm,
            BindingResult bindingResult,
            Model model) {

        WorkoutPlan plan = workoutPlanService.getWorkoutPlanById(planId);
        if (plan == null) return "redirect:/workout-plans";

        if (!bindingResult.hasErrors()) {
            try {
                // If the row no longer exists the service returns null, and we simply go back to the plan.
                itemService.updateItem(planId, itemId, workoutPlanItemForm);
                return "redirect:/workout-plans/" + planId;
            } catch (FieldValidationException e) {
                bindingResult.rejectValue(e.getField(), "invalid", e.getMessage());
            }
        }

        model.addAttribute("itemId", itemId);
        addFormData(plan, model);
        return "workoutplanitem/edit";
    }

    @PostMapping("/{itemId}/delete")
    public String deleteItem(@PathVariable Long planId, @PathVariable Long itemId) {
        itemService.deleteItem(planId, itemId);
        return "redirect:/workout-plans/" + planId;
    }

    private void addFormData(WorkoutPlan plan, Model model) {
        model.addAttribute("workoutPlan", plan);
        model.addAttribute("exercises", exerciseService.getExercises());
    }
}
