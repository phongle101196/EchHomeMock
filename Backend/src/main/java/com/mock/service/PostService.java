package com.mock.service;

import com.mock.payload.reponse.PostReponse;
import com.mock.payload.request.PostDto;

import java.util.List;

public interface PostService {
    // CREATE POST
    PostDto createPost(PostDto postDto, Integer userId);

    //UPDATE POST
    PostDto updatePost(PostDto postDto, Integer postId);

    // DELETE POST
    void deletePost(Integer postId);

    // GET ALL POSTS
    PostReponse getAllPost(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);

    // GET SINGLE POST
    PostDto getPostById(Integer postId);

    // GET ALL POSTS BY USER

    List<PostDto> getPostsByUser(Integer userId);

    // SEARCH POST WITH KEYWORD
    List<PostDto> searchPosts(String keywords);

    List<PostDto> searchPostsWithPrice(double keyPrice);
}
