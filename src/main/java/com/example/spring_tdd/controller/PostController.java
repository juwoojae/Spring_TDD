package com.example.spring_tdd.controller;

import com.example.spring_tdd.dto.PostRequestDTO;
import com.example.spring_tdd.dto.PostResponseDTO;
import com.example.spring_tdd.repository.PostRepository;
import com.example.spring_tdd.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;


    //api method
    @PostMapping("/post")
    public ResponseEntity<?> postMethod(@RequestBody PostRequestDTO dto) throws Exception {

        Long id = postService.create(dto);
        PostResponseDTO postResponseDTO = new PostResponseDTO();
        postResponseDTO.setId(id);

        return new ResponseEntity<>(postResponseDTO, HttpStatus.OK);


    }

}
