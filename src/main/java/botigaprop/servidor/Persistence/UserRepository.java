package botigaprop.servidor.Persistence;

import botigaprop.servidor.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer>{
}
