package nl.hsleiden.WebshopBE.DAO.repository;

import nl.hsleiden.WebshopBE.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserModel, String> {

    Optional<UserModel> findByEmail(String email);
}
