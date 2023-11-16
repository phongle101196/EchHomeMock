package com.mock.payload.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
public class SavePostDto {
    private Integer id;
    private PostDto post;
    private UserDto user;
    private LocalDateTime timestamp;
}
