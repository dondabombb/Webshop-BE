package nl.hsleiden.WebshopBE.mapper;

import nl.hsleiden.WebshopBE.DTO.CategoryDTO;
import nl.hsleiden.WebshopBE.model.CategoryModel;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {
    public CategoryModel toModel(CategoryDTO dto) {
        CategoryModel category = new CategoryModel();
        category.setName(dto.getName());
        return category;
    }

    public CategoryModel mergeCategory(CategoryModel category, CategoryDTO dto) {
        if (dto.getName() != null) {
            category.setName(dto.getName());
        }
        return category;
    }
}