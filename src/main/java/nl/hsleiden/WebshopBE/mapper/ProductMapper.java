package nl.hsleiden.WebshopBE.mapper;

import nl.hsleiden.WebshopBE.DTO.ProductDTO;
import nl.hsleiden.WebshopBE.model.ProductModel;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public ProductModel toModel(ProductDTO dto) {
        ProductModel product = new ProductModel();
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setImageUrl(dto.getImageUrl());
        product.setPrice(dto.getPrice());
        product.setStock(100); // Default waarde
        product.setActive(true);
        return product;
    }

    public ProductModel mergeProduct(ProductModel product, ProductDTO dto) {
        if (dto.getName() != null) {
            product.setName(dto.getName());
        }
        if (dto.getDescription() != null) {
            product.setDescription(dto.getDescription());
        }
        if (dto.getImageUrl() != null) {
            product.setImageUrl(dto.getImageUrl());
        }
        if (dto.getPrice() != null) {
            product.setPrice(dto.getPrice());
        }
        return product;
    }

}
