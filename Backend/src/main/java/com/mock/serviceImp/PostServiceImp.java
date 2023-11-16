package com.mock.serviceImp;

import com.mock.entity.Post;
import com.mock.entity.User;
import com.mock.exception.ResourceNotFoundException;
import com.mock.payload.reponse.PostReponse;
import com.mock.payload.request.PostDto;
import com.mock.repository.PostRepository;
import com.mock.repository.UserRepository;
import com.mock.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImp implements PostService {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private UserRepository userRepository;

    @Override
    public PostDto createPost(PostDto postDto, Integer userId) {
        User user = this.userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "User id", userId));

        Post post = this.modelMapper.map(postDto, Post.class);
        post.setRoomImage("default.png");
        post.setAddDate(new Date());
        post.setUser(user);

        Post newPost = this.postRepository.save(post);

        return this.modelMapper.map(newPost, PostDto.class);
    }

    @Override
    public PostDto updatePost(PostDto postDto, Integer postId){
        Post post =  this.postRepository.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post","postId",postId));
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setRoomImage(postDto.getRoomImage());
        post.setRoomAddress(postDto.getRoomAddress());
        post.setRoomPrice(postDto.getRoomPrice());
        post.setRoomSize(postDto.getRoomSize());
        post.setRoomDescription(postDto.getRoomDescription());
        post.setRoomDistrict(postDto.getRoomDistrict());

        Post updatePost = this.postRepository.save(post);
        return this.modelMapper.map(updatePost, PostDto.class);
    }

    @Override
    public void deletePost(Integer postId) {
       Post post = this.postRepository.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post","postId",postId));
        this.postRepository.delete(post);
    }

    @Override
    public PostReponse getAllPost(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        Sort sort = null;
        if (sortDir.equalsIgnoreCase("asc")){
            sort=Sort.by(sortBy).ascending();
        }else {
            sort=Sort.by(sortBy).descending();
        }
        Pageable pageable = PageRequest.of(pageNumber,pageSize, sort);
        Page<Post> pagePost = this.postRepository.findAll(pageable);
       List<Post> allPosts = pagePost.getContent();
        List<PostDto> postDtos =  allPosts.stream().map((post) -> this.modelMapper.map(post, PostDto.class)).collect(Collectors.toList());

        PostReponse postReponse = new PostReponse();
        postReponse.setContent(postDtos);
        postReponse.setPageNumber(pagePost.getNumber());
        postReponse.setPageSize(pagePost.getSize());
        postReponse.setTotalElements(pagePost.getTotalElements());
        postReponse.setTotalPages(pagePost.getTotalPages());
        postReponse.setLastPage(pagePost.isLast());

        return postReponse;

    }

    @Override
    public PostDto getPostById(Integer postId) {
       Post post = this.postRepository.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post","postId",postId));
        return this.modelMapper.map(post, PostDto.class);
    }

    @Override
    public List<PostDto> getPostsByUser(Integer userId) {
        User user = this.userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User","userId",userId));
    List<Post> posts = this.postRepository.findAllByUser(user);

    List<PostDto> postDtos = posts.stream().map(post -> this.modelMapper.map(post, PostDto.class)).collect(Collectors.toList());
        return postDtos;
    }
    @Override
    public List<PostDto> searchPosts(String keywords) {
       List<Post> posts = this.postRepository.findByRoomDistrictContaining(keywords);
       List<PostDto> postDtos = posts.stream().map((post) ->this.modelMapper.map(post, PostDto.class)).collect(Collectors.toList());
        return postDtos;
    }

    @Override
    public List<PostDto> searchPostsWithPrice(double keyPrice) {
        List<Post> posts =  this.postRepository.findByRoomPrice(keyPrice);
        List<PostDto> postDtos = posts.stream().map((post) ->this.modelMapper.map(post, PostDto.class)).collect(Collectors.toList());
        return postDtos;
    }
}
