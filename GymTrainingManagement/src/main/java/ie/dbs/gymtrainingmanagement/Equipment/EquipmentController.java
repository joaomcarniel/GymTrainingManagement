package ie.dbs.gymtrainingmanagement.Equipment;

import ie.dbs.gymtrainingmanagement.Storage.InvalidImageException;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/equipments")
public class EquipmentController {
    private final EquipmentService equipmentService;

    public EquipmentController(EquipmentService equipmentService) {
        this.equipmentService = equipmentService;
    }

    @GetMapping
    public String listEquipment(Model model) {
        model.addAttribute("equipmentList", equipmentService.GetEquipment());
        return "equipments/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("equipmentForm", new EquipmentForm());
        model.addAttribute("muscleGroups", MuscleGroup.values());
        return "equipments/create";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Equipment equipment = equipmentService.GetEquipmentById(id);
        if (equipment == null) return "redirect:/equipments";

        EquipmentForm equipmentForm = new EquipmentForm(
                equipment.getName(), equipment.getDescription(), equipment.getMuscleGroup());

        model.addAttribute("equipmentForm", equipmentForm);
        model.addAttribute("equipmentId", id);
        model.addAttribute("currentImage", equipment.getImagePath());
        model.addAttribute("muscleGroups", MuscleGroup.values());
        return "equipments/edit";
    }

    @PostMapping
    public String createEquipment(
            @Valid @ModelAttribute("equipmentForm") EquipmentForm equipmentForm,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("muscleGroups", MuscleGroup.values());
            return "equipments/create";
        }

        try {
            equipmentService.CreateEquipment(equipmentForm);
        } catch (InvalidImageException e) {
            bindingResult.rejectValue("image", "image.invalid", e.getMessage());
            model.addAttribute("muscleGroups", MuscleGroup.values());
            return "equipments/create";
        }
        return "redirect:/equipments";
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
            equipmentService.UpdateEquipment(id, equipmentForm);
        } catch (InvalidImageException e) {
            bindingResult.rejectValue("image", "image.invalid", e.getMessage());
            return prepareEditView(id, model);
        }
        return "redirect:/equipments";
    }

    @PostMapping("/{id}/delete")
    public String deleteEquipment(@PathVariable Long id) {
        equipmentService.DeleteEquipment(id);
        return "redirect:/equipments";
    }

    private String prepareEditView(Long id, Model model) {
        Equipment equipment = equipmentService.GetEquipmentById(id);
        if (equipment == null) return "redirect:/equipments";

        model.addAttribute("equipmentId", id);
        model.addAttribute("currentImage", equipment.getImagePath());
        model.addAttribute("muscleGroups", MuscleGroup.values());
        return "equipments/edit";
    }
}
