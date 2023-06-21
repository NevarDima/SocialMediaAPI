package com.socialmedia.socialmediaapi.service;

import com.socialmedia.socialmediaapi.exception.PostNotFoundException;
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
import java.util.UUID;

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
        log.debug("Post with uuid: '{}' saved.", post.getUuid());
        return post;
    }

    public Post findPostById(UUID uuid) {
        var post = postRepo.findByUuid(uuid);
        if (post.isEmpty()){
            log.debug("Post with uuid: '{}' doesn't exist", uuid);
            throw new PostNotFoundException();
        }
        return post.get();
    }

    @Transactional
    public Post updatePostById(UUID userUuid, UUID uuid, Map<String, Object> postMap) {
        var optionalPost = postRepo.findByUuidAndAuthorUuid(uuid, userUuid);
        if (optionalPost.isEmpty()){
            log.debug("Post with author uuid: '{}' uuid: '{}' doesn't exist", userUuid, uuid);
            throw new PostNotFoundException();
        }
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
        log.debug("Post with uuid: '{}' updated.", post.getUuid());
        return postRepo.save(post);
    }

    @Transactional
    public void deletePostById(UUID userUuid, UUID uuid) {
        var optionalPost = postRepo.findByUuidAndAuthorUuid(uuid, userUuid);
        if (optionalPost.isEmpty()){
            throw new PostNotFoundException();
        }
        postRepo.delete(optionalPost.get());
    }
}

