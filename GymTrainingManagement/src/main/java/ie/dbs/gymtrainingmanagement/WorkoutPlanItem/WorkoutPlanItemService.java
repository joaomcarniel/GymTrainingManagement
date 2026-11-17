package ie.dbs.gymtrainingmanagement.WorkoutPlanItem;

import ie.dbs.gymtrainingmanagement.Common.FieldValidationException;
import ie.dbs.gymtrainingmanagement.Exercise.Exercise;
import ie.dbs.gymtrainingmanagement.Exercise.ExerciseRepository;
import ie.dbs.gymtrainingmanagement.WorkoutPlan.WorkoutPlan;
import ie.dbs.gymtrainingmanagement.WorkoutPlan.WorkoutPlanRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WorkoutPlanItemService {
    private final WorkoutPlanItemRepository itemRepository;
    private final WorkoutPlanRepository workoutPlanRepository;
    private final ExerciseRepository exerciseRepository;

    public WorkoutPlanItemService(WorkoutPlanItemRepository itemRepository,
                                  WorkoutPlanRepository workoutPlanRepository,
                                  ExerciseRepository exerciseRepository) {
        this.itemRepository = itemRepository;
        this.workoutPlanRepository = workoutPlanRepository;
        this.exerciseRepository = exerciseRepository;
    }

    /** Returns null when the plan does not exist. */
    @Transactional
    public WorkoutPlanItem createItem(Long planId, WorkoutPlanItemForm form) {
        WorkoutPlan plan = workoutPlanRepository.findById(planId).orElse(null);
        if (plan == null) return null;

        Exercise exercise = findExercise(form.getExerciseId());

        int orderIndex = form.getOrderIndex() != null
                ? form.getOrderIndex()
                : itemRepository.findMaxOrderIndex(planId) + 1;

        WorkoutPlanItem item = new WorkoutPlanItem(
                plan, exercise, form.getSets(), form.getReps(), form.getWeightKg(),
                form.getRestSeconds(), orderIndex, blankToNull(form.getDayLabel()));
        return itemRepository.save(item);
    }

    @Transactional(readOnly = true)
    public List<WorkoutPlanItem> getItems(Long planId) {
        return itemRepository.findByPlanIdWithDetails(planId);
    }

    /** Returns null when the row does not exist or belongs to another plan. */
    @Transactional(readOnly = true)
    public WorkoutPlanItem getItemById(Long planId, Long itemId) {
        WorkoutPlanItem item = itemRepository.findById(itemId).orElse(null);
        if (item == null || !item.getWorkoutPlan().getId().equals(planId)) return null;
        return item;
    }

    @Transactional
    public WorkoutPlanItem updateItem(Long planId, Long itemId, WorkoutPlanItemForm form) {
        WorkoutPlanItem item = itemRepository.findById(itemId).orElse(null);
        if (item == null || !item.getWorkoutPlan().getId().equals(planId)) return null;

        Exercise exercise = findExercise(form.getExerciseId());

        item.setExercise(exercise);
        item.setSets(form.getSets());
        item.setReps(form.getReps());
        item.setWeightKg(form.getWeightKg());
        item.setRestSeconds(form.getRestSeconds());
        item.setDayLabel(blankToNull(form.getDayLabel()));
        if (form.getOrderIndex() != null) {
            item.setOrderIndex(form.getOrderIndex());
        }
        return itemRepository.save(item);
    }

    @Transactional
    public boolean deleteItem(Long planId, Long itemId) {
        WorkoutPlanItem item = itemRepository.findById(itemId).orElse(null);
        if (item == null || !item.getWorkoutPlan().getId().equals(planId)) return false;

        itemRepository.delete(item);
        return true;
    }

    private Exercise findExercise(Long exerciseId) {
        return exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new FieldValidationException(
                        "exerciseId", "The selected exercise does not exist"));
    }

    private String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }
}
