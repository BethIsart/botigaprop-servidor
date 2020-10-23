package botigaprop.servidor.Controllers;

import botigaprop.servidor.Models.User;
import botigaprop.servidor.Persistence.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @Autowired
    private UserRepository repository;

    @PostMapping("/add")
    public String addUser(@RequestBody User newUser) {
        repository.save(newUser);
        return "Usuario registrado correctamente con el id " + newUser.getId();
    }


    @PostMapping("/users")
    User newUser(@RequestBody User newUser) {

        return repository.save(newUser);
    }
}
