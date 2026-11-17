package ie.dbs.gymtrainingmanagement.Member;

import ie.dbs.gymtrainingmanagement.Common.FieldValidationException;
import ie.dbs.gymtrainingmanagement.Gym.GymService;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/members")
public class MemberController {
    private final MemberService memberService;
    private final GymService gymService;

    public MemberController(MemberService memberService, GymService gymService) {
        this.memberService = memberService;
        this.gymService = gymService;
    }

    @GetMapping
    public String listMembers(Model model) {
        model.addAttribute("members", memberService.getMembers());
        return "member/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("memberForm", new MemberForm());
        addFormData(model, null);
        return "member/create";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Member member = memberService.getMemberById(id);
        if (member == null) return "redirect:/members";

        // Long (not long) so a member without login does not break the form
        Long loginId = member.getLogin() == null ? null : Long.valueOf(member.getLogin().getId());
        MemberForm memberForm = new MemberForm(
                member.getFullName(), member.getEmail(), member.getPhone(),
                member.getBirthDate(), member.getGym().getId(), loginId, member.isActive());

        model.addAttribute("memberForm", memberForm);
        model.addAttribute("memberId", id);
        addFormData(model, id);
        return "member/edit";
    }

    @PostMapping
    public String createMember(
            @Valid @ModelAttribute("memberForm") MemberForm memberForm,
            BindingResult bindingResult,
            Model model) {

        if (!bindingResult.hasErrors()) {
            try {
                memberService.createMember(memberForm);
                return "redirect:/members";
            } catch (FieldValidationException e) {
                bindingResult.rejectValue(e.getField(), "invalid", e.getMessage());
            }
        }

        addFormData(model, null);
        return "member/create";
    }

    @PostMapping("/{id}")
    public String updateMember(
            @PathVariable Long id,
            @Valid @ModelAttribute("memberForm") MemberForm memberForm,
            BindingResult bindingResult,
            Model model) {

        if (!bindingResult.hasErrors()) {
            try {
                // If the member no longer exists the service returns null, and we go back to the list.
                memberService.updateMember(id, memberForm);
                return "redirect:/members";
            } catch (FieldValidationException e) {
                bindingResult.rejectValue(e.getField(), "invalid", e.getMessage());
            }
        }

        model.addAttribute("memberId", id);
        addFormData(model, id);
        return "member/edit";
    }

    @PostMapping("/{id}/delete")
    public String deleteMember(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            memberService.deleteMember(id);
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("error",
                    "This member cannot be deleted because they still have workout plans. Delete the plans first.");
        }
        return "redirect:/members";
    }

    private void addFormData(Model model, Long memberId) {
        model.addAttribute("gyms", gymService.getGyms());
        model.addAttribute("logins", memberService.getAvailableLogins(memberId));
    }
}
