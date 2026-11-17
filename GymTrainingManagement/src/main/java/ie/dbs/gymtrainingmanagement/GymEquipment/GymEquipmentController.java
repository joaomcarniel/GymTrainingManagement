package ie.dbs.gymtrainingmanagement.GymEquipment;

import ie.dbs.gymtrainingmanagement.Common.FieldValidationException;
import ie.dbs.gymtrainingmanagement.Equipment.EquipmentService;
import ie.dbs.gymtrainingmanagement.Gym.GymService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/gym-equipment")
public class GymEquipmentController {
    private final GymEquipmentService gymEquipmentService;
    private final GymService gymService;
    private final EquipmentService equipmentService;

    public GymEquipmentController(GymEquipmentService gymEquipmentService,
                                  GymService gymService,
                                  EquipmentService equipmentService) {
        this.gymEquipmentService = gymEquipmentService;
        this.gymService = gymService;
        this.equipmentService = equipmentService;
    }

    @GetMapping
    public String listGymEquipment(@RequestParam(required = false) Long gymId, Model model) {
        model.addAttribute("gymEquipmentList", gymEquipmentService.getGymEquipment(gymId));
        model.addAttribute("gyms", gymService.getGyms());
        model.addAttribute("selectedGymId", gymId);
        return "gymequipment/list";
    }

    @GetMapping("/new")
    public String showCreateForm(@RequestParam(required = false) Long gymId, Model model) {
        GymEquipmentForm form = new GymEquipmentForm();
        form.setGymId(gymId);
        form.setQuantity(1);

        model.addAttribute("gymEquipmentForm", form);
        addFormData(model);
        return "gymequipment/create";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        GymEquipment gymEquipment = gymEquipmentService.getGymEquipmentById(id);
        if (gymEquipment == null) return "redirect:/gym-equipment";

        GymEquipmentForm form = new GymEquipmentForm(
                gymEquipment.getGym().getId(), gymEquipment.getEquipment().getId(),
                gymEquipment.getQuantity(), gymEquipment.getCondition());

        model.addAttribute("gymEquipmentForm", form);
        addEditData(gymEquipment, model);
        return "gymequipment/edit";
    }

    @PostMapping
    public String createGymEquipment(
            @Valid @ModelAttribute("gymEquipmentForm") GymEquipmentForm gymEquipmentForm,
            BindingResult bindingResult,
            Model model) {

        if (!bindingResult.hasErrors()) {
            try {
                gymEquipmentService.createGymEquipment(gymEquipmentForm);
                // Back to the list of the same gym, handy when adding several machines in a row
                return "redirect:/gym-equipment?gymId=" + gymEquipmentForm.getGymId();
            } catch (FieldValidationException e) {
                bindingResult.rejectValue(e.getField(), "invalid", e.getMessage());
            }
        }

        addFormData(model);
        return "gymequipment/create";
    }

    @PostMapping("/{id}")
    public String updateGymEquipment(
            @PathVariable Long id,
            @Valid @ModelAttribute("gymEquipmentForm") GymEquipmentForm gymEquipmentForm,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            return prepareEditView(id, model);
        }

        // If the record no longer exists the service returns null, and we simply go back to the list.
        gymEquipmentService.updateGymEquipment(id, gymEquipmentForm);
        return "redirect:/gym-equipment";
    }

    @PostMapping("/{id}/delete")
    public String deleteGymEquipment(@PathVariable Long id) {
        gymEquipmentService.deleteGymEquipment(id);
        return "redirect:/gym-equipment";
    }

    private void addFormData(Model model) {
        model.addAttribute("gyms", gymService.getGyms());
        model.addAttribute("equipmentList", equipmentService.getEquipment());
        model.addAttribute("conditions", EquipmentCondition.values());
    }

    private void addEditData(GymEquipment gymEquipment, Model model) {
        model.addAttribute("gymEquipmentId", gymEquipment.getId());
        model.addAttribute("gymName", gymEquipment.getGym().getName());
        model.addAttribute("equipmentName", gymEquipment.getEquipment().getName());
        model.addAttribute("conditions", EquipmentCondition.values());
    }

    // Re-adds what the edit page needs when we have to show it again after an error.
    private String prepareEditView(Long id, Model model) {
        GymEquipment gymEquipment = gymEquipmentService.getGymEquipmentById(id);
        if (gymEquipment == null) return "redirect:/gym-equipment";

        addEditData(gymEquipment, model);
        return "gymequipment/edit";
    }
}
