package com.socialmedia.socialmediaapi.service;

import com.socialmedia.socialmediaapi.exception.FriendsNotFoundException;
import com.socialmedia.socialmediaapi.model.Friend;
import com.socialmedia.socialmediaapi.model.RelationsStatus;
import com.socialmedia.socialmediaapi.repo.FriendRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FriendService {

    private final FriendRepo friendRepo;
    private final FollowerService followerService;

    @Transactional
    public Friend requestForFrendship(UUID subscriberUuid, UUID interestUuid) {
        var bySubscriber= friendRepo.findBySubscriberAndInterestUuids(subscriberUuid, interestUuid);
        var byInterest= friendRepo.findBySubscriberAndInterestUuids(interestUuid, subscriberUuid);
        Friend relation;
        if(bySubscriber.isPresent()){
            relation = checkSubToInterestStatus(bySubscriber.get());
            followerService.follow(subscriberUuid, interestUuid);
            return relation;
        }else if(byInterest.isPresent()){
            var optionalRelation = checkInterestToSubStatus(byInterest.get());
            if (optionalRelation.isPresent()) return optionalRelation.get();
        }
        relation = friendRepo.save(Friend
            .builder()
            .subscriberUuid(subscriberUuid)
            .interestUuid(interestUuid)
            .relationsStatus(RelationsStatus.NEW)
            .createdAt(LocalDateTime.now())
            .build());
        followerService.follow(subscriberUuid, interestUuid);
        log.debug("Request for friendship between subscriber: '{}' and interest: '{}' saved with uuid: '{}'.", subscriberUuid, interestUuid,
            relation.getUuid());
        return relation;
    }

    private Optional<Friend> checkInterestToSubStatus(Friend byInterest) {
        if(byInterest.getRelationsStatus().equals(RelationsStatus.NEW)){
            byInterest.setRelationsStatus(RelationsStatus.ACTIVE);
            friendRepo.save(byInterest);
            log.debug("Relations between subscriber: '{}' and interest: '{}' was changed to '{}'.", byInterest.getSubscriberUuid(), byInterest.getInterestUuid(),
                RelationsStatus.ACTIVE);
            followerService.follow(byInterest.getInterestUuid(), byInterest.getSubscriberUuid());
            return Optional.of(byInterest);
        }
        return Optional.empty();
    }

    private Friend checkSubToInterestStatus(Friend bySubscriber) {
        log.debug("Relations between subscriber: '{}' and interest: '{}' exists.", bySubscriber.getSubscriberUuid(), bySubscriber.getInterestUuid());
        if(bySubscriber.getRelationsStatus().equals(RelationsStatus.DECLINED)){
            bySubscriber.setRelationsStatus(RelationsStatus.NEW);
            friendRepo.save(bySubscriber);
            log.debug("Relations between subscriber: '{}' and interest: '{}' was changed to '{}'.", bySubscriber.getSubscriberUuid(), bySubscriber.getInterestUuid(),
                RelationsStatus.NEW);
        }
        return bySubscriber;
    }

    @Transactional
    public Friend confirmFriendship(UUID interestUuid, UUID subscriberUuid, boolean isConfirmed) {
        var optionalFriend = friendRepo.findBySubscriberAndInterestUuids(subscriberUuid, interestUuid);
        if(optionalFriend.isEmpty()){
            log.error("Relations for subscriber: '{}' and interest: '{}' doesn't exist.", subscriberUuid, interestUuid);
            throw new FriendsNotFoundException();
        }
        var friend = optionalFriend.get();
        if(isConfirmed){
            friend.setRelationsStatus(RelationsStatus.ACTIVE);
            friend.setUpdatedAt(LocalDateTime.now());
            followerService.follow(interestUuid, subscriberUuid);
        }else{
            friend.setRelationsStatus(RelationsStatus.DECLINED);
            friend.setUpdatedAt(LocalDateTime.now());
        }
        var updatedFriend = friendRepo.save(friend);
        log.debug("Friendship between subscriber: '{}' and interest: '{}' with uuid: '{}' was updated successfully.", subscriberUuid,
            interestUuid, updatedFriend.getUuid());
        return updatedFriend;
    }

    @Transactional
    public Friend removeFromFriend(UUID userUuid1, UUID userUuid2) {
        var optionalRelation = friendRepo.findBySubscriberAndInterestUuids(userUuid1, userUuid2);
        if (optionalRelation.isEmpty()){
            optionalRelation = friendRepo.findBySubscriberAndInterestUuids(userUuid2, userUuid1);
        }
        if (optionalRelation.isEmpty()){
            log.error("Relations doesn't exist between '{}' and '{}'.", userUuid1, userUuid2);
            throw new FriendsNotFoundException();
        }
        var relation = optionalRelation.get();
        relation.setRelationsStatus(RelationsStatus.DECLINED);
        followerService.unfollow(userUuid1, userUuid2);
        log.debug("Friendship between '{}' and '{}' with uuid: '{}' was removed.", userUuid1, userUuid2, relation.getUuid());
        return relation;
    }

    public List<UUID> getFriends(UUID userUuid) {
        var interestUuidsBySubscriberUuid = friendRepo.findInterestUuidsBySubscriberUuid(userUuid, RelationsStatus.ACTIVE);
        var subscriberUuidsByInterestUuid = friendRepo.findSubscriberUuidsByInterestUuid(userUuid, RelationsStatus.ACTIVE);
        List<UUID> friends = new ArrayList<>();
        interestUuidsBySubscriberUuid.ifPresent(friends::addAll);
        subscriberUuidsByInterestUuid.ifPresent(friends::addAll);
        if (friends.isEmpty()){
            log.error("No friends for user: '{}'.", userUuid);
            throw new FriendsNotFoundException();
        }
        log.debug("Friends for user: '{}' was got.", userUuid);
        return friends;
    }

}
