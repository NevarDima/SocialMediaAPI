package com.socialmedia.socialmediaapi.controller;

import com.socialmedia.socialmediaapi.mapper.RelationsMapper;
import com.socialmedia.socialmediaapi.model.User;
import com.socialmedia.socialmediaapi.service.RelationsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping(value = "/api/v1/users")
@RequiredArgsConstructor
@Slf4j
public class RelationsController {

    private final RelationsService relationsService;
    private final RelationsMapper relationsMapper;

    @PostMapping(value = "/friends/{interestUuid}")
    public ResponseEntity<?> requestForFriendship(
        @AuthenticationPrincipal User user,
        @PathVariable UUID interestUuid
    ) {
        var relations = relationsService.requestForFrendship(user.getUuid(), interestUuid);
        log.debug("Relation with uuid: '{}' successfully saved.", relations.getInterestUuid());
        return new ResponseEntity<>(relationsMapper.map(relations), HttpStatus.OK);
    }

    @PatchMapping(value = "/friends/{subscriberUuid}")
    public ResponseEntity<?> handleRequestForFriendship(
        @AuthenticationPrincipal User user,
        @PathVariable UUID subscriberUuid,
        @RequestParam boolean isConfirmed
    ) {
        var relations = relationsService.confirmFriendship(user.getUuid(), subscriberUuid, isConfirmed);
        log.debug("Relation with uuid: '{}' successfully updated.", relations.getInterestUuid());
        return new ResponseEntity<>(relationsMapper.map(relations), HttpStatus.OK);
    }

    @DeleteMapping(value = "/friends/{uuid}")
    public ResponseEntity<?> removeFromFriends(
        @AuthenticationPrincipal User user,
        @PathVariable UUID uuid
    ) {
        var relations = relationsService.removeFromFriend(user.getUuid(), uuid);
        log.debug("Friendship was reduced between '{}' and '{}'.", user.getUuid(), uuid);
        return new ResponseEntity<>(relationsMapper.map(relations), HttpStatus.OK);
    }

    @GetMapping(value = "/friends")
    public ResponseEntity<?> getFriends(@AuthenticationPrincipal User user) {
        var friends = relationsService.getFriends(user.getUuid());
        log.debug("List of friends for user uuid '{}'.", user.getUuid());
        return new ResponseEntity<>(friends, HttpStatus.OK);
    }

    @PostMapping(value = "/follow/{interestUuid}")
    public ResponseEntity<?> follow(
        @AuthenticationPrincipal User user,
        @PathVariable UUID interestUuid
    ) {
        var relations = relationsService.follow(user.getUuid(), interestUuid);
        log.debug("Relation with uuid: '{}' successfully saved.", relations.getInterestUuid());
        return new ResponseEntity<>(relationsMapper.map(relations), HttpStatus.OK);
    }

    @DeleteMapping(value = "/follow/{interestUuid}")
    public ResponseEntity<?> unfollow(@AuthenticationPrincipal User user,
                                      @PathVariable UUID interestUuid){
        var relations = relationsService.unfollow(user.getUuid(), interestUuid);
        log.debug("Relation with uuid: '{}' changed status to declined.", relations.getInterestUuid());
        return new ResponseEntity<>(relationsMapper.map(relations), HttpStatus.OK);
    }
}
