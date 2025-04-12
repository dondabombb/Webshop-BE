package nl.hsleiden.WebshopBE.DAO;

import nl.hsleiden.WebshopBE.model.PaymentModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentDAO extends JpaRepository<PaymentModel, String> {
    PaymentModel findByPaymentOption(String paymentOption);
} 