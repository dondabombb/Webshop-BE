package nl.hsleiden.WebshopBE.DAO.repository;

import nl.hsleiden.WebshopBE.model.CartModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<CartModel, String> {
}
