package com.cloudapp.cloud_app.cloud_app.Dto;

import com.cloudapp.cloud_app.cloud_app.model.ServiceCategory.Servicecategory;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceRequestDto {

    @NotBlank(message = "Description cannot be empty")
    private String description;

    @NotNull(message = "Customer ID is required")
    private Long customerId;

    @NotNull(message = "Service category is required")
    private Servicecategory serviceCategory;

}