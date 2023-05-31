package com.socialmedia.socialmediaapi.repo;

import com.socialmedia.socialmediaapi.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepo extends JpaRepository<Post, String> {

    Optional<Post> findById(int id);
}
