package com.example.app.controller;

import com.example.app.dto.ProductDto;
import com.example.app.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api") // Base path for products, specific category paths handled in methods
@Tag(name = "Product Management", description = "APIs for managing products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Operation(summary = "List products in a category", responses = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved product list for the category",
                     content = @Content(mediaType = "application/json", 
                                        schema = @Schema(implementation = ProductDto.class, type = "array"))),
        @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @GetMapping("/categories/{categoryId}/products")
    public ResponseEntity<List<ProductDto>> getProductsByCategoryId(
            @Parameter(description = "ID of the category to list products from") @PathVariable Long categoryId) {
        List<ProductDto> products = productService.getAllProductsByCategoryId(categoryId);
        return ResponseEntity.ok(products);
    }

    @Operation(summary = "Get product by ID", responses = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved product",
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductDto.class))),
        @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @GetMapping("/products/{id}")
    public ResponseEntity<ProductDto> getProductById(@Parameter(description = "ID of product to return") @PathVariable Long id) {
        ProductDto productDto = productService.getProductById(id);
        return ResponseEntity.ok(productDto);
    }

    @Operation(summary = "Add new product to a category", description = "Requires ADMIN role", responses = {
        @ApiResponse(responseCode = "201", description = "Product created and added to category successfully",
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @PostMapping("/categories/{categoryId}/products")
    public ResponseEntity<ProductDto> createProduct(
            @Parameter(description = "ID of the category to add product to") @PathVariable Long categoryId, 
            @Valid @RequestBody ProductDto productDto) {
        ProductDto createdProduct = productService.createProduct(categoryId, productDto);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    @Operation(summary = "Update product details", description = "Requires ADMIN role", responses = {
        @ApiResponse(responseCode = "200", description = "Product updated successfully",
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "404", description = "Product or (if specified) new Category not found")
    })
    @PutMapping("/products/{id}")
    public ResponseEntity<ProductDto> updateProduct(
            @Parameter(description = "ID of product to update") @PathVariable Long id, 
            @Valid @RequestBody ProductDto productDto) {
        // The ProductDto can optionally contain a categoryId if the user wants to move the product.
        // The service layer will handle this logic.
        ProductDto updatedProduct = productService.updateProduct(id, productDto);
        return ResponseEntity.ok(updatedProduct);
    }

    @Operation(summary = "Remove product", description = "Requires ADMIN role", responses = {
        @ApiResponse(responseCode = "204", description = "Product removed successfully"),
        @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deleteProduct(@Parameter(description = "ID of product to remove") @PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
} 