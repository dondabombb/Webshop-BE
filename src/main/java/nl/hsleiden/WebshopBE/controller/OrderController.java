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
        

        if (cart.getProducts().isEmpty()) {
            response.setMessage("Cannot place order with empty cart");
            return new ApiResponseService(false, HttpStatus.BAD_REQUEST, response);
        }
        

        if (currentUser.getShippingAddress() == null) {
            response.setMessage("Please add a shipping address to your account first");
            return new ApiResponseService(false, HttpStatus.BAD_REQUEST, response);
        }


        PaymentModel paymentMethod = paymentDAO.findByPaymentOption(orderDTO.getPaymentMethod());
        if (paymentMethod == null) {
            response.setMessage("Invalid payment method");
            return new ApiResponseService(false, HttpStatus.BAD_REQUEST, response);
        }
        

        OrderModel order = orderMapper.toModel(orderDTO, currentUser, cart);
        OrderModel savedOrder = orderDAO.createOrder(order);
        

        CartModel newCart = new CartModel();
        newCart.setUser(currentUser);
        currentUser.setCart(newCart);
        cartDAO.createCart(newCart);
        
        response.setMessage("Order placed successfully");
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
            response.setMessage("Order not found");
            return new ApiResponseService(false, HttpStatus.NOT_FOUND, response);
        }

        UserModel currentUser = authService.getCurrentUser();
        if (!currentUser.isAdmin() && !order.get().getUser().getId().equals(currentUser.getId())) {
            response.setMessage("No access to this order");
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
            response.setMessage("Order not found");
            return new ApiResponseService(false, HttpStatus.NOT_FOUND, response);
        }
        
        OrderModel order = orderOpt.get();
        order.setOrderStatus(status);
        
        OrderModel updatedOrder = orderDAO.updateOrder(order);
        
        response.setMessage("Order status updated successfully");
        response.setResult(updatedOrder);
        return new ApiResponseService(true, HttpStatus.OK, response);
    }
}