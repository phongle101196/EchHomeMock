package com.mock.controller;

import com.mock.payload.reponse.ApiResponse;
import com.mock.payload.request.CommentDto;
import com.mock.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("/api/v1/comments")
@CrossOrigin("*")
@Validated
public class CommentController {
    @Autowired
    private CommentService commentService;

    // CREATE COMMENT
    @PostMapping("/post/{postId}")
    public ResponseEntity<CommentDto> createComment(@RequestBody @Valid CommentDto comment, @PathVariable Integer postId) {

        CommentDto createComment = this.commentService.createComment(comment, postId);

        return new ResponseEntity<CommentDto>(createComment, HttpStatus.CREATED);

    }

    // DELETE COMMENT
    @DeleteMapping("/{commentId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse> deleteComment(@PathVariable Integer commentId, Principal principal) {

        commentService.deleteComment(commentId);

        return new ResponseEntity<>(new ApiResponse("Comment is successfully deleted!", true), HttpStatus.OK);
    }
}
