package com.example.spring_tdd.controller;

import com.example.spring_tdd.dto.GetResponseDTO;
import com.example.spring_tdd.dto.PostRequestDTO;
import com.example.spring_tdd.entity.PostEntity;
import com.example.spring_tdd.service.PostService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
//import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

    /**
     * 제공하는 @WithMockUser 어노테이션을 통해 단위 테스트시 SecurityContext를 만들어줄 수 있다.
     * 특정 role 값이 필요한 경로에 대해 role 에 충족하지 못하는 경우 403 응답이 발생하는지 then 검증할수 있다.
     */
    @Test
    //@WithMockUser(username = "admin", roles = {"ADMIN"})
    //mock 으로 로그인 상태를 설정해준것
    void post_method_controller1() throws Exception {
        //given
        PostRequestDTO requestDTO = new PostRequestDTO();
        requestDTO.setTitle("제목");
        requestDTO.setContent("내용");
        // when & then
        mockMvc.perform(post("/post")
                        // .with(csrf())
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
     * <p>
     * 단일 테스트 방법
     * Controller 에서 Mock 으로 Service 에서 예외를 던지고
     * ControllerAdviser 가 응답을 날리면 그 응답으로 test 를 진행하자
     */
    @Test
    //@WithMockUser(username = "admin", roles = {"ADMIN"})
    void post_method_controller2() throws Exception {
        //given
        PostRequestDTO requestDTO = new PostRequestDTO();
        requestDTO.setTitle("제목");
        requestDTO.setContent("내용");

        given(postService.create(any(PostRequestDTO.class))).willReturn(1L);

        // when & then
        mockMvc.perform(post("/post")
                        //.with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1)); //응답 body 에 id 필드를 가지고 value 는 1을 가질것이다

        //실제로 postService 의 의존성을 사용했는지 확인하자
        verify(postService).create(any(PostRequestDTO.class));

    }

    /**
     * 예외를 던지는 경우
     */
    @Test
    //@WithMockUser(username = "admin", roles = {"ADMIN"})
    void post_method_controller3() throws Exception {
        //given
        PostRequestDTO requestDTO = new PostRequestDTO();
        requestDTO.setTitle("");
        requestDTO.setContent("내용");

        given(postService.create(any(PostRequestDTO.class))).willThrow(new IllegalArgumentException());

        // when & then
        mockMvc.perform(post("/post")
                        //.with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void get_method_controller4() throws Exception {
      //given
        GetResponseDTO getResponseDTO = new GetResponseDTO();
        ReflectionTestUtils.setField(getResponseDTO,"id",1L);
        getResponseDTO.setTitle("테스트 타이틀");
        getResponseDTO.setContent("테스트 컨텐츠");

        given(postService.read(any(Long.class))).willReturn(getResponseDTO);
        //when & then
        // when & then
        mockMvc.perform(get("/post/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("테스트 타이틀"))
                .andExpect(jsonPath("$.content").value("테스트 컨텐츠"));

    }
}