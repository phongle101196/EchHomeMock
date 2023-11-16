package com.mock.controller;

import com.mock.entity.Post;
import com.mock.entity.SavePost;
import com.mock.entity.User;
import com.mock.exception.UnauthorizedAccessException;
import com.mock.payload.reponse.ApiResponse;
import com.mock.payload.request.SavePostDto;
import com.mock.payload.request.UserDto;
import com.mock.repository.PostRepository;
import com.mock.repository.SavePostRepository;
import com.mock.repository.UserRepository;
import com.mock.service.SavePostService;
import com.mock.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/likepost")
@CrossOrigin("*")
public class SavePostController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private SavePostRepository savePostRepository;
    @Autowired
    private SavePostService savePostService;
    @Autowired
    private UserService userService;
    @PostMapping("/posts/{postId}/like")
    public ResponseEntity<String> likePost(@PathVariable Integer postId, @AuthenticationPrincipal UserDetails userDetails) {
//         Lấy thông tin người dùng hiện tại từ UserDetails
        User currentUser = (User) userDetails;
//
        // Kiểm tra xem bài post có tồn tại hay không
        Optional<Post> postOptional = postRepository.findById(postId);
        if (!postOptional.isPresent()) {
            return ResponseEntity.badRequest().body("Post not found");
        }
        Post post = postOptional.get();
//
        // Kiểm tra xem người dùng đã like bài post này chưa
        boolean isLiked = savePostRepository.existsByUserAndPost(currentUser, post);
        if (isLiked) {
            return ResponseEntity.badRequest().body("You have already liked this post");
        }

        // Lưu thông tin like vào bảng savePost
        SavePost savePost = new SavePost();
        savePost.setUser(currentUser);
        savePost.setPost(post);
        savePost.setTimestamp(LocalDateTime.now());
        savePostRepository.save(savePost);

        return ResponseEntity.ok("Post liked successfully");
    }
    // GET BY USER
    @GetMapping("/user/{userId}/savepost")
    public ResponseEntity<List<SavePostDto>> getSavePostsByUser(@PathVariable Integer userId, Principal principal){

        List<SavePostDto> savePostDtos = this.savePostService.getSavePostsByUser(userId);
        return new ResponseEntity<List<SavePostDto>>(savePostDtos, HttpStatus.OK);
    }

    // DELETE SAVEPOST
    @DeleteMapping("/delete_savepost/{id}")
    public ApiResponse deleteSavePost(@PathVariable Integer id, Principal principal){

        this.savePostService.deleteSavePost(id);
        return new  ApiResponse("The post has been successfully removed from the saved list!", true);
    }
}
