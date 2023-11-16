package com.mock.controller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mock.entity.User;
import com.mock.exception.UnauthorizedAccessException;
import com.mock.payload.reponse.MessageResponse;
import com.mock.payload.request.ChangePasswordRequest;
import com.mock.payload.request.ForgotPasswordRequest;
import com.mock.payload.request.ResetPasswordRequest;
import com.mock.payload.request.UserDto;
import com.mock.repository.UserRepository;
import com.mock.service.UserService;
import com.mock.serviceImp.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin("*")
@Validated
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private EmailService emailService;
    @PutMapping("/update_user/{userId}")
    public ResponseEntity<UserDto> updateUser(@RequestBody @Valid UserDto userDto, @PathVariable("userId") Integer userId, Principal principal){
        if (principal == null) {
            throw  new UnauthorizedAccessException("Please Login!");
        }
        System.out.println(principal);
        // Lấy thông tin người dùng từ userId
        UserDto getuser = this.userService.getUserById(userId);
        // Lấy thông tin xác thực của người dùng
        String loggedInUsername = principal.getName();

//         Kiểm tra xem người dùng đã đăng nhập có quyền truy cập vào thông tin của chính mình hay không
        if (!loggedInUsername.equals(getuser.getEmail())) {
            throw  new UnauthorizedAccessException("Access Denined!");
        }
        UserDto updatedUser = this.userService.updateUser(userDto,userId);
       return ResponseEntity.ok(updatedUser);
    }
    @PostMapping("/{userId}/lock")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> toggleUserStatus(@PathVariable("userId") int userId) {
        userService.toggleUserStatus(userId);
        return new ResponseEntity<>("User account locked/unlocked successfully", HttpStatus.OK);
    }

    // GET ALL USERS( BY ADMIN)
    @GetMapping("/getAllUsers")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<UserDto>> getAllUsers(){
        return ResponseEntity.ok(this.userService.getAllUsers());
    }

    // GET USER BY ID(BY ADMIN)
    @GetMapping("/getUser/{userId}")
    public ResponseEntity<UserDto> getSingleUser(@PathVariable Integer userId){
       return ResponseEntity.ok(this.userService.getUserById(userId));
    }
    @PostMapping("/promote")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> promoteUserToPoster(@RequestParam Integer userId) {
        userService.promoteUserToPoster(userId);
        return ResponseEntity.ok("User role promoted to ROLE_POSTER successfully");
    }
    // RESET PASSWORD
    @PutMapping("/change_password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest, Principal principal) {
        // Kiểm tra xem người dùng đã đăng nhập hay chưa
        if (principal == null) {
            throw  new UnauthorizedAccessException("Please Login!");
        }
        // Lấy thông tin người dùng hiện tại từ SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        // Kiểm tra mật khẩu hiện tại của người dùng
        User user =userService.findByEmail(email);
        if (user==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Error: User not found"));
        }


        if (!passwordEncoder.matches(changePasswordRequest.getOldPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Error: Invalid old password"));
        }

        // Cập nhật mật khẩu mới cho người dùng
        user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        userService.saveOrUpdate(user);

        return ResponseEntity.ok("Password changed successfully");
    }


    // FORGOT PASSWORD
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest forgotPasswordRequest) {
        // Kiểm tra xem email đã được đăng ký trong hệ thống hay chưa
        User user = userService.findByEmail(forgotPasswordRequest.getEmail());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Error: Email not found"));
        }
        // Tạo mã xác thực và lưu vào cơ sở dữ liệu
        String resetToken = UUID.randomUUID().toString();
        user.setResetPasswordToken(resetToken);
        userService.saveOrUpdate(user);
        // Gửi email chứa liên kết đặt lại mật khẩu
        String resetPasswordLink = "http://localhost:8080/api/v1/reset-password?token=" + resetToken;
        emailService.sendResetPasswordEmail(user.getEmail(), resetPasswordLink);
        return ResponseEntity.ok(new MessageResponse("Reset password link has been sent to your email"));
    }
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest) {
        // Kiểm tra xem mã xác thực có hợp lệ hay không
        User user = userService.findByResetPasswordToken(resetPasswordRequest.getResetToken());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Error: Invalid reset token"));
        }

        // Cập nhật mật khẩu mới
        user.setPassword(passwordEncoder.encode(resetPasswordRequest.getNewPassword()));
        user.setResetPasswordToken(null);
        userService.saveOrUpdate(user);

        return ResponseEntity.ok(new MessageResponse("Password reset successfully"));
    }


}
