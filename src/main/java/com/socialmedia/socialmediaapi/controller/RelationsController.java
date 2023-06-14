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
@RequestMapping(value = "/api/v1/relations")
@RequiredArgsConstructor
@Slf4j
public class RelationsController {

    private final RelationsService relationsService;
    private final RelationsMapper relationsMapper;

    @PostMapping(value = "/friends/{interestUuid}")
    public ResponseEntity<?> requestForFriendship(
        @AuthenticationPrincipal User user,
        @PathVariable UUID interestUuid) {
        try {
            var relations = relationsService.requestForFrendship(user.getUuid(), interestUuid);
            log.debug("Relation with uuid: '{}' successfully saved.", relations.getInterestUuid());
            return new ResponseEntity<>(relationsMapper.map(relations), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Failed saving request for friendship.", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping(value = "/friends/{subscriberUuid}")
    public ResponseEntity<?> handleRequestForFriendship(
        @AuthenticationPrincipal User user,
        @PathVariable UUID subscriberUuid,
        @RequestParam boolean isConfirmed) {
        try {
            var optionalRelations = relationsService.confirmFriendship(user.getUuid(), subscriberUuid, isConfirmed);
            if (optionalRelations.isPresent()) {
                log.debug("Relation with uuid: '{}' successfully updated.", optionalRelations.get().getInterestUuid());
                return new ResponseEntity<>(relationsMapper.map(optionalRelations.get()), HttpStatus.OK);
            }
            log.debug("Relations for subscriber: '{}' and interest: '{}' doesn't exist.", subscriberUuid, user.getUuid());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("Failed updating request for friendship.", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(value = "/friends/{uuid}")
    public ResponseEntity<?> removeFromFriends(
        @AuthenticationPrincipal User user,
        @PathVariable UUID uuid) {
        try {
            var optionalRelation = relationsService.removeFromFriend(user.getUuid(), uuid);
            if (optionalRelation.isPresent()) {
                log.debug("Friendship was reduced between '{}' and '{}'.", user.getUuid(), uuid);
                return new ResponseEntity<>(relationsMapper.map(optionalRelation.get()), HttpStatus.OK);
            }
            log.debug("Relations doesn't exist between '{}' and '{}'.", user.getUuid(), uuid);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("Failed removing for friends.", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/friends")
    public ResponseEntity<?> getFriends(@AuthenticationPrincipal User user) {
        try {
            var optionalFriends = relationsService.getFriends(user.getUuid());
            if (optionalFriends.isPresent()) {
                log.debug("List of friends for user uuid '{}'.", user.getUuid());
                return new ResponseEntity<>(optionalFriends.get(), HttpStatus.OK);
            }
            log.debug("Friends don't exist for user: '{}'.", user.getUuid());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("Failed getting friends.");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/follow/{interestUuid}")
    public ResponseEntity<?> follow(@AuthenticationPrincipal User user,
                                    @PathVariable UUID interestUuid){
        try {
            var relations = relationsService.follow(user.getUuid(), interestUuid);
            log.debug("Relation with uuid: '{}' successfully saved.", relations.getInterestUuid());
            return new ResponseEntity<>(relationsMapper.map(relations), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Failed saving request for follow.", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

//    @DeleteMapping(value = "/follow/{interestUuid}")
//    public ResponseEntity<?> unfollow(@AuthenticationPrincipal User user,
//                                      @PathVariable UUID interestUuid){
//
//    }
}
