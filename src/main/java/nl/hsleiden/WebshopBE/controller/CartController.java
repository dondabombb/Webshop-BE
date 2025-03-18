package nl.hsleiden.WebshopBE.controller;

import lombok.AllArgsConstructor;
import nl.hsleiden.WebshopBE.DAO.CartProductDAO;
import nl.hsleiden.WebshopBE.DAO.ProductDAO;
import nl.hsleiden.WebshopBE.DTO.CartProductDTO;
import nl.hsleiden.WebshopBE.constant.ApiConstant;
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

    @GetMapping(value = ApiConstant.getCart)
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    public ApiResponseService getCart() {
        ApiResponse response = new ApiResponse();
        
        UserModel currentUser = authService.getCurrentUser();
        CartModel cart = currentUser.getCart();
        
        response.setResult(cart);
        return new ApiResponseService(true, HttpStatus.OK, response);
    }

    @PostMapping(value = ApiConstant.addProductToCart)
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    public ApiResponseService addProductToCart(@Valid @RequestBody CartProductDTO cartProductDTO) {
        ApiResponse response = new ApiResponse();
        
        UserModel currentUser = authService.getCurrentUser();
        CartModel cart = currentUser.getCart();
        
        Optional<ProductModel> productOpt = productDAO.getProduct(cartProductDTO.getProductId());
        if (!productOpt.isPresent()) {
            response.setMessage("Product niet gevonden");
            return new ApiResponseService(false, HttpStatus.NOT_FOUND, response);
        }
        
        ProductModel product = productOpt.get();
        
        // Controleer of product al in winkelwagen zit
        Optional<CartProductModel> existingCartProduct = cartProductDAO.getCartProduct(cart.getId(), product.getId());
        
        if (existingCartProduct.isPresent()) {
            // Update hoeveelheid
            CartProductModel cartProduct = existingCartProduct.get();
            cartProduct.setQuantity(cartProduct.getQuantity() + cartProductDTO.getQuantity());
            cartProductDAO.updateCartProduct(cartProduct);
            response.setMessage("Product hoeveelheid bijgewerkt in winkelwagen");
        } else {
            // Voeg nieuw product toe
            CartProductModel cartProduct = new CartProductModel(cart, product, cartProductDTO.getQuantity());
            cart.addProduct(cartProduct);
            cartProductDAO.createCartProduct(cartProduct);
            response.setMessage("Product toegevoegd aan winkelwagen");
        }
        
        response.setResult(cart);
        return new ApiResponseService(true, HttpStatus.OK, response);
    }

    @PutMapping(value = ApiConstant.updateCartProduct)
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    public ApiResponseService updateCartProduct(@PathVariable String productId, @RequestParam int quantity) {
        ApiResponse response = new ApiResponse();
        
        UserModel currentUser = authService.getCurrentUser();
        CartModel cart = currentUser.getCart();

        Optional<ProductModel> foundProduct = this.productDAO.getProduct(productId);
        if (!foundProduct.isPresent()){
            response.setMessage("Dit product bestaat niet");
            return new ApiResponseService(false, HttpStatus.NOT_FOUND, response);
        }

        ProductModel product = foundProduct.get();

        Optional<CartProductModel> cartProduct = this.cartProductDAO.getCartProduct(cart.getId(), product.getId());
        if(!cartProduct.isPresent()){
            response.setMessage("Dit product bevindt zich niet in de winkelwagen");
            return new ApiResponseService(false, HttpStatus.NOT_FOUND, response);
        }

        if(quantity <= 0){
            this.cartProductDAO.deleteCartProduct(cartProduct.get());
            response.setMessage("Product is verwijderd uit winkelwagen");
        } else {
            cartProduct.get().setQuantity(quantity);
            this.cartProductDAO.updateCartProduct(cartProduct.get());
            response.setMessage("Product hoeveelheid is aangepast");
        }

        response.setResult(cart);
        return new ApiResponseService(true, HttpStatus.OK, response);
    }
}
