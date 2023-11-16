package com.mock.security;

import com.mock.entity.User;
import com.mock.exception.ResourceNotFoundException;
import com.mock.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // LOADING USER FROM DATABASE BY USERNAME
//        User user = this.userRepository.findByEmail(username).orElseThrow(()->new ResourceNotFoundException("User","email : "+username,0));
//        return user;
        User user = this.userRepository.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return user;
    }
}
