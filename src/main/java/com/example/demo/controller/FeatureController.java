package com.example.demo.controller;

import com.example.demo.model.FeatureAccessResponse;
import com.example.demo.model.FeatureAccessRequest;
import com.example.demo.service.FeatureService;
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
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/feature")
public class FeatureController {
    @Autowired
    private FeatureService featureService;

    @GetMapping
    public FeatureAccessResponse getFeaturesByUser(@NotNull @RequestParam String email,
                                                   @NotNull @RequestParam String featureName) {
        return featureService.getFeatureByUser(email, featureName);
    }

    @PostMapping
    public ResponseEntity<?> createOrUpdateFeatureForUser(@Valid @RequestBody FeatureAccessRequest request) {
        return featureService.createOrUpdateFeatureForUser(request)
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }
}
