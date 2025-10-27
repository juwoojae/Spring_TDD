package com.example.spring_tdd.controller;

import com.example.spring_tdd.dto.GetResponseDTO;
import com.example.spring_tdd.dto.PostRequestDTO;
import com.example.spring_tdd.dto.PostResponseDTO;
import com.example.spring_tdd.repository.PostRepository;
import com.example.spring_tdd.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {

    private final PostService postService;


    //api method
    @PostMapping
    public ResponseEntity<?> postMethod(@RequestBody PostRequestDTO dto) throws Exception {

        Long id = postService.create(dto);
        PostResponseDTO postResponseDTO = new PostResponseDTO();
        postResponseDTO.setId(id);

        return new ResponseEntity<>(postResponseDTO, HttpStatus.OK);


    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getMethod(@PathVariable Long id) throws Exception {

        GetResponseDTO getResponseDTO = postService.read(id);
        return new  ResponseEntity<>(getResponseDTO, HttpStatus.OK);

    }

}
