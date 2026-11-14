package ie.dbs.gymtrainingmanagement.Login;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
public class LoginController {
    private final LoginService _loginService;

    public LoginController(LoginService loginService){
        _loginService = loginService;
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello Advanced Web Development";
    }

    @PostMapping("/new")
    @ResponseStatus(HttpStatus.CREATED)
    public Login CreateLogin(@RequestBody Login login) {
        return _loginService.CreateLogin(login);

    }
}
