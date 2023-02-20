package com.example.demo;

import com.example.demo.entity.FeatureEntity;
import com.example.demo.repository.FeatureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class FeatureApplication {

	@Autowired
	private FeatureRepository featureRepository;

	public static void main(String[] args) {
		SpringApplication.run(FeatureApplication.class, args);
	}

	@PostConstruct
	private void initDb() {
		List<FeatureEntity> featureList = Arrays.asList(
				createFeature("kofuzi@gmail.com", "drawCircle", true),
				createFeature("kofuzi@gmail.com", "drawRectangle", false),
				createFeature("runner@gmail.com", "drawSquare", false),
				createFeature("runner@gmail.com", "drawLine", true),
				createFeature("modifyme@gmail.com", "drawCircle", true)
		);
		featureRepository.saveAllAndFlush(featureList);
	}

	private FeatureEntity createFeature(String email, String featureName, boolean enable) {
		return FeatureEntity.builder()
				.email(email)
				.featureName(featureName)
				.isEnable(enable)
				.build();
	}
}
