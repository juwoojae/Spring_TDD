package com.example.spring_tdd.integration;

import com.example.spring_tdd.dto.PostRequestDTO;
import com.example.spring_tdd.entity.PostEntity;
import com.example.spring_tdd.repository.PostRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class PostControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    PostRepository postRepository;

    @Test
    void post_integrationTest() throws Exception {
        //given
        PostRequestDTO requestDTO = new PostRequestDTO();
        requestDTO.setTitle("제목");
        requestDTO.setContent("내용");
        //when & then
        mockMvc.perform(post("/post")
                      //  .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists());

    }

    @Test
    void get_integrationTest() throws Exception {
        //given
        PostEntity postEntity = new PostEntity();
        postEntity.setContent("내용");
        postEntity.setTitle("제목");
        Long saveId = postRepository.save(postEntity).getId();
        //when & then
        mockMvc.perform(get("/post/"+ saveId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(saveId))
                .andExpect(jsonPath("$.title").value("제목"))
                .andExpect(jsonPath("$.content").value("내용"));
    }
}
