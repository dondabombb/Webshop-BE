package nl.hsleiden.WebshopBE.seeder;

import nl.hsleiden.WebshopBE.DAO.PaymentDAO;
import nl.hsleiden.WebshopBE.model.PaymentModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class PaymentMethodSeeder implements CommandLineRunner {

    private final PaymentDAO paymentDAO;

    @Autowired
    public PaymentMethodSeeder(PaymentDAO paymentDAO) {
        this.paymentDAO = paymentDAO;
    }

    @Override
    public void run(String... args) {
        if (paymentDAO.count() == 0) {
            PaymentModel paypal = new PaymentModel();
            paypal.setPaymentOption("Paypal");
            paymentDAO.save(paypal);

            PaymentModel creditcard = new PaymentModel();
            creditcard.setPaymentOption("Creditcard");
            paymentDAO.save(creditcard);

            PaymentModel ideal = new PaymentModel();
            ideal.setPaymentOption("IDEAL");
            paymentDAO.save(ideal);
        }
    }
} 