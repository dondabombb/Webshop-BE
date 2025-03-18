package nl.hsleiden.WebshopBE.controller;

import lombok.AllArgsConstructor;
import nl.hsleiden.WebshopBE.DAO.ProductDAO;
import nl.hsleiden.WebshopBE.DAO.CartDAO;
import nl.hsleiden.WebshopBE.DAO.CartProductDAO;
import nl.hsleiden.WebshopBE.DTO.CartProductDTO;
import nl.hsleiden.WebshopBE.DTO.ProductDTO;
import nl.hsleiden.WebshopBE.constant.ApiConstant;
import nl.hsleiden.WebshopBE.exceptions.EntryNotFoundException;
import nl.hsleiden.WebshopBE.mapper.ProductMapper;
import nl.hsleiden.WebshopBE.model.CartModel;
import nl.hsleiden.WebshopBE.model.CartProductModel;
import nl.hsleiden.WebshopBE.model.ProductModel;
import nl.hsleiden.WebshopBE.other.ApiResponse;
import nl.hsleiden.WebshopBE.service.ApiResponseService;
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
public class ProductController {
    private final ProductDAO productDAO;
    private final ProductMapper productMapper;
    private CartDAO cartDAO;
    private CartProductDAO cartProductDAO;

    @GetMapping(value = ApiConstant.getAllProducts)
    @ResponseBody
    public ApiResponseService getAllProducts() {
        ApiResponse response = new ApiResponse();
        
        List<ProductModel> products = productDAO.getAllProducts();
        
        response.setResult(products);
        return new ApiResponseService(true, HttpStatus.OK, response);
    }

    @GetMapping(value = ApiConstant.getProduct)
    @ResponseBody
    public ApiResponseService getProduct(@PathVariable String productId) {
        ApiResponse response = new ApiResponse();
        
        Optional<ProductModel> product = productDAO.getProduct(productId);
        if (!product.isPresent()) {
            response.setMessage("Product niet gevonden");
            return new ApiResponseService(false, HttpStatus.NOT_FOUND, response);
        }
        
        response.setResult(product.get());
        return new ApiResponseService(true, HttpStatus.OK, response);
    }

    @PostMapping(value = ApiConstant.getAllProducts)
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseBody
    public ApiResponseService createProduct(@Valid @RequestBody ProductDTO productDTO) {
        ApiResponse response = new ApiResponse();
        
        // Controleer of product met dezelfde naam al bestaat
        Optional<ProductModel> existingProduct = productDAO.getProduct(productDTO.getName());
        if (existingProduct.isPresent()) {
            response.setMessage("Product met deze naam bestaat al");
            return new ApiResponseService(false, HttpStatus.CONFLICT, response);
        }
        
        ProductModel product = productMapper.toModel(productDTO);
        ProductModel savedProduct = productDAO.createProduct(product);
        
        response.setMessage("Product succesvol aangemaakt");
        response.setResult(savedProduct);
        return new ApiResponseService(true, HttpStatus.CREATED, response);
    }

    @PutMapping(value = ApiConstant.getProduct)
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseBody
    public ApiResponseService updateProduct(@PathVariable String productId, @Valid @RequestBody ProductDTO productDTO) {
        ApiResponse response = new ApiResponse();
        
        Optional<ProductModel> existingProduct = productDAO.getProduct(productId);
        if (!existingProduct.isPresent()) {
            response.setMessage("Product niet gevonden");
            return new ApiResponseService(false, HttpStatus.NOT_FOUND, response);
        }
        
        // Controleer of nieuwe naam al in gebruik is door een ander product
        if (productDTO.getName() != null) {
            Optional<ProductModel> productWithName = productDAO.getProduct(productDTO.getName());
            if (productWithName.isPresent() && !productWithName.get().getId().equals(productId)) {
                response.setMessage("Product met deze naam bestaat al");
                return new ApiResponseService(false, HttpStatus.CONFLICT, response);
            }
        }
        
        ProductModel updatedProduct = productMapper.mergeProduct(existingProduct.get(), productDTO);
        ProductModel savedProduct = productDAO.updateProduct(updatedProduct);
        
        response.setMessage("Product succesvol bijgewerkt");
        response.setResult(savedProduct);
        return new ApiResponseService(true, HttpStatus.OK, response);
    }

    @DeleteMapping(value = ApiConstant.getProduct)
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseBody
    public ApiResponseService deleteProduct(@PathVariable String productId) {
        ApiResponse response = new ApiResponse();
        
        Optional<ProductModel> existingProduct = productDAO.getProduct(productId);
        if (!existingProduct.isPresent()) {
            response.setMessage("Product niet gevonden");
            return new ApiResponseService(false, HttpStatus.NOT_FOUND, response);
        }
        
        productDAO.deleteProduct(productId);
        
        response.setMessage("Product succesvol verwijderd");
        return new ApiResponseService(true, HttpStatus.OK, response);
    }

    @PostMapping(value = ApiConstant.addProductToCart, consumes = {"application/json"})
    @ResponseBody
    public ApiResponseService addProductToCart(@PathVariable String cartId, @RequestBody @Valid CartProductDTO cartProductDTO) throws EntryNotFoundException {
        ApiResponse response = new ApiResponse();

        Optional<CartModel> foundCart = this.cartDAO.getCart(cartId);
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

    @DeleteMapping(value = ApiConstant.getProductFromCart)
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
        response.setResult(cart);
        return new ApiResponseService(true, HttpStatus.ACCEPTED, response);
    }
}
