package com.example.demo.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateFeatureAccess {
    private String featureName;

    private String email;

    private boolean enable;
}
