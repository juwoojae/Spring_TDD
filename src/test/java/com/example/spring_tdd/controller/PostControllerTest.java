package com.example.spring_tdd.controller;

import com.example.spring_tdd.dto.PostRequestDTO;
import com.example.spring_tdd.service.PostService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 1. 엔드 포인트에 Json 을 보냈을때 200(Ok) 를 보내는가
 * 2. Service 의존성을 사용하는가 + http 응답 body 에 저장한 값이 오는가
 */
@WebMvcTest(controllers = PostController.class)
class PostControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    PostService postService;

    @Test
    void post_method_controller1() throws Exception {
        //given
        PostRequestDTO requestDTO = new PostRequestDTO();
        requestDTO.setTitle("제목");
        requestDTO.setContent("내용");
        // when & then
        mockMvc.perform(post("/post")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    /**
     * 명심하자. 예외 처리에 관한 테스트
     * 어디서 처리해야 할까?
     * 1. 전체 예외처리를 담당하는 ControllerAdviser
     * 2. 예외를 실제로 던지는 Service
     *
     * 단일 테스트 방법
     * Controller 에서 Mock 으로 Service 에서 예외를 던지고
     * ControllerAdviser 가 응답을 날리면 그 응답으로 test 를 진행하자
     */
    @Test
    void post_method_controller2() throws Exception {
        //given
        PostRequestDTO requestDTO = new PostRequestDTO();
        requestDTO.setTitle("제목");
        requestDTO.setContent("내용");

        given(postService.create(any(PostRequestDTO.class))).willReturn(1L);

        // when & then
        mockMvc.perform(post("/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1)); //응답 body 에 id 필드를 가지고 value 는 1을 가질것이다

        //실제로 postService 의 의존성을 사용했는지 확인하자
        verify(postService).create(any(PostRequestDTO.class));

    }
    @Test
    void post_method_controller3() throws Exception {
        //given
        PostRequestDTO requestDTO = new PostRequestDTO();
        requestDTO.setTitle("");
        requestDTO.setContent("내용");

        given(postService.create(any(PostRequestDTO.class))).willThrow(new IllegalArgumentException());

        // when & then
        mockMvc.perform(post("/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}