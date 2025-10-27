package com.example.spring_tdd.service;

import com.example.spring_tdd.dto.PostRequestDTO;
import com.example.spring_tdd.entity.PostEntity;
import com.example.spring_tdd.repository.PostRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @InjectMocks
    PostService postService;

    @Mock
    PostRepository postRepository;

    @Captor
    ArgumentCaptor<PostEntity> postEntityCaptor;
    /**
     * create 는 대략 DTO 를 받아 저장 후 id 값을 리턴한다
     */
    @Test
    void create1(){
        //given
        PostRequestDTO requestDTO = new PostRequestDTO();
        requestDTO.setTitle("제목1");
        requestDTO.setTitle("내용1");

        PostEntity saved = new PostEntity();
        ReflectionTestUtils.setField(saved, "id", 1L); //db 가 id 를 생성해서 반환했다고 가정 (흉내내기 Mock)
        saved.setTitle("제목");
        saved.setContent("내용");
        given(postRepository.save(any(PostEntity.class))).willReturn(saved); //만약 PostService 가 postRepostory.save(PostEntity 객체 무엇이든) 호출하면 saved 를 반환한다.
        //when
        Long resultId = postService.create(requestDTO);
        //then
        assertThat(resultId).isEqualTo(1L);
    }

    /**
     * 비즈니스 로직을 검증하는 테스트가 아님.
     * 서비스 계층의 흐름을 확인하는 단위기반 테스트
     */
    @Test
    void create2(){ //PostService 가 create() 메서드가 Repository 의 save() 를 호출하는가?
        // given
        PostRequestDTO requestDTO = new PostRequestDTO();
        requestDTO.setTitle("제목2");
        requestDTO.setContent("내용2");
        //이부분이 PostRepository 를 Mock 으로 구현한것
        PostEntity saved = new PostEntity();
        ReflectionTestUtils.setField(saved, "id", 1L); //db 가 id 를 생성해서 반환했다고 가정 (흉내내기 Mock)
        saved.setTitle("제목");
        saved.setContent("내용");
        given(postRepository.save(any(PostEntity.class))).willReturn(saved); //만약 PostService 가 postRepostory.save(PostEntity 객체 무엇이든) 호출하면 saved 를 반환한다.
        // when
        postService.create(requestDTO);

        // then
        verify(postRepository).save(any(PostEntity.class));

    }
    @Test
    void create_savesEntityAndReturnsId() {
        // given
        PostRequestDTO dto = new PostRequestDTO();
        dto.setTitle("테스트 제목");
        dto.setContent("테스트 내용");

        PostEntity saved = new PostEntity();
        ReflectionTestUtils.setField(saved, "id", 1L); // Mocked DB ID
        given(postRepository.save(any(PostEntity.class))).willReturn(saved);

        // when
        Long resultId = postService.create(dto);

        // then
        verify(postRepository).save(postEntityCaptor.capture());  // save 호출 확인
        PostEntity captured = postEntityCaptor.getValue();

        // DTO → Entity 변환 검증
        assertThat(captured.getTitle()).isEqualTo("테스트 제목");
        assertThat(captured.getContent()).isEqualTo("테스트 내용");

        // 반환 ID 검증
        assertThat(resultId).isEqualTo(1L);
    }
}

//ArgumentCaptor 사용하기
