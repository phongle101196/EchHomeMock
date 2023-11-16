package com.mock.repository;

import com.mock.entity.Post;
import com.mock.entity.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    List<Post> findAllByUser(User user);
    List<Post> findByRoomDistrictContaining(String roomDistrict);
    List<Post> findByRoomPrice(double roomPrice);
}
