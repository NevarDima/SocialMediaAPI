package com.socialmedia.socialmediaapi.controller;

import com.socialmedia.socialmediaapi.mapper.RelationsMapper;
import com.socialmedia.socialmediaapi.model.User;
import com.socialmedia.socialmediaapi.service.FollowerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping(value = "/api/v1/users/follow")
@RequiredArgsConstructor
@Slf4j
public class FollowerController {

    private final FollowerService followerService;
    private final RelationsMapper relationsMapper;

    @PostMapping(value = "/{interestUuid}")
    public ResponseEntity<?> follow(
        @AuthenticationPrincipal User user,
        @PathVariable UUID interestUuid) {
        var follower = followerService.follow(user.getUuid(), interestUuid);
        log.debug("Relation with uuid: '{}' successfully saved.", follower.getInterestUuid());
        return new ResponseEntity<>(relationsMapper.followerToRelation(follower), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{interestUuid}")
    public ResponseEntity<?> unfollow(@AuthenticationPrincipal User user,
                                      @PathVariable UUID interestUuid){
        var follower = followerService.unfollow(user.getUuid(), interestUuid);
        log.debug("Relation with uuid: '{}' changed status to declined.", follower.getInterestUuid());
        return new ResponseEntity<>(relationsMapper.followerToRelation(follower), HttpStatus.OK);
    }

    @GetMapping(value = "/youfollow")
    public ResponseEntity<?> getUsersYouFollow(@AuthenticationPrincipal User user){
        var usersYouFollow = followerService.getUsersYouFollow(user.getUuid());
        log.debug("List of Users you follow for user uuid: '{}'", user.getUuid());
        return new ResponseEntity<>(usersYouFollow, HttpStatus.OK);
    }
}
