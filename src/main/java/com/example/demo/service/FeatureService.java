package com.example.demo.service;

import com.example.demo.controller.exception.FeatureNotFoundException;
import com.example.demo.entity.FeatureEntity;
import com.example.demo.model.FeatureAccessResponse;
import com.example.demo.repository.FeatureRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@AllArgsConstructor
public class FeatureService {
    private final FeatureRepository featureRepository;

    public FeatureAccessResponse getFeatureByUser(String email, String featureName) {
        FeatureEntity featureEntity = featureRepository.getFeatureInfo(email, featureName).orElseThrow(
                () -> new FeatureNotFoundException()
        );
        return FeatureAccessResponse.builder().canAccess(featureEntity.getIsEnable()).build();
    }

    @Transactional
    public boolean updateUserFeatureAccess(String email, String featureName, boolean isEnable) {
        Optional<FeatureEntity> feature = featureRepository.findFeatureForUpdate(email, featureName);
        FeatureEntity entityToUpdate;
        if (feature.isPresent()) {
            if (feature.get().getIsEnable() == isEnable) {
                return false;
            } else {
                entityToUpdate = feature.get();
                entityToUpdate.setIsEnable(isEnable);
            }
        } else {
            entityToUpdate = FeatureEntity.builder()
                    .email(email)
                    .featureName(featureName)
                    .isEnable(isEnable)
                    .build();
        }
        featureRepository.save(entityToUpdate);
        return true;
    }
}
