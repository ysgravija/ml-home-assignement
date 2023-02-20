package com.example.demo.controller;

import com.example.demo.controller.exception.FeatureNotFoundException;
import com.example.demo.entity.FeatureEntity;
import com.example.demo.model.GetFeatureInfo;
import com.example.demo.model.UpdateFeatureAccess;
import com.example.demo.repository.FeatureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/feature")
public class FeatureController {
    @Autowired
    private FeatureRepository featureRepository;

    @GetMapping
    public GetFeatureInfo getFeaturesByUser(@RequestParam String email, @RequestParam String featureName) {
        FeatureEntity featureEntity = featureRepository.getFeatureInfo(email, featureName).orElseThrow(
                () -> new FeatureNotFoundException()
        );
        return GetFeatureInfo.builder().canAccess(featureEntity.getIsEnable()).build();
    }

    @PostMapping
    public ResponseEntity<?> updateFeatureForUser(@Valid @RequestBody UpdateFeatureAccess request) {
        Optional<FeatureEntity> feature = featureRepository.findFeatureForUpdate(
                request.getEmail(), request.getFeatureName());

        FeatureEntity createOrUpdate = null;
        if (feature.isPresent()) {
            if (feature.get().getIsEnable() == request.isEnable()) {
                return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
            } else {
                createOrUpdate = feature.get();
                createOrUpdate.setIsEnable(request.isEnable());
            }
        } else {
            createOrUpdate = FeatureEntity.builder()
                    .email(request.getEmail())
                    .featureName(request.getFeatureName())
                    .build();
        }
        featureRepository.save(createOrUpdate);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
