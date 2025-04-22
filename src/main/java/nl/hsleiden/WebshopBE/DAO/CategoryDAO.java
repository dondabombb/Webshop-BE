package nl.hsleiden.WebshopBE.DAO;

import nl.hsleiden.WebshopBE.DAO.repository.CategoryRepository;
import nl.hsleiden.WebshopBE.model.CategoryModel;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class CategoryDAO {
    private final CategoryRepository categoryRepository;

    public CategoryDAO(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Optional<CategoryModel> getCategory(String categoryId) {
        return this.categoryRepository.findById(categoryId);
    }

    public Optional<CategoryModel> getCategoryByName(String name) {
        return this.categoryRepository.findByName(name);
    }

    public List<CategoryModel> getAllCategories() {
        return this.categoryRepository.findAll();
    }

    public CategoryModel createCategory(CategoryModel category) {
        return this.categoryRepository.save(category);
    }

    public CategoryModel updateCategory(CategoryModel category) {
        return this.categoryRepository.saveAndFlush(category);
    }

    public void deleteCategory(String categoryId) {
        this.categoryRepository.deleteById(categoryId);
    }
}