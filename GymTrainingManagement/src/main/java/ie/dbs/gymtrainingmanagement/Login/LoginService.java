package ie.dbs.gymtrainingmanagement.Login;

import ie.dbs.gymtrainingmanagement.Role.Role;
import ie.dbs.gymtrainingmanagement.Role.RoleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LoginService {
    private final LoginRepository loginRepository;
    private final RoleRepository roleRepository;

    public LoginService(LoginRepository loginRepository, RoleRepository roleRepository) {
        this.loginRepository = loginRepository;
        this.roleRepository = roleRepository;
    }

    public Login CreateLogin(LoginForm loginForm) {
        Role role = roleRepository
                .findById(loginForm.getRoleId().longValue()).orElse(null);
        if (role == null) return null;

        Login login = new Login(
                loginForm.getUserName(), loginForm.getPassword(), role, loginForm.isActive());
        return loginRepository.save(login);
    }

    @Transactional(readOnly = true)
    public List<Login> GetLogins() {
        return loginRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Login getLoginById(Long id) {
        return loginRepository.findById(id).orElse(null);
    }


    @Transactional
    public boolean DeleteLogin(Long id) {
        if (!loginRepository.existsById(id)) return false;
        loginRepository.deleteById(id);
        return true;
    }

    @Transactional
    public Login UpdateLogin(Long id, LoginForm form) {
        Login login = loginRepository.findById(id).orElse(null);
        if (login == null) return null;

        Role role = roleRepository
                .findById(form.getRoleId().longValue()).orElse(null);
        if (role == null) return null;

        login.setPassword(form.getPassword());
        login.setActive(form.isActive());
        login.setRole(role);
        return loginRepository.save(login);
    }
}
