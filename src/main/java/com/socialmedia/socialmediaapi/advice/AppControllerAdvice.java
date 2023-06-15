package com.socialmedia.socialmediaapi.advice;

import com.socialmedia.socialmediaapi.exception.FriendsNotFoundException;
import com.socialmedia.socialmediaapi.exception.PostNotFoundException;
import com.socialmedia.socialmediaapi.exception.RelationNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class AppControllerAdvice {

    @ExceptionHandler(RelationNotFoundException.class)
    public ResponseEntity<?> handleRelationNotFoundException(RelationNotFoundException exception){
        return new ResponseEntity<>("Relation doesn't exist.", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(FriendsNotFoundException.class)
    public ResponseEntity<?> handleFriendsNotFoundException(FriendsNotFoundException exception){
        return new ResponseEntity<>("Friends don't exist.", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PostNotFoundException.class)
    public ResponseEntity<?> handlePostNotFoundException(PostNotFoundException exception){
        return new ResponseEntity<>("Post doesn't exist", HttpStatus.NOT_FOUND);
    }
}
