package ie.dbs.gymtrainingmanagement.Equipment;

import ie.dbs.gymtrainingmanagement.Storage.InvalidImageException;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/equipment")
public class EquipmentController {
    private final EquipmentService equipmentService;

    public EquipmentController(EquipmentService equipmentService) {
        this.equipmentService = equipmentService;
    }

    @GetMapping
    public String listEquipment(Model model) {
        model.addAttribute("equipmentList", equipmentService.getEquipment());
        return "equipment/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("equipmentForm", new EquipmentForm());
        model.addAttribute("muscleGroups", MuscleGroup.values());
        return "equipment/create";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Equipment equipment = equipmentService.getEquipmentById(id);
        if (equipment == null) return "redirect:/equipment";

        EquipmentForm equipmentForm = new EquipmentForm(
                equipment.getName(), equipment.getDescription(), equipment.getMuscleGroup());

        model.addAttribute("equipmentForm", equipmentForm);
        model.addAttribute("equipmentId", id);
        model.addAttribute("currentImage", equipment.getImagePath());
        model.addAttribute("muscleGroups", MuscleGroup.values());
        return "equipment/edit";
    }

    @PostMapping
    public String createEquipment(
            @Valid @ModelAttribute("equipmentForm") EquipmentForm equipmentForm,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("muscleGroups", MuscleGroup.values());
            return "equipment/create";
        }

        try {
            equipmentService.createEquipment(equipmentForm);
        } catch (InvalidImageException e) {
            bindingResult.rejectValue("image", "image.invalid", e.getMessage());
            model.addAttribute("muscleGroups", MuscleGroup.values());
            return "equipment/create";
        }
        return "redirect:/equipment";
    }

    @PostMapping("/{id}")
    public String updateEquipment(
            @PathVariable Long id,
            @Valid @ModelAttribute("equipmentForm") EquipmentForm equipmentForm,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            return prepareEditView(id, model);
        }

        try {
            // If the equipment no longer exists the service returns null, and we go back to the list.
            equipmentService.updateEquipment(id, equipmentForm);
        } catch (InvalidImageException e) {
            bindingResult.rejectValue("image", "image.invalid", e.getMessage());
            return prepareEditView(id, model);
        }
        return "redirect:/equipment";
    }

    @PostMapping("/{id}/delete")
    public String deleteEquipment(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            equipmentService.deleteEquipment(id);
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("error",
                    "This equipment cannot be deleted because it is still assigned to a gym.");
        }
        return "redirect:/equipment";
    }

    // Re-adds what the edit page needs when we have to show it again after an error.
    private String prepareEditView(Long id, Model model) {
        Equipment equipment = equipmentService.getEquipmentById(id);
        if (equipment == null) return "redirect:/equipment";

        model.addAttribute("equipmentId", id);
        model.addAttribute("currentImage", equipment.getImagePath());
        model.addAttribute("muscleGroups", MuscleGroup.values());
        return "equipment/edit";
    }
}
