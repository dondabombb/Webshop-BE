package nl.hsleiden.WebshopBE.controller;

import lombok.AllArgsConstructor;
import nl.hsleiden.WebshopBE.DAO.PaymentDAO;
import nl.hsleiden.WebshopBE.DTO.PaymentDTO;
import nl.hsleiden.WebshopBE.constant.ApiConstant;
import nl.hsleiden.WebshopBE.model.PaymentModel;
import nl.hsleiden.WebshopBE.other.ApiResponse;
import nl.hsleiden.WebshopBE.service.ApiResponseService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping()
@Validated
@AllArgsConstructor
public class PaymentController {

    private final PaymentDAO paymentDAO;

    @GetMapping(value = ApiConstant.getAllPaymentMethods)
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    public ApiResponseService getAllPaymentMethods() {
        ApiResponse response = new ApiResponse();
        List<PaymentModel> paymentMethods = paymentDAO.findAll();
        response.setResult(paymentMethods);
        return new ApiResponseService(true, HttpStatus.OK, response);
    }

    @GetMapping(value = ApiConstant.getPaymentMethod)
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    public ApiResponseService getPaymentMethod(@PathVariable String paymentId) {
        ApiResponse response = new ApiResponse();
        Optional<PaymentModel> paymentMethod = paymentDAO.findById(paymentId);
        
        if (paymentMethod.isEmpty()) {
            response.setMessage("Payment method not found");
            return new ApiResponseService(false, HttpStatus.NOT_FOUND, response);
        }
        
        response.setResult(paymentMethod.get());
        return new ApiResponseService(true, HttpStatus.OK, response);
    }

    @PutMapping(value = ApiConstant.getPaymentMethod)
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseBody
    public ApiResponseService updatePaymentMethod(@PathVariable String paymentId, @RequestBody PaymentDTO updatedPayment) {
        ApiResponse response = new ApiResponse();
        Optional<PaymentModel> existingPayment = paymentDAO.findById(paymentId);
        
        if (existingPayment.isEmpty()) {
            response.setMessage("Payment method not found");
            return new ApiResponseService(false, HttpStatus.NOT_FOUND, response);
        }
        
        PaymentModel payment = existingPayment.get();
        payment.setPaymentOption(updatedPayment.getPayment());
        PaymentModel savedPayment = paymentDAO.save(payment);
        
        response.setMessage("Payment method successfully updated");
        response.setResult(savedPayment);
        return new ApiResponseService(true, HttpStatus.OK, response);
    }

    @DeleteMapping(value = ApiConstant.getPaymentMethod)
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseBody
    public ApiResponseService deletePaymentMethod(@PathVariable String paymentId) {
        ApiResponse response = new ApiResponse();
        
        if (!paymentDAO.existsById(paymentId)) {
            response.setMessage("Payment method not found");
            return new ApiResponseService(false, HttpStatus.NOT_FOUND, response);
        }
        
        paymentDAO.deleteById(paymentId);
        response.setMessage("Payment method successfully deleted");
        return new ApiResponseService(true, HttpStatus.OK, response);
    }
    
    @PostMapping(value = ApiConstant.getAllPaymentMethods)
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseBody
    public ApiResponseService createPayment(@RequestBody String newPayment) {
        ApiResponse response = new ApiResponse();
        
        try {
            PaymentModel payment = new PaymentModel();
            payment.setPaymentOption(newPayment);
            PaymentModel savedPayment = paymentDAO.save(payment);
            response.setMessage("Payment method successfully created");
            response.setResult(savedPayment);
            return new ApiResponseService(true, HttpStatus.CREATED, response);
        } catch (Exception e) {
            response.setMessage("Error creating payment method: " + e.getMessage());
            return new ApiResponseService(false, HttpStatus.BAD_REQUEST, response);
        }
    }
}
