package ie.dbs.gymtrainingmanagement.WorkoutPlan;

import ie.dbs.gymtrainingmanagement.Common.FieldValidationException;
import ie.dbs.gymtrainingmanagement.Member.MemberService;
import ie.dbs.gymtrainingmanagement.WorkoutPlanItem.WorkoutPlanItemService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/workout-plans")
public class WorkoutPlanController {
    private final WorkoutPlanService workoutPlanService;
    private final WorkoutPlanItemService workoutPlanItemService;
    private final MemberService memberService;

    public WorkoutPlanController(WorkoutPlanService workoutPlanService,
                                 WorkoutPlanItemService workoutPlanItemService,
                                 MemberService memberService) {
        this.workoutPlanService = workoutPlanService;
        this.workoutPlanItemService = workoutPlanItemService;
        this.memberService = memberService;
    }

    @GetMapping
    public String listWorkoutPlans(@RequestParam(required = false) Long memberId, Model model) {
        model.addAttribute("workoutPlans", workoutPlanService.getWorkoutPlans(memberId));
        model.addAttribute("members", memberService.getMembers());
        model.addAttribute("selectedMemberId", memberId);
        return "workoutplan/list";
    }

    // The page of one plan, with its exercises
    @GetMapping("/{id}")
    public String showWorkoutPlan(@PathVariable Long id, Model model) {
        WorkoutPlan workoutPlan = workoutPlanService.getWorkoutPlanById(id);
        if (workoutPlan == null) return "redirect:/workout-plans";

        model.addAttribute("workoutPlan", workoutPlan);
        model.addAttribute("items", workoutPlanItemService.getItems(id));
        return "workoutplan/detail";
    }

    @GetMapping("/new")
    public String showCreateForm(@RequestParam(required = false) Long memberId, Model model) {
        WorkoutPlanForm form = new WorkoutPlanForm();
        form.setMemberId(memberId);
        form.setStartDate(LocalDate.now());

        model.addAttribute("workoutPlanForm", form);
        addFormData(model);
        return "workoutplan/create";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        WorkoutPlan plan = workoutPlanService.getWorkoutPlanById(id);
        if (plan == null) return "redirect:/workout-plans";

        WorkoutPlanForm form = new WorkoutPlanForm(
                plan.getName(), plan.getMember().getId(), plan.getStartDate(),
                plan.getNotes(), plan.isActive());

        model.addAttribute("workoutPlanForm", form);
        model.addAttribute("workoutPlanId", id);
        addFormData(model);
        return "workoutplan/edit";
    }

    @PostMapping
    public String createWorkoutPlan(
            @Valid @ModelAttribute("workoutPlanForm") WorkoutPlanForm workoutPlanForm,
            BindingResult bindingResult,
            Model model) {

        if (!bindingResult.hasErrors()) {
            try {
                WorkoutPlan plan = workoutPlanService.createWorkoutPlan(workoutPlanForm);
                // Straight to the plan page, where the exercises are added
                return "redirect:/workout-plans/" + plan.getId();
            } catch (FieldValidationException e) {
                bindingResult.rejectValue(e.getField(), "invalid", e.getMessage());
            }
        }

        addFormData(model);
        return "workoutplan/create";
    }

    @PostMapping("/{id}")
    public String updateWorkoutPlan(
            @PathVariable Long id,
            @Valid @ModelAttribute("workoutPlanForm") WorkoutPlanForm workoutPlanForm,
            BindingResult bindingResult,
            Model model) {

        if (!bindingResult.hasErrors()) {
            try {
                // If the plan no longer exists the service returns null, and we go back to the list.
                WorkoutPlan plan = workoutPlanService.updateWorkoutPlan(id, workoutPlanForm);
                return plan == null ? "redirect:/workout-plans" : "redirect:/workout-plans/" + id;
            } catch (FieldValidationException e) {
                bindingResult.rejectValue(e.getField(), "invalid", e.getMessage());
            }
        }

        model.addAttribute("workoutPlanId", id);
        addFormData(model);
        return "workoutplan/edit";
    }

    @PostMapping("/{id}/delete")
    public String deleteWorkoutPlan(@PathVariable Long id) {
        workoutPlanService.deleteWorkoutPlan(id);
        return "redirect:/workout-plans";
    }

    private void addFormData(Model model) {
        model.addAttribute("members", memberService.getMembers());
    }
}
