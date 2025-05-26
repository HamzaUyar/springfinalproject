package com.example.app.service;

import com.example.app.dto.ProductDto;
import com.example.app.entity.Category;
import com.example.app.entity.Product;
import com.example.app.exception.ResourceNotFoundException;
import com.example.app.repository.CategoryRepository;
import com.example.app.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository; // To fetch Category entity

    @Autowired
    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional(readOnly = true)
    public List<ProductDto> getAllProductsByCategoryId(Long categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new ResourceNotFoundException("Category not found with ID " + categoryId + " when fetching products.");
        }
        return productRepository.findByCategoryId(categoryId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProductDto getProductById(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID " + productId));
        return convertToDto(product);
    }

    @Transactional
    public ProductDto createProduct(Long categoryId, ProductDto productDto) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID " + categoryId + " when creating product."));
        Product product = convertToEntity(productDto, category);
        Product savedProduct = productRepository.save(product);
        return convertToDto(savedProduct);
    }

    @Transactional
    public ProductDto updateProduct(Long productId, ProductDto productDto) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID " + productId));

        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());

        // If categoryId is provided in DTO and is different, update product's category
        if (productDto.getCategoryId() != null && !productDto.getCategoryId().equals(product.getCategory().getId())) {
            Category newCategory = categoryRepository.findById(productDto.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("New category not found with ID " + productDto.getCategoryId()));
            product.setCategory(newCategory);
        }
        
        Product updatedProduct = productRepository.save(product);
        return convertToDto(updatedProduct);
    }

    @Transactional
    public void deleteProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID " + productId));
        productRepository.delete(product);
    }

    // --- Helper Methods for DTO/Entity Conversion ---
    private ProductDto convertToDto(Product product) {
        ProductDto dto = new ProductDto();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setPrice(product.getPrice());
        dto.setCategoryId(product.getCategory().getId());
        return dto;
    }

    private Product convertToEntity(ProductDto productDto, Category category) {
        Product product = new Product();
        // ID is not set from DTO
        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());
        product.setCategory(category);
        return product;
    }
} 