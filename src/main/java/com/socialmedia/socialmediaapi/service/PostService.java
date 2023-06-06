package com.socialmedia.socialmediaapi.service;

import com.socialmedia.socialmediaapi.model.Post;
import com.socialmedia.socialmediaapi.model.User;
import com.socialmedia.socialmediaapi.repo.PostRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepo postRepo;

    @Transactional
    public Post createPost(User user, Map<String, Object> postMap) {
        var post = postRepo.save(Post
            .builder()
            .header(postMap.get("header").toString())
            .content(postMap.get("content").toString())
            .image(postMap.get("image").toString())
            .author(user)
            .createdAt(LocalDateTime.now())
            .build()
        );
        log.debug("Post with id: '{}' saved.", post.getId());
        return post;
    }

    public Optional<Post> findPostById(long id) {
        return postRepo.findById(id);
    }

    @Transactional
    public Optional<Post> updatePostById(long id, Map<String, Object> postMap) {
        var optionalPost = postRepo.findById(id);
        if (optionalPost.isPresent()){
            var post = optionalPost.get();
            if(postMap.containsKey("header") && !Objects.equals(post.getHeader(), postMap.get("header").toString())){
                post.setHeader(postMap.get("header").toString());
            }
            if(postMap.containsKey("content") && !Objects.equals(post.getContent(), postMap.get("content").toString())){
                post.setContent(postMap.get("content").toString());
            }
            if(postMap.containsKey("image") && !Objects.equals(post.getImage(), postMap.get("image").toString())){
                post.setImage(postMap.get("image").toString());
            }
            post.setUpdatedAt(LocalDateTime.now());
            log.debug("Post with id: '{}' updated.", post.getId());
            return Optional.of(postRepo.save(post));
        }
        return optionalPost;
    }

    @Transactional
    public Optional<Post> deletePostById(long id) {
        var optionalPost = postRepo.findById(id);
        optionalPost.ifPresent(postRepo::delete);
        return optionalPost;
    }
}

