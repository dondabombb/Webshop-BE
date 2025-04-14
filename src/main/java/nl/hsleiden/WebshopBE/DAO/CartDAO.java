package nl.hsleiden.WebshopBE.DAO;

import nl.hsleiden.WebshopBE.DAO.repository.CartRepository;
import nl.hsleiden.WebshopBE.exceptions.EntryNotFoundException;
import nl.hsleiden.WebshopBE.model.CartModel;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CartDAO {

    private final CartRepository cartRepository;
    public CartDAO(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public Optional<CartModel> getCart(String cartId) {
        return this.cartRepository.findById(cartId);
    }

    public void createCart(CartModel cart) {
        this.cartRepository.save(cart);
    }

    public CartModel updateCart(CartModel cartUpdate) throws EntryNotFoundException {
        return this.cartRepository.saveAndFlush(cartUpdate);
    }

}
