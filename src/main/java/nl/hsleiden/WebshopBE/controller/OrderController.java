package nl.hsleiden.WebshopBE.controller;

import lombok.AllArgsConstructor;
import nl.hsleiden.WebshopBE.DAO.CartDAO;
import nl.hsleiden.WebshopBE.DAO.OrderDAO;
import nl.hsleiden.WebshopBE.DAO.PaymentDAO;
import nl.hsleiden.WebshopBE.DTO.OrderDTO;
import nl.hsleiden.WebshopBE.constant.ApiConstant;
import nl.hsleiden.WebshopBE.mapper.OrderMapper;
import nl.hsleiden.WebshopBE.model.CartModel;
import nl.hsleiden.WebshopBE.model.OrderModel;
import nl.hsleiden.WebshopBE.model.PaymentModel;
import nl.hsleiden.WebshopBE.model.UserModel;
import nl.hsleiden.WebshopBE.other.ApiResponse;
import nl.hsleiden.WebshopBE.service.ApiResponseService;
import nl.hsleiden.WebshopBE.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping()
@Validated
@AllArgsConstructor
public class OrderController {

    private final OrderDAO orderDAO;
    private final CartDAO cartDAO;
    private final PaymentDAO paymentDAO;
    private final OrderMapper orderMapper;
    private final AuthService authService;

    @PostMapping(value = ApiConstant.createOrder)
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    public ApiResponseService createOrder(@Valid @RequestBody OrderDTO orderDTO) {
        ApiResponse response = new ApiResponse();
        
        UserModel currentUser = authService.getCurrentUser();
        CartModel cart = currentUser.getCart();
        
        // Controleer of de winkelwagen niet leeg is
        if (cart.getProducts().isEmpty()) {
            response.setMessage("Kan geen bestelling plaatsen met een lege winkelwagen");
            return new ApiResponseService(false, HttpStatus.BAD_REQUEST, response);
        }
        
        // Controleer of de gebruiker een verzendadres heeft
        if (currentUser.getShippingAddress() == null) {
            response.setMessage("Voeg eerst een verzendadres toe aan je account");
            return new ApiResponseService(false, HttpStatus.BAD_REQUEST, response);
        }

        // Controleer of de betaalmethode geldig is
        PaymentModel paymentMethod = paymentDAO.findByPaymentOption(orderDTO.getPaymentMethod());
        if (paymentMethod == null) {
            response.setMessage("Ongeldige betaalmethode");
            return new ApiResponseService(false, HttpStatus.BAD_REQUEST, response);
        }
        
        // Maak een nieuwe bestelling
        OrderModel order = orderMapper.toModel(orderDTO, currentUser, cart);
        OrderModel savedOrder = orderDAO.createOrder(order);
        
        // Maak een nieuwe lege winkelwagen voor de gebruiker
        CartModel newCart = new CartModel();
        newCart.setUser(currentUser);
        currentUser.setCart(newCart);
        cartDAO.createCart(newCart);
        
        response.setMessage("Bestelling succesvol geplaatst");
        response.setResult(savedOrder);
        return new ApiResponseService(true, HttpStatus.CREATED, response);
    }

    @GetMapping(value = ApiConstant.getOrder)
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    public ApiResponseService getOrder(@PathVariable String orderId) {
        ApiResponse response = new ApiResponse();
        
        Optional<OrderModel> order = orderDAO.getOrder(orderId);
        if (order.isEmpty()) {
            response.setMessage("Bestelling niet gevonden");
            return new ApiResponseService(false, HttpStatus.NOT_FOUND, response);
        }
        
        // Controleer of de gebruiker toegang heeft tot deze bestelling
        UserModel currentUser = authService.getCurrentUser();
        if (!currentUser.isAdmin() && !order.get().getUser().getId().equals(currentUser.getId())) {
            response.setMessage("Geen toegang tot deze bestelling");
            return new ApiResponseService(false, HttpStatus.FORBIDDEN, response);
        }
        
        response.setResult(order.get());
        return new ApiResponseService(true, HttpStatus.OK, response);
    }

    @GetMapping(value = ApiConstant.getUserOrders)
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    public ApiResponseService getUserOrders() {
        ApiResponse response = new ApiResponse();
        
        UserModel currentUser = authService.getCurrentUser();
        List<OrderModel> orders = orderDAO.getOrdersByUser(currentUser);
        
        response.setResult(orders);
        return new ApiResponseService(true, HttpStatus.OK, response);
    }

    @GetMapping(value = ApiConstant.getAllOrders)
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseBody
    public ApiResponseService getAllOrders() {
        ApiResponse response = new ApiResponse();
        
        List<OrderModel> orders = orderDAO.getAllOrders();
        
        response.setResult(orders);
        return new ApiResponseService(true, HttpStatus.OK, response);
    }

    @PutMapping(value = ApiConstant.updateOrderStatus)
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseBody
    public ApiResponseService updateOrderStatus(@PathVariable String orderId, @RequestParam String status) {
        ApiResponse response = new ApiResponse();
        
        Optional<OrderModel> orderOpt = orderDAO.getOrder(orderId);
        if (orderOpt.isEmpty()) {
            response.setMessage("Bestelling niet gevonden");
            return new ApiResponseService(false, HttpStatus.NOT_FOUND, response);
        }
        
        OrderModel order = orderOpt.get();
        order.setOrderStatus(status);
        
        OrderModel updatedOrder = orderDAO.updateOrder(order);
        
        response.setMessage("Bestelstatus succesvol bijgewerkt");
        response.setResult(updatedOrder);
        return new ApiResponseService(true, HttpStatus.OK, response);
    }
} 