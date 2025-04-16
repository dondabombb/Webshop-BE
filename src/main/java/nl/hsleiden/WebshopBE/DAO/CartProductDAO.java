package nl.hsleiden.WebshopBE.DAO;

import nl.hsleiden.WebshopBE.DAO.repository.CartProductRepository;
import nl.hsleiden.WebshopBE.model.CartProductModel;
import org.springframework.stereotype.Component;

import java.util.Optional;
@Component
public class CartProductDAO {
    private final CartProductRepository cartProductRepository;

    public CartProductDAO(CartProductRepository cartProductRepository) {
        this.cartProductRepository = cartProductRepository;
    }

    public Optional<CartProductModel> getCartProduct(String cartId, String productId) {
        return this.cartProductRepository.findByCartIdAndProductId(cartId, productId);
    }

    public CartProductModel createCartProduct(CartProductModel cartProduct) {
        return this.cartProductRepository.save(cartProduct);
    }

    public void deleteCartProduct(CartProductModel cartProduct) {
        cartProduct.getCart().removeProduct(cartProduct);
        this.cartProductRepository.deleteById(cartProduct.getId());
    }

    public void updateCartProduct(CartProductModel cartProduct) {
        this.cartProductRepository.saveAndFlush(cartProduct);
    }

    public void deleteAllByCartId(String cartId){
        this.cartProductRepository.deleteAllByCartId(cartId);
    };
}
