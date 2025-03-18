package nl.hsleiden.WebshopBE.DAO.repository;

import nl.hsleiden.WebshopBE.model.OrderModel;
import nl.hsleiden.WebshopBE.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<OrderModel, String> {
    List<OrderModel> findByUser(UserModel user);
    List<OrderModel> findByOrderStatus(String status);
} 