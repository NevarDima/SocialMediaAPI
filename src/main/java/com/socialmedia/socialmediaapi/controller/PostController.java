package com.socialmedia.socialmediaapi.controller;

import com.socialmedia.socialmediaapi.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequestMapping(value = "/post")
@RequiredArgsConstructor
@Slf4j
public class PostController {

    private final PostService postService;

    @PostMapping(value = "/")
    public ResponseEntity<?> createPost(@RequestBody Map<String, Object> postMap){
        try{
            var post =  postService.createPost(postMap);
            log.debug("Post with id: '{}' successfully saved", post.getId());
            return new ResponseEntity<>(post, HttpStatus.OK);
        }catch (Exception e){
            log.error("Failed creating post", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getPostById(@PathVariable int id){
        try{
            var optionalPost = postService.findPostById(id);
            if(optionalPost.isPresent()) {
                log.debug("Post with id: '{}' successfully found", optionalPost.get().getId());
                return new ResponseEntity<>(optionalPost.get(), HttpStatus.OK);
            }
            log.debug("Post with id: '{}' doesn't exist", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (Exception e){
            log.error("Failed getting post", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/{id}")
    public ResponseEntity<?> updatePostById (@PathVariable int id, @RequestBody Map<String, Object> postMap){
        try{
            var optionalPost =  postService.updatePostById(id, postMap);
            if(optionalPost.isPresent()) {
                log.debug("Post with id: '{}' successfully updated", optionalPost.get().getId());
                return new ResponseEntity<>(optionalPost, HttpStatus.OK);
            }
            log.debug("Post with id: '{}' doesn't exist", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (Exception e){
            log.error("Failed updating post", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deletePostById(@PathVariable int id){
        try{
            var optionalPost = postService.deletePostById(id);
            if (optionalPost.isPresent()) {
                log.debug("Post with id: '{}' successfully deleted", id);
                return new ResponseEntity<>(HttpStatus.OK);
            }
            log.debug("Post with id: '{}' doesn't exist", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (Exception e){
            log.error("Failed deleting post", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
