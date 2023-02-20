package com.example.demo.model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
public class FeatureAccessRequest {
    @NonNull
    @NotEmpty
    private String featureName;

    @NonNull
    @NotEmpty
    private String email;

    private boolean enable;
}
