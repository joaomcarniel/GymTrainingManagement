package ie.dbs.gymtrainingmanagement.Login;

import ie.dbs.gymtrainingmanagement.Role.RoleRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/logins")
public class LoginController {
    private final LoginService _loginService;
    private final RoleRepository _roleRepository;

    public LoginController(LoginService loginService, RoleRepository roleRepository){
        _loginService = loginService;
        _roleRepository = roleRepository;
    }

    @GetMapping
    public String ListLogins(Model model) {
        model.addAttribute("logins", _loginService.GetLogins());
        return "login/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("loginForm", new LoginForm());
        model.addAttribute("roles", _roleRepository.findAll());
        return "login/create";
    }

    @PostMapping
    public String CreateLogin(
            @Valid @ModelAttribute("loginForm") LoginForm loginForm,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("roles", _roleRepository.findAll());
            return "login/create";
        }

        Login savedLogin = _loginService.CreateLogin(loginForm);
        if (savedLogin == null) {
            bindingResult.rejectValue("roleId", "role.invalid",
                    "The selected role does not exist");
            model.addAttribute("role", _roleRepository.findAll());
            return "login/create";
        }
        return "redirect:/logins";
    }
}
