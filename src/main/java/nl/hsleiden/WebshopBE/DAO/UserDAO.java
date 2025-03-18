package nl.hsleiden.WebshopBE.DAO;

import nl.hsleiden.WebshopBE.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserDAO extends JpaRepository<UserModel, String> {
    Optional<UserModel> findByEmail(String email);
    
    default Optional<UserModel> getUser(String userId) {
        return findById(userId);
    }
    
    default Optional<UserModel> getUserByEmail(String email) {
        return findByEmail(email);
    }

    default List<UserModel> getAllUsers() {
        return findAll();
    }

    default UserModel createUser(UserModel user) {
        return save(user);
    }

    default UserModel updateUser(UserModel updatedUser) {
        return saveAndFlush(updatedUser);
    }

    default void deleteUser(String userId) {
        deleteById(userId);
    }
}
