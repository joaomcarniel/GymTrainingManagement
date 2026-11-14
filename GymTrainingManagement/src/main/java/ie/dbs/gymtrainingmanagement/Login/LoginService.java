package ie.dbs.gymtrainingmanagement.Login;

import org.springframework.stereotype.Service;

@Service
public class LoginService {
    private final LoginRepository loginRepository;

    public LoginService(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    public Login CreateLogin(Login login) {
        return loginRepository.save(login);
    }
}
