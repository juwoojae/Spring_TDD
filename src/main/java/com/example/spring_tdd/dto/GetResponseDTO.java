package com.example.spring_tdd.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//조회니깐
public class GetResponseDTO {

    private  Long id;
    private String title;
    private String content;
}
