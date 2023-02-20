package com.example.demo.controller;

import com.example.demo.model.UpdateFeatureAccess;
import com.example.demo.repository.FeatureRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

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

//    @BeforeEach
//    void createData() {
//        List<FeatureEntity> featureList = Arrays.asList(
//                createFeature("kofuzi@gmail.com", "drawCircle", true),
//                createFeature("kofuzi@gmail.com", "drawRectangle", false),
//                createFeature("runner@gmail.com", "drawSquare", false),
//                createFeature("runner@gmail.com", "drawLine", true),
//                createFeature("modifyme@gmail.com", "drawCircle", true)
//        );
//        featureRepository.saveAllAndFlush(featureList);
//    }

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
    void should_return_404_when_record_exists() throws Exception {
        mockMvc.perform(
                        get("/feature")
                                .queryParam("email", "not_found@gmail.com")
                                .queryParam("featureName", "drawCircle")
                ).andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void should_return_200_when_record_is_modified() throws Exception {
        UpdateFeatureAccess request = UpdateFeatureAccess.builder()
                .email("modifyme@gmail.com")
                .featureName("drawCircle")
                .enable(false)
                .build();

        ObjectMapper mapper = new ObjectMapper();
        String content = mapper.writeValueAsString(request);

        mockMvc.perform(
                        post("/feature")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                ).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void should_return_301_when_record_is_not_modified() throws Exception {
        UpdateFeatureAccess request = UpdateFeatureAccess.builder()
                .email("kofuzi@gmail.com")
                .featureName("drawCircle")
                .enable(true)
                .build();

        ObjectMapper mapper = new ObjectMapper();
        String content = mapper.writeValueAsString(request);

        mockMvc.perform(
                        post("/feature")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                ).andDo(print())
                .andExpect(status().isNotModified());
    }

//    private FeatureEntity createFeature(String email, String featureName, boolean enable) {
//        return FeatureEntity.builder()
//                .email(email)
//                .featureName(featureName)
//                .isEnable(enable)
//                .build();
//    }
}
