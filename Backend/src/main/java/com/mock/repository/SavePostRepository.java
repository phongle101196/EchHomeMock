package com.mock.repository;

import com.mock.entity.Post;
import com.mock.entity.SavePost;
import com.mock.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SavePostRepository extends JpaRepository<SavePost, Integer> {
    boolean existsByUserAndPost(User user, Post post);
    List<SavePost> findAllByUser(User user);
    boolean existsByIdAndUserUsername(Integer id, String username);
}
