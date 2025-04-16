package nl.hsleiden.WebshopBE.controller;

import lombok.AllArgsConstructor;
import nl.hsleiden.WebshopBE.DAO.CartDAO;
import nl.hsleiden.WebshopBE.DAO.CartProductDAO;
import nl.hsleiden.WebshopBE.DAO.ProductDAO;
import nl.hsleiden.WebshopBE.DTO.CartProductDTO;
import nl.hsleiden.WebshopBE.constant.ApiConstant;
import nl.hsleiden.WebshopBE.exceptions.EntryNotFoundException;
import nl.hsleiden.WebshopBE.model.CartModel;
import nl.hsleiden.WebshopBE.model.CartProductModel;
import nl.hsleiden.WebshopBE.model.ProductModel;
import nl.hsleiden.WebshopBE.model.UserModel;
import nl.hsleiden.WebshopBE.other.ApiResponse;
import nl.hsleiden.WebshopBE.service.ApiResponseService;
import nl.hsleiden.WebshopBE.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping()
@Validated
@AllArgsConstructor
public class CartController {

    private final CartProductDAO cartProductDAO;
    private final ProductDAO productDAO;
    private final AuthService authService;
    private final CartDAO cartDAO;

//    @RolesAllowed({"USER", "ADMIN"})
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping(value = ApiConstant.getCart)
    @ResponseBody
    public ApiResponseService getCart() {
        ApiResponse response = new ApiResponse();

        UserModel currentUser = authService.getCurrentUser();
        CartModel cart = currentUser.getCart();

        response.setResult(cart);
        return new ApiResponseService(true, HttpStatus.OK, response);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PostMapping(value = ApiConstant.addProductToCart)
    @ResponseBody
    public ApiResponseService addProductToCart(@RequestBody @Valid CartProductDTO cartProductDTO) throws EntryNotFoundException {
        ApiResponse response = new ApiResponse();

        Optional<CartModel> foundCart = this.cartDAO.getCart(cartProductDTO.getCartId());
        if (!foundCart.isPresent()){
            response.setMessage("Kan de huidige winkelwagen niet vinden");
            return new ApiResponseService(false, HttpStatus.NOT_FOUND, response);
        }

        Optional<ProductModel> foundProduct = this.productDAO.getProduct(cartProductDTO.getProductId());
        if (!foundProduct.isPresent()){
            response.setMessage("Dit product bestaat niet");
            return new ApiResponseService(false, HttpStatus.NOT_FOUND, response);
        }

        CartModel cart = foundCart.get();
        ProductModel product = foundProduct.get();
        int quantity = cartProductDTO.getQuantity();

        CartProductModel newCartProduct = new CartProductModel(
                cart,
                product,
                quantity
        );

        Optional<CartProductModel> cartProduct = this.cartProductDAO.getCartProduct(cart.getId(), product.getId());
        if(cartProduct.isPresent()){
            int oldQuantity = cartProduct.get().getQuantity();
            int newQuantity = oldQuantity + quantity;
            cartProduct.get().setQuantity(newQuantity);
        } else {
            CartProductModel savedCartProduct = this.cartProductDAO.createCartProduct(newCartProduct);
            cart.addProduct(savedCartProduct);
        }

        CartModel updatedCart = this.cartDAO.updateCart(cart);
        response.setResult(updatedCart);
        response.setMessage("Product is toegevoegd aan winkelwagen");
        return new ApiResponseService(true, HttpStatus.CREATED, response);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @DeleteMapping(value = ApiConstant.removeProductFromCart)
    @ResponseBody
    public ApiResponseService removeProductFromCart(@PathVariable String cartId, @PathVariable String productId) throws EntryNotFoundException {
        ApiResponse response = new ApiResponse();

        Optional<CartModel> foundCart = this.cartDAO.getCart(cartId);
        if (!foundCart.isPresent()){
            response.setMessage("Kan de huidige winkelwagen niet vinden");
            return new ApiResponseService(false, HttpStatus.NOT_FOUND, response);
        }

        Optional<ProductModel> foundProduct = this.productDAO.getProduct(productId);
        if (!foundProduct.isPresent()){
            response.setMessage("Dit product bestaat niet");
            return new ApiResponseService(false, HttpStatus.NOT_FOUND, response);
        }

        CartModel cart = foundCart.get();
        ProductModel product = foundProduct.get();

        Optional<CartProductModel> cartProduct = this.cartProductDAO.getCartProduct(cart.getId(), product.getId());
        if(!cartProduct.isPresent()){
            response.setMessage("Dit product bevindt zich niet in de winkelwagen");
            return new ApiResponseService(false, HttpStatus.NOT_FOUND, response);
        }

        this.cartProductDAO.deleteCartProduct(cartProduct.get());
        response.setMessage("Product is verwijderd uit de winkelwagen");
        response.setResult(this.cartDAO.getCart(cartId));
        return new ApiResponseService(true, HttpStatus.ACCEPTED, response);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PutMapping(value = ApiConstant.incrementProduct)
    @ResponseBody
    public ApiResponseService incrementProduct(@RequestBody @Valid CartProductDTO cartProductDTO) throws EntryNotFoundException {
        ApiResponse response = new ApiResponse();

        Optional<CartProductModel> cartProduct = this.cartProductDAO.getCartProduct(cartProductDTO.getCartId(), cartProductDTO.getProductId());
        if (!cartProduct.isPresent()) {
            response.setMessage("Product not found in cart");
            return new ApiResponseService(false, HttpStatus.NOT_FOUND, response);
        }

        CartProductModel product = cartProduct.get();
        product.setQuantity(product.getQuantity() + cartProductDTO.getQuantity());
        this.cartProductDAO.updateCartProduct(product);

        response.setMessage("Product quantity increased");
        response.setResult(product.getCart());
        return new ApiResponseService(true, HttpStatus.OK, response);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PutMapping(value = ApiConstant.decrementProduct)
    @ResponseBody
    public ApiResponseService decrementProduct(@RequestBody @Valid CartProductDTO cartProductDTO) throws EntryNotFoundException {
        ApiResponse response = new ApiResponse();

        Optional<CartProductModel> cartProduct = this.cartProductDAO.getCartProduct(cartProductDTO.getCartId(), cartProductDTO.getProductId());
        if (!cartProduct.isPresent()) {
            response.setMessage("Product not found in cart");
            return new ApiResponseService(false, HttpStatus.NOT_FOUND, response);
        }

        CartProductModel product = cartProduct.get();
        if (product.getQuantity() <= 1) {
            this.cartProductDAO.deleteCartProduct(product);
            response.setMessage("Product removed from cart");
        } else {
            product.setQuantity(product.getQuantity() - cartProductDTO.getQuantity());
            this.cartProductDAO.updateCartProduct(product);
            response.setMessage("Product quantity decreased");
        }

        response.setResult(product.getCart());
        return new ApiResponseService(true, HttpStatus.OK, response);
    }
}
