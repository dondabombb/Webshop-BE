package nl.hsleiden.WebshopBE.controller;

import lombok.AllArgsConstructor;
import nl.hsleiden.WebshopBE.DAO.PaymentDAO;
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
            response.setMessage("Betaalmethode niet gevonden");
            return new ApiResponseService(false, HttpStatus.NOT_FOUND, response);
        }
        
        response.setResult(paymentMethod.get());
        return new ApiResponseService(true, HttpStatus.OK, response);
    }

    @PutMapping(value = ApiConstant.getPaymentMethod)
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseBody
    public ApiResponseService updatePaymentMethod(@PathVariable String paymentId, @RequestBody PaymentModel updatedPayment) {
        ApiResponse response = new ApiResponse();
        Optional<PaymentModel> existingPayment = paymentDAO.findById(paymentId);
        
        if (existingPayment.isEmpty()) {
            response.setMessage("Betaalmethode niet gevonden");
            return new ApiResponseService(false, HttpStatus.NOT_FOUND, response);
        }
        
        PaymentModel payment = existingPayment.get();
        payment.setPaymentOption(updatedPayment.getPaymentOption());
        PaymentModel savedPayment = paymentDAO.save(payment);
        
        response.setMessage("Betaalmethode succesvol bijgewerkt");
        response.setResult(savedPayment);
        return new ApiResponseService(true, HttpStatus.OK, response);
    }

    @DeleteMapping(value = ApiConstant.getPaymentMethod)
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseBody
    public ApiResponseService deletePaymentMethod(@PathVariable String paymentId) {
        ApiResponse response = new ApiResponse();
        
        if (!paymentDAO.existsById(paymentId)) {
            response.setMessage("Betaalmethode niet gevonden");
            return new ApiResponseService(false, HttpStatus.NOT_FOUND, response);
        }
        
        paymentDAO.deleteById(paymentId);
        response.setMessage("Betaalmethode succesvol verwijderd");
        return new ApiResponseService(true, HttpStatus.OK, response);
    }
}
