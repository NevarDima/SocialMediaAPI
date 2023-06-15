package com.socialmedia.socialmediaapi.controller;

import com.socialmedia.socialmediaapi.mapper.PostMapper;
import com.socialmedia.socialmediaapi.model.User;
import com.socialmedia.socialmediaapi.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/v1/post")
@RequiredArgsConstructor
@Slf4j
public class PostController {

    private final PostService postService;
    private final PostMapper postMapper;

    @PostMapping(value = "/")
    public ResponseEntity<?> createPost(@AuthenticationPrincipal User user, @RequestBody Map<String, Object> postMap){
        var post =  postService.createPost(user, postMap);
        log.debug("Post with uuid: '{}' successfully saved", post.getUuid());
        return new ResponseEntity<>(postMapper.map(post), HttpStatus.OK);
    }

    @GetMapping(value = "/{uuid}")
    public ResponseEntity<?> getPostById(@PathVariable UUID uuid){
        var post = postService.findPostById(uuid);
        log.debug("Post with uuid: '{}' successfully found", post.getUuid());
        return new ResponseEntity<>(postMapper.map(post), HttpStatus.OK);
    }

    @PostMapping(value = "/{uuid}")
    public ResponseEntity<?> updatePostById (@AuthenticationPrincipal User user, @PathVariable UUID uuid, @RequestBody Map<String, Object> postMap){
        var post =  postService.updatePostById(user.getUuid(), uuid, postMap);
        log.debug("Post with uuid: '{}' successfully updated", post.getUuid());
        return new ResponseEntity<>(postMapper.map(post), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{uuid}")
    public ResponseEntity<?> deletePostById(@AuthenticationPrincipal User user, @PathVariable UUID uuid){
        postService.deletePostById(user.getUuid(), uuid);
        log.debug("Post with uuid: '{}' successfully deleted", uuid);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
