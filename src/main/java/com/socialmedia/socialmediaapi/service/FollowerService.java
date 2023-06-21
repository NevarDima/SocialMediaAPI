package com.socialmedia.socialmediaapi.service;

import com.socialmedia.socialmediaapi.exception.FollowersNotFoundException;
import com.socialmedia.socialmediaapi.model.Follower;
import com.socialmedia.socialmediaapi.model.RelationsStatus;
import com.socialmedia.socialmediaapi.repo.FollowerRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FollowerService {
    private final FollowerRepo followerRepo;

    @Transactional
    public Follower follow(UUID subscriberUuid, UUID interestUuid) {
        var optionalFollower = followerRepo.findBySubscriberAndInterestUuids(subscriberUuid, interestUuid);
        if (optionalFollower.isPresent()){
            var follower = optionalFollower.get();
            if(follower.getRelationsStatus().equals(RelationsStatus.DECLINED)){
                follower.setRelationsStatus(RelationsStatus.ACTIVE);
                follower.setUpdatedAt(LocalDateTime.now());
                followerRepo.save(follower);
            }
            return follower;
        }
        var savedFollower = followerRepo.save(Follower
            .builder()
            .subscriberUuid(subscriberUuid)
            .interestUuid(interestUuid)
            .relationsStatus(RelationsStatus.ACTIVE)
            .createdAt(LocalDateTime.now())
            .build());
        log.debug("Request for follow between subscriber: '{}' and interest: '{}' saved with uuid: '{}'.", subscriberUuid, interestUuid,
            savedFollower.getUuid());
        return savedFollower;
    }

    @Transactional
    public Follower unfollow(UUID subscriberUuid, UUID interestUuid) {
        var optionalFollower = followerRepo.findBySubscriberAndInterestUuids(subscriberUuid, interestUuid);
        if(optionalFollower.isEmpty()){
            log.error("Relations doesn't exist between '{}' and '{}'.", subscriberUuid, interestUuid);
            throw new FollowersNotFoundException();
        }
        var follower = optionalFollower.get();
        if (follower.getRelationsStatus().equals(RelationsStatus.ACTIVE)){
            follower.setRelationsStatus(RelationsStatus.DECLINED);
            follower.setUpdatedAt(LocalDateTime.now());
            followerRepo.save(follower);
        }
        return follower;
    }

    public List<UUID> getUsersYouFollow(UUID subscriberUuid) {
        var optionalUsersYouFollow = followerRepo.findSubscriberUuidsByInterestUuid(subscriberUuid, RelationsStatus.ACTIVE);
        if (optionalUsersYouFollow.isEmpty()){
            log.debug("No users you follow for user: '{}'.", subscriberUuid);
            throw new FollowersNotFoundException();
        }
        log.debug("Users you follow for user: '{}' was got.", subscriberUuid);
        return optionalUsersYouFollow.get();
    }
}
