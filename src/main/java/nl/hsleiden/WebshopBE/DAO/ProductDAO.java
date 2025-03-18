package nl.hsleiden.WebshopBE.DAO;

import nl.hsleiden.WebshopBE.DAO.repository.ProductRepository;
import nl.hsleiden.WebshopBE.mapper.ProductMapper;
import nl.hsleiden.WebshopBE.model.ProductModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Component
public class ProductDAO {

    private final ProductRepository productRepository;

    public ProductDAO(ProductRepository productRepository, ProductMapper mapper) {
        this.productRepository = productRepository;
    }

    public Optional<ProductModel> getProduct(String productId) {
        return this.productRepository.findById(productId);
    }

    public List<ProductModel> getAllProducts() {
        return this.productRepository.findAll();
    }

    public ProductModel createProduct(ProductModel product) {
        return this.productRepository.save(product);
    }

    public ProductModel updateProduct(ProductModel product) {
        return this.productRepository.saveAndFlush(product);
    }

    public void deleteProduct(String productId) {
        this.productRepository.findById(productId).get();
        this.productRepository.deleteById(productId);
    }
}
