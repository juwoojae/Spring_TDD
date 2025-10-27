package com.example.spring_tdd.service;

import com.example.spring_tdd.dto.PostRequestDTO;
import com.example.spring_tdd.entity.PostEntity;
import com.example.spring_tdd.repository.PostRepository;
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
}

//throw new UnsupportedOperationException("아직 구현중에 있다.");

