package nl.hsleiden.WebshopBE.mapper;

import nl.hsleiden.WebshopBE.DAO.ProductDAO;
import nl.hsleiden.WebshopBE.model.ProductModel;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
@Component
public class CartMapper {
    private ProductDAO productDAO;

    public CartMapper(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    public Set<ProductModel> getAllProducts(String[] productIds) {
        Set<ProductModel> products = new HashSet<>();
        if (productIds != null) {
            products = Arrays.stream(productIds)
                    .map(productId -> this.productDAO.getProduct(productId).orElse(null))
                    .collect(Collectors.toSet());
        }

        return products;
    }

}
