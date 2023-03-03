package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeatureAccessRequest {
    @NonNull
    @NotEmpty
    private String featureName;

    @NonNull
    @NotEmpty
    private String email;

    private boolean enable;
}
