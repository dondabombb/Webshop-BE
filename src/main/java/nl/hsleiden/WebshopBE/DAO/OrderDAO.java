package nl.hsleiden.WebshopBE.DAO;

import nl.hsleiden.WebshopBE.DAO.repository.OrderRepository;
import nl.hsleiden.WebshopBE.model.OrderModel;
import nl.hsleiden.WebshopBE.model.UserModel;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class OrderDAO {
    private final OrderRepository orderRepository;

    public OrderDAO(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Optional<OrderModel> getOrder(String orderId) {
        return this.orderRepository.findById(orderId);
    }

    public List<OrderModel> getAllOrders() {
        return this.orderRepository.findAll();
    }

    public List<OrderModel> getOrdersByUser(UserModel user) {
        return this.orderRepository.findByUser(user);
    }

    public List<OrderModel> getOrdersByStatus(String status) {
        return this.orderRepository.findByOrderStatus(status);
    }

    public OrderModel createOrder(OrderModel order) {
        return this.orderRepository.save(order);
    }

    public OrderModel updateOrder(OrderModel updatedOrder) {
        return this.orderRepository.saveAndFlush(updatedOrder);
    }

    public void deleteOrder(String orderId) {
        this.orderRepository.deleteById(orderId);
    }
} 