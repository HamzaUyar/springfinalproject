package com.example.app.controller;

import com.example.app.dto.CategoryDto;
import com.example.app.service.CategoryService;
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
@RequestMapping("/api/categories")
@Tag(name = "Category Management", description = "APIs for managing product categories")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Operation(summary = "List all categories", responses = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved list", 
                     content = @Content(mediaType = "application/json", 
                                        schema = @Schema(implementation = CategoryDto.class, type = "array")))
    })
    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        List<CategoryDto> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @Operation(summary = "Get category by ID", responses = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved category", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = CategoryDto.class))),
        @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getCategoryById(@Parameter(description = "ID of category to return") @PathVariable Long id) {
        CategoryDto categoryDto = categoryService.getCategoryById(id);
        return ResponseEntity.ok(categoryDto);
    }

    @Operation(summary = "Create new category", description = "Requires ADMIN role", responses = {
        @ApiResponse(responseCode = "201", description = "Category created successfully", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = CategoryDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDto) {
        CategoryDto createdCategory = categoryService.createCategory(categoryDto);
        return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
    }

    @Operation(summary = "Update category name", description = "Requires ADMIN role", responses = {
        @ApiResponse(responseCode = "200", description = "Category updated successfully",
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = CategoryDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<CategoryDto> updateCategory(@Parameter(description = "ID of category to update") @PathVariable Long id, 
                                                    @Valid @RequestBody CategoryDto categoryDto) {
        CategoryDto updatedCategory = categoryService.updateCategory(id, categoryDto);
        return ResponseEntity.ok(updatedCategory);
    }

    @Operation(summary = "Delete category", description = "Requires ADMIN role. Deleting a category will also delete its associated products.", responses = {
        @ApiResponse(responseCode = "204", description = "Category deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@Parameter(description = "ID of category to delete") @PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
} 