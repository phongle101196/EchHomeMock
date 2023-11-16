package com.mock.controller;

import com.mock.config.AppConstants;
import com.mock.entity.Post;
import com.mock.entity.User;
import com.mock.exception.ResourceNotFoundException;
import com.mock.exception.UnauthorizedAccessException;
import com.mock.payload.reponse.ApiResponse;
import com.mock.payload.reponse.PostReponse;
import com.mock.payload.request.PostDto;
import com.mock.repository.UserRepository;
import com.mock.service.FileService;
import com.mock.service.PostService;
import org.hibernate.engine.jdbc.StreamUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
@CrossOrigin("*")
//@Validated
public class PostController {
    @Autowired
    private PostService postService;
    @Autowired
    private FileService fileService;
    @Value("${project.image}")
    private String path;
    @Autowired
    private UserRepository userRepository;


    // CREATE
    @PostMapping("/create/{userId}")
    @PreAuthorize("hasRole('ROLE_POSTER')")
    public ResponseEntity<PostDto> createPost(@RequestBody PostDto postDto, @PathVariable Integer userId ) {

        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));
        if (user.isStatus() == true) {

            PostDto createPost = this.postService.createPost(postDto, userId);
            return new ResponseEntity<PostDto>(createPost, HttpStatus.CREATED);
        } else {
            throw new UnauthorizedAccessException("Only enabled ROLE_POSTER can create posts");
        }
    }

    // GET BY USER
    @GetMapping("/user/{userId}/posts")
    public ResponseEntity<List<PostDto>> getPostsByUser(@PathVariable Integer userId) {
        List<PostDto> posts = this.postService.getPostsByUser(userId);
        return new ResponseEntity<List<PostDto>>(posts, HttpStatus.OK);
    }

    // GET ALL POSTS
    @GetMapping("/allposts")
    public ResponseEntity<PostReponse> getAllPosts(
            @RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIR, required = false) String sortDir) {
        PostReponse postReponse = this.postService.getAllPost(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<PostReponse>(postReponse, HttpStatus.OK);
    }

    // GET POST BY POST ID
    @GetMapping("/{postId}")
    public ResponseEntity<PostDto> getPostById(@PathVariable Integer postId) {
        PostDto postDto = this.postService.getPostById(postId);
        return new ResponseEntity<PostDto>(postDto, HttpStatus.OK);
    }

    // DELETE POST
    @DeleteMapping("/delete_post/{postId}")
    @PreAuthorize("hasRole('ROLE_POSTER') ")
    public ApiResponse deletePost(@PathVariable Integer postId) {
        // Lấy thông tin người dùng từ SecurityContextHolder
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();


        // Xóa bài viết
        postService.deletePost(postId);

        return new ApiResponse("Post is successfully deleted!", true);
    }

    // UPDATE POST
    @PutMapping("/update_post/{postId}")
    @PreAuthorize("hasRole('ROLE_POSTER') and principal.isEnabled()")
    public ResponseEntity<PostDto> updatePost(@RequestBody PostDto postDto, @PathVariable Integer postId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user.isStatus()) {
            PostDto updatePost = this.postService.updatePost(postDto, postId);
            return new ResponseEntity<PostDto>(updatePost, HttpStatus.OK);
        } else {
            throw new UnauthorizedAccessException("Only enabled ROLE_POSTER can update posts");
        }
    }

    // SEARCH POST WITH KEYWORD
    @GetMapping("/search/district/{keywords}")
    public ResponseEntity<List<PostDto>> searchPostByRoomDistrict(@PathVariable("keywords") String keywords) {
        List<PostDto> result = this.postService.searchPosts(keywords);
        return new ResponseEntity<List<PostDto>>(result, HttpStatus.OK);
    }

    @GetMapping("/search/price/{keyPrice}")
    public ResponseEntity<List<PostDto>> searchPostByRoomPrice(@PathVariable("keyPrice") double keyPrice) {
        List<PostDto> result = this.postService.searchPostsWithPrice(keyPrice);
        return new ResponseEntity<List<PostDto>>(result, HttpStatus.OK);
    }

    // POST IMAGE UPLOAD
    @PostMapping("/image/upload/{postId}")
    public ResponseEntity<PostDto> uploadPostImage(@RequestParam("image") MultipartFile image, @PathVariable Integer postId) throws IOException {
        PostDto postDto = this.postService.getPostById(postId);
        String fileName = this.fileService.uploadImage(path, image);
        postDto.setRoomImage(fileName);
        PostDto updatePost = this.postService.updatePost(postDto, postId);
        return new ResponseEntity<PostDto>(updatePost, HttpStatus.OK);
    }

    @GetMapping(value = "post/image/{imageName}", produces = MediaType.IMAGE_JPEG_VALUE)
    public void downloadImage(@PathVariable("imageName") String imageName, HttpServletResponse response) throws IOException {
        InputStream resource = this.fileService.getResource(path, imageName);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource, response.getOutputStream());
    }
}
