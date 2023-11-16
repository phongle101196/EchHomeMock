package com.mock.payload.request;

import com.mock.entity.Comment;
import com.mock.entity.RoomStatus;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class PostDto {
    private Integer postId;
    @NotEmpty
    private String title;
    @NotEmpty
    private String content;
    private String roomImage;
    @NotEmpty
    private String roomAddress;
    @NotEmpty
    private double roomPrice;
    @NotEmpty
    private double roomSize;
    @NotEmpty
    private String roomDescription;
    @NotEmpty
    private String roomDistrict;
    private RoomStatus roomStatus;
    private Date addDate;
    private UserDto user;
    private Set<CommentDto> comments = new HashSet<>();
}
