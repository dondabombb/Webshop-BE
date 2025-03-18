package nl.hsleiden.WebshopBE.mapper;

import nl.hsleiden.WebshopBE.DTO.OrderDTO;
import nl.hsleiden.WebshopBE.model.*;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class OrderMapper {

    private final AddressMapper addressMapper;

    public OrderMapper(AddressMapper addressMapper) {
        this.addressMapper = addressMapper;
    }

    public OrderModel toModel(OrderDTO dto, UserModel user, CartModel cart) {
        OrderModel order = new OrderModel();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setOrderStatus("PENDING");
        order.setTotalAmount(cart.getTotalPrice());
        
        // Verzendadres instellen
        order.setShippingAddress(addressMapper.toModel(dto.getShippingAddress()));
        
        // Factuuradres instellen (of verzendadres gebruiken als factuuradres niet is opgegeven)
        if (dto.getBillingAddress() != null) {
            order.setBillingAddress(addressMapper.toModel(dto.getBillingAddress()));
        } else {
            order.setBillingAddress(order.getShippingAddress());
        }
        
        // Producten uit winkelwagen omzetten naar orderitems
        List<OrderItemModel> orderItems = new ArrayList<>();
        for (CartProductModel cartProduct : cart.getProducts()) {
            OrderItemModel orderItem = new OrderItemModel();
            orderItem.setProduct(cartProduct.getProduct());
            orderItem.setQuantity(cartProduct.getQuantity());
            orderItem.setPrice(cartProduct.getProduct().getPrice());
            orderItem.setProductName(cartProduct.getProduct().getName());
            orderItems.add(orderItem);
        }
        order.setItems(orderItems);
        
        return order;
    }
} 