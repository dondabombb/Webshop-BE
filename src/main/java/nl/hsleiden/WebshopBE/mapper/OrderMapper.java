package nl.hsleiden.WebshopBE.mapper;

import nl.hsleiden.WebshopBE.DTO.OrderDTO;
import nl.hsleiden.WebshopBE.model.*;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class OrderMapper {
    public OrderModel toModel(OrderDTO orderDTO, UserModel user, CartModel cart) {
        OrderModel order = new OrderModel();
        order.setUser(user);
        
        // Convert cart products to order items
        List<OrderItemModel> orderItems = new ArrayList<>();
        for (CartProductModel cartProduct : cart.getProducts()) {
            OrderItemModel orderItem = new OrderItemModel();
            orderItem.setProduct(cartProduct.getProduct());
            orderItem.setQuantity(cartProduct.getQuantity());
            orderItem.setPrice(cartProduct.getProduct().getPrice());
            orderItems.add(orderItem);
        }
        order.setItems(orderItems);
        
        order.setShippingAddress(user.getShippingAddress());
        order.setBillingAddress(user.getBillingAddress());
        order.setOrderDate(LocalDateTime.now());
        order.setOrderStatus("PENDING");
        
        // Calculate total amount
        double totalAmount = cart.getProducts().stream()
            .mapToDouble(cp -> cp.getProduct().getPrice() * cp.getQuantity())
            .sum();
        order.setTotalAmount(totalAmount);
        
        return order;
    }
} 