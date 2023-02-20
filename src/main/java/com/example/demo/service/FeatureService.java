package com.example.demo.service;

import com.example.demo.controller.exception.FeatureNotFoundException;
import com.example.demo.entity.FeatureEntity;
import com.example.demo.model.FeatureAccessResponse;
import com.example.demo.model.FeatureAccessRequest;
import com.example.demo.repository.FeatureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class FeatureService {
    @Autowired
    private FeatureRepository featureRepository;

    public FeatureAccessResponse getFeatureByUser(String email, String featureName) {
        FeatureEntity featureEntity = featureRepository.getFeatureInfo(email, featureName).orElseThrow(
                () -> new FeatureNotFoundException()
        );
        return FeatureAccessResponse.builder().canAccess(featureEntity.getIsEnable()).build();
    }

    @Transactional
    public boolean createOrUpdateFeatureForUser(FeatureAccessRequest request) {
        Optional<FeatureEntity> feature = featureRepository.findFeatureForUpdate(
                request.getEmail(), request.getFeatureName()
        );

        FeatureEntity entityToUpdate;
        if (feature.isPresent()) {
            if (feature.get().getIsEnable() == request.isEnable()) {
                return false;
            } else {
                entityToUpdate = feature.get();
                entityToUpdate.setIsEnable(request.isEnable());
            }
        } else {
            entityToUpdate = FeatureEntity.builder()
                    .email(request.getEmail())
                    .featureName(request.getFeatureName())
                    .isEnable(request.isEnable())
                    .build();
        }
        featureRepository.save(entityToUpdate);
        return true;
    }
}
