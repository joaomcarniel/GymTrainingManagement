package ie.dbs.gymtrainingmanagement.WorkoutPlan;

import ie.dbs.gymtrainingmanagement.Common.FieldValidationException;
import ie.dbs.gymtrainingmanagement.Member.Member;
import ie.dbs.gymtrainingmanagement.Member.MemberRepository;
import ie.dbs.gymtrainingmanagement.WorkoutPlanItem.WorkoutPlanItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WorkoutPlanService {
    private final WorkoutPlanRepository workoutPlanRepository;
    private final WorkoutPlanItemRepository workoutPlanItemRepository;
    private final MemberRepository memberRepository;

    public WorkoutPlanService(WorkoutPlanRepository workoutPlanRepository,
                              WorkoutPlanItemRepository workoutPlanItemRepository,
                              MemberRepository memberRepository) {
        this.workoutPlanRepository = workoutPlanRepository;
        this.workoutPlanItemRepository = workoutPlanItemRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional
    public WorkoutPlan createWorkoutPlan(WorkoutPlanForm form) {
        Member member = findMember(form.getMemberId());

        WorkoutPlan plan = new WorkoutPlan(
                form.getName().trim(), blankToNull(form.getNotes()),
                form.getStartDate(), form.isActive(), member);
        return workoutPlanRepository.save(plan);
    }

    /** All plans, or only the ones of one member when memberId is given. */
    @Transactional(readOnly = true)
    public List<WorkoutPlan> getWorkoutPlans(Long memberId) {
        return memberId == null
                ? workoutPlanRepository.findAllWithDetails()
                : workoutPlanRepository.findByMemberIdWithDetails(memberId);
    }

    @Transactional(readOnly = true)
    public WorkoutPlan getWorkoutPlanById(Long id) {
        return workoutPlanRepository.findById(id).orElse(null);
    }

    @Transactional
    public WorkoutPlan updateWorkoutPlan(Long id, WorkoutPlanForm form) {
        WorkoutPlan plan = workoutPlanRepository.findById(id).orElse(null);
        if (plan == null) return null;

        Member member = findMember(form.getMemberId());

        plan.setName(form.getName().trim());
        plan.setNotes(blankToNull(form.getNotes()));
        plan.setStartDate(form.getStartDate());
        plan.setActive(form.isActive());
        plan.setMember(member);
        return workoutPlanRepository.save(plan);
    }

    // A plan owns its exercise rows, so they are removed together with it.
    @Transactional
    public boolean deleteWorkoutPlan(Long id) {
        if (!workoutPlanRepository.existsById(id)) return false;
        workoutPlanItemRepository.deleteAllByWorkoutPlanId(id);
        workoutPlanRepository.deleteById(id);
        return true;
    }

    private Member findMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new FieldValidationException(
                        "memberId", "The selected member does not exist"));
    }

    private String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }
}
