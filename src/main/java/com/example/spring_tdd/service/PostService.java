package com.example.spring_tdd.service;

import com.example.spring_tdd.dto.GetResponseDTO;
import com.example.spring_tdd.dto.PostRequestDTO;
import com.example.spring_tdd.entity.PostEntity;
import com.example.spring_tdd.repository.PostRepository;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

@Service
public class PostService {

    private PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    //crud method inpl
    public Long create(PostRequestDTO dto){
        PostEntity postEntity = new PostEntity();
        String title = dto.getTitle();
        if(title.isEmpty()){
            throw new IllegalArgumentException();
        }
        postEntity.setTitle(title);
        postEntity.setContent(dto.getContent());
        return postRepository.save(postEntity).getId();
    }
    //1. 1. id 를 넣었을때 id 를 리턴하는가 2. repository 의 의존성을 사용하는가
    public GetResponseDTO read(Long id){
        PostEntity postEntity = postRepository.findById(id).orElseThrow(
                ()-> new IllegalStateException("id 가 없습니다"));
        GetResponseDTO getResponseDTO = new GetResponseDTO();
        getResponseDTO.setId(postEntity.getId());
        getResponseDTO.setTitle(postEntity.getTitle());
        getResponseDTO.setContent(postEntity.getContent());
        return getResponseDTO;
    }
}

//throw new UnsupportedOperationException("아직 구현중에 있다.");

