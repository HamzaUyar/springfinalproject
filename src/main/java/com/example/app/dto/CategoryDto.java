package com.example.app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryDto {
    private Long id;

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must be less than or equal to 100 characters")
    private String name;
} 