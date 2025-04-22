package nl.hsleiden.WebshopBE.controller;

import javax.validation.Valid;
import lombok.AllArgsConstructor;
import nl.hsleiden.WebshopBE.DAO.CategoryDAO;
import nl.hsleiden.WebshopBE.DTO.CategoryDTO;
import nl.hsleiden.WebshopBE.model.CategoryModel;
import nl.hsleiden.WebshopBE.mapper.CategoryMapper;
import nl.hsleiden.WebshopBE.constant.ApiConstant;
import nl.hsleiden.WebshopBE.other.ApiResponse;
import nl.hsleiden.WebshopBE.service.ApiResponseService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping()
@Validated
@AllArgsConstructor
public class CatagoryController {
    private final CategoryDAO categoryDAO;
    private final CategoryMapper categoryMapper;

    @GetMapping(value = ApiConstant.getAllCategories)
    @ResponseBody
    public ApiResponseService getAllCategories() {
        ApiResponse response = new ApiResponse();
        List<CategoryModel> categories = categoryDAO.getAllCategories();
        response.setResult(categories);
        return new ApiResponseService(true, HttpStatus.OK, response);
    }

    @GetMapping(value = ApiConstant.getCategory)
    @ResponseBody
    public ApiResponseService getCategory(@PathVariable String categoryId) {
        ApiResponse response = new ApiResponse();
        Optional<CategoryModel> category = categoryDAO.getCategory(categoryId);
        
        if (category.isEmpty()) {
            response.setMessage("Category not found");
            return new ApiResponseService(false, HttpStatus.NOT_FOUND, response);
        }
        
        response.setResult(category.get());
        return new ApiResponseService(true, HttpStatus.OK, response);
    }

    @PostMapping(value = ApiConstant.getAllCategories)
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseBody
    public ApiResponseService createCategory(@Valid @RequestBody CategoryDTO categoryDTO) {
        ApiResponse response = new ApiResponse();
        
        Optional<CategoryModel> existingCategory = categoryDAO.getCategoryByName(categoryDTO.getName());
        if (existingCategory.isPresent()) {
            response.setMessage("Category with this name already exists");
            return new ApiResponseService(false, HttpStatus.CONFLICT, response);
        }
        
        CategoryModel category = categoryMapper.toModel(categoryDTO);
        CategoryModel savedCategory = categoryDAO.createCategory(category);
        
        response.setMessage("Category created successfully");
        response.setResult(savedCategory);
        return new ApiResponseService(true, HttpStatus.CREATED, response);
    }

    @PutMapping(value = ApiConstant.getCategory)
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseBody
    public ApiResponseService updateCategory(@PathVariable String categoryId, @Valid @RequestBody CategoryDTO categoryDTO) {
        ApiResponse response = new ApiResponse();
        
        Optional<CategoryModel> existingCategory = categoryDAO.getCategory(categoryId);
        if (existingCategory.isEmpty()) {
            response.setMessage("Category not found");
            return new ApiResponseService(false, HttpStatus.NOT_FOUND, response);
        }
        
        if (categoryDTO.getName() != null) {
            Optional<CategoryModel> categoryWithName = categoryDAO.getCategoryByName(categoryDTO.getName());
            if (categoryWithName.isPresent() && !categoryWithName.get().getId().equals(categoryId)) {
                response.setMessage("Category with this name already exists");
                return new ApiResponseService(false, HttpStatus.CONFLICT, response);
            }
        }
        
        CategoryModel updatedCategory = categoryMapper.mergeCategory(existingCategory.get(), categoryDTO);
        CategoryModel savedCategory = categoryDAO.updateCategory(updatedCategory);
        
        response.setMessage("Category updated successfully");
        response.setResult(savedCategory);
        return new ApiResponseService(true, HttpStatus.OK, response);
    }

    @DeleteMapping(value = ApiConstant.getCategory)
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseBody
    public ApiResponseService deleteCategory(@PathVariable String categoryId) {
        ApiResponse response = new ApiResponse();
        
        Optional<CategoryModel> existingCategory = categoryDAO.getCategory(categoryId);
        if (existingCategory.isEmpty()) {
            response.setMessage("Category not found");
            return new ApiResponseService(false, HttpStatus.NOT_FOUND, response);
        }
        
        categoryDAO.deleteCategory(categoryId);
        
        response.setMessage("Category deleted successfully");
        return new ApiResponseService(true, HttpStatus.OK, response);
    }
}
