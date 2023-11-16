package com.mock.serviceImp;

import com.mock.entity.Comment;
import com.mock.entity.Post;
import com.mock.exception.ResourceNotFoundException;
import com.mock.payload.request.CommentDto;
import com.mock.payload.request.PostDto;
import com.mock.repository.CommentRepository;
import com.mock.repository.PostRepository;
import com.mock.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CommentServiceImp implements CommentService {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Override
    public CommentDto createComment(CommentDto commentDto, Integer postId) {
        Post post = this.postRepository.findById(postId).orElseThrow(()-> new ResourceNotFoundException("Post","postId",postId));
       Comment comment = this.modelMapper.map(commentDto, Comment.class);
       comment.setPost(post);
       Comment savedComment = this.commentRepository.save(comment);
        return this.modelMapper.map(savedComment, CommentDto.class);
    }

    @Override
    public void deleteComment(Integer commentId) {
        Comment comment = this.commentRepository.findById(commentId).orElseThrow(()->new ResourceNotFoundException("Comment","commentId",commentId));
        this.commentRepository.delete(comment);

    }

}
