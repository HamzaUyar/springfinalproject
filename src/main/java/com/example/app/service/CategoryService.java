package com.example.app.service;

import com.example.app.dto.CategoryDto;
import com.example.app.entity.Category;
import com.example.app.exception.ResourceNotFoundException;
import com.example.app.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional(readOnly = true)
    public List<CategoryDto> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CategoryDto getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID " + id));
        return convertToDto(category);
    }

    @Transactional
    public CategoryDto createCategory(CategoryDto categoryDto) {
        // Check for existing category with the same name (optional, can be handled by DB constraint too)
        categoryRepository.findByName(categoryDto.getName()).ifPresent(c -> {
            throw new IllegalArgumentException("Category with name '" + categoryDto.getName() + "' already exists.");
        });
        Category category = convertToEntity(categoryDto);
        Category savedCategory = categoryRepository.save(category);
        return convertToDto(savedCategory);
    }

    @Transactional
    public CategoryDto updateCategory(Long id, CategoryDto categoryDto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID " + id));
        
        // Check if new name conflicts with another existing category
        categoryRepository.findByName(categoryDto.getName()).ifPresent(existingCategory -> {
            if (!existingCategory.getId().equals(id)) {
                throw new IllegalArgumentException("Another category with name '" + categoryDto.getName() + "' already exists.");
            }
        });

        category.setName(categoryDto.getName());
        Category updatedCategory = categoryRepository.save(category);
        return convertToDto(updatedCategory);
    }

    @Transactional
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID " + id));
        // Consider implications if category has products. 
        // Current setup: CascadeType.ALL and orphanRemoval=true will delete associated products.
        categoryRepository.delete(category);
    }

    // --- Helper Methods for DTO/Entity Conversion ---
    private CategoryDto convertToDto(Category category) {
        CategoryDto dto = new CategoryDto();
        dto.setId(category.getId());
        dto.setName(category.getName());
        return dto;
    }

    private Category convertToEntity(CategoryDto categoryDto) {
        Category category = new Category();
        // ID is not set from DTO for new entities
        category.setName(categoryDto.getName());
        return category;
    }
} 