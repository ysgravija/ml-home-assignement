package com.example.demo.controller;

import com.example.demo.entity.FeatureEntity;
import com.example.demo.model.FeatureAccessRequest;
import com.example.demo.repository.FeatureRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class FeatureControllerTest {

    @Autowired
    private FeatureRepository featureRepository;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void addData() {
        List<FeatureEntity> featureList = Arrays.asList(
            createFeature("kofuzi@gmail.com", "drawCircle", true),
            createFeature("kofuzi@gmail.com", "drawRectangle", false),
            createFeature("modifyme@gmail.com", "drawCircle", true)
        );
        featureRepository.saveAllAndFlush(featureList);
    }

    @AfterEach
    void resetData() {
        featureRepository.deleteAll();
        featureRepository.flush();
    }

    @Test
    void should_return_200_when_record_exists() throws Exception {
        mockMvc.perform(
            get("/feature")
                    .queryParam("email", "kofuzi@gmail.com")
                    .queryParam("featureName", "drawCircle")
            ).andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.canAccess", org.hamcrest.Matchers.is(true)));
    }

    @Test
    void should_return_404_when_record_does_not_exist() throws Exception {
        mockMvc.perform(
                        get("/feature")
                                .queryParam("email", "not_found@gmail.com")
                                .queryParam("featureName", "drawCircle")
                ).andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void should_return_200_when_new_record_is_added() throws Exception {
        // given
        String email = "new-user@gmail.com";
        String featureName = "drawCircle";
        FeatureAccessRequest request = FeatureAccessRequest.builder()
                .email(email)
                .featureName(featureName)
                .enable(true)
                .build();

        // when
        ObjectMapper mapper = new ObjectMapper();
        String content = mapper.writeValueAsString(request);
        mockMvc.perform(
                        post("/feature")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                ).andDo(print())
                .andExpect(status().isOk());

        // then
        final FeatureEntity featureEntity = featureRepository.findFeatureForUpdate(email, featureName).orElseThrow();
        assertEquals(email, featureEntity.getEmail());
        assertEquals(featureName, featureEntity.getFeatureName());
        assertEquals(true, featureEntity.getIsEnable());
    }

    @Test
    void should_return_200_when_record_is_modified() throws Exception {
        // given
        String email = "modifyme@gmail.com";
        String featureName = "drawCircle";
        FeatureAccessRequest request = FeatureAccessRequest.builder()
                .email(email)
                .featureName(featureName)
                .enable(false)
                .build();

        // when
        ObjectMapper mapper = new ObjectMapper();
        String content = mapper.writeValueAsString(request);
        mockMvc.perform(
                        post("/feature")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                ).andDo(print())
                .andExpect(status().isOk());

        // then
        final FeatureEntity featureEntity = featureRepository.findFeatureForUpdate(email, featureName).orElseThrow();
        assertEquals(email, featureEntity.getEmail());
        assertEquals(featureName, featureEntity.getFeatureName());
        assertEquals(false, featureEntity.getIsEnable());
    }

    @Test
    void should_return_301_when_record_is_not_modified() throws Exception {
        // given
        String email = "kofuzi@gmail.com";
        String featureName = "drawCircle";
        FeatureAccessRequest request = FeatureAccessRequest.builder()
                .email(email)
                .featureName(featureName)
                .enable(true)
                .build();

        // when
        ObjectMapper mapper = new ObjectMapper();
        String content = mapper.writeValueAsString(request);

        mockMvc.perform(
                        post("/feature")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                ).andDo(print())
                .andExpect(status().isNotModified());

        // then
        final FeatureEntity featureEntity = featureRepository.findFeatureForUpdate(email, featureName).orElseThrow();
        assertEquals(email, featureEntity.getEmail());
        assertEquals(featureName, featureEntity.getFeatureName());
        assertEquals(true, featureEntity.getIsEnable());
    }

    private FeatureEntity createFeature(String email, String featureName, boolean enable) {
        return FeatureEntity.builder()
                .email(email)
                .featureName(featureName)
                .isEnable(enable)
                .build();
    }
}
