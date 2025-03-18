package nl.hsleiden.WebshopBE.DAO.repository;

import nl.hsleiden.WebshopBE.model.CartProductModel;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartProductRepository extends JpaRepository<CartProductModel, String>{
    @Query("SELECT u FROM CartProductModel u WHERE u.cart.id = ?1 and u.product.id = ?2")
    Optional<CartProductModel> findByCartIdAndProductId(String cartId, String productId);
}
