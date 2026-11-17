package ie.dbs.gymtrainingmanagement.Gym;

import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/gyms")
public class GymController {
    private final GymService gymService;

    public GymController(GymService gymService) {
        this.gymService = gymService;
    }

    @GetMapping
    public String listGyms(Model model) {
        model.addAttribute("gyms", gymService.getGyms());
        return "gym/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("gymForm", new GymForm());
        return "gym/create";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Gym gym = gymService.getGymById(id);
        if (gym == null) return "redirect:/gyms";

        Address address = gym.getAddress() != null ? gym.getAddress() : new Address();
        GymForm gymForm = new GymForm(
                gym.getName(), gym.getPhone(), address.getStreet(),
                address.getCity(), address.getCounty(), address.getEircode());

        model.addAttribute("gymForm", gymForm);
        model.addAttribute("gymId", id);
        return "gym/edit";
    }

    @PostMapping
    public String createGym(
            @Valid @ModelAttribute("gymForm") GymForm gymForm,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "gym/create";
        }

        gymService.createGym(gymForm);
        return "redirect:/gyms";
    }

    @PostMapping("/{id}")
    public String updateGym(
            @PathVariable Long id,
            @Valid @ModelAttribute("gymForm") GymForm gymForm,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("gymId", id);
            return "gym/edit";
        }

        // If the gym no longer exists the service returns null, and we simply go back to the list.
        gymService.updateGym(id, gymForm);
        return "redirect:/gyms";
    }

    @PostMapping("/{id}/delete")
    public String deleteGym(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            gymService.deleteGym(id);
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("error",
                    "This gym cannot be deleted because it still has members or equipment linked to it.");
        }
        return "redirect:/gyms";
    }
}
