package com.socialmedia.socialmediaapi.service;

import com.socialmedia.socialmediaapi.model.Relations;
import com.socialmedia.socialmediaapi.model.RelationsStatus;
import com.socialmedia.socialmediaapi.repo.RelationsRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class RelationsService {

    private final RelationsRepo relationsRepo;

    @Transactional
    public Relations requestForFrendship(UUID subscriberUuid, UUID interestUuid) {
        Optional<Relations> bySubscriber= relationsRepo.findBySubscriberAndInterestUuids(subscriberUuid, interestUuid);
        Optional<Relations> byInterest= relationsRepo.findBySubscriberAndInterestUuids(interestUuid, subscriberUuid);
        Relations relation;
        if(bySubscriber.isPresent()){
            relation = checkSubToInterestStatus(subscriberUuid, interestUuid, bySubscriber.get());
            return relation;
        }else if(byInterest.isPresent()){
            var optionalRelation = checkInterestToSubStatus(subscriberUuid, interestUuid, byInterest.get());
            if (optionalRelation.isPresent()) return optionalRelation.get();
        }
        relation = relationsRepo.save(Relations
            .builder()
            .subscriberUuid(subscriberUuid)
            .interestUuid(interestUuid)
            .relationsStatus(RelationsStatus.REQUESTFORFRIEND)
            .build());
        log.debug("Request for friendship between subscriber: '{}' and interest: '{}' saved with uuid: '{}'.", subscriberUuid, interestUuid,
            relation.getUuid());
        return relation;
    }

    private Optional<Relations> checkInterestToSubStatus(UUID subscriberUuid, UUID interestUuid, Relations byInterest) {
        if(byInterest.getRelationsStatus().equals(RelationsStatus.REQUESTFORFRIEND)){
            byInterest.setRelationsStatus(RelationsStatus.FRIEND);
            relationsRepo.save(byInterest);
            log.debug("Relations between subscriber: '{}' and interest: '{}' was changed to '{}'.", subscriberUuid, interestUuid,
                RelationsStatus.FRIEND);
            return Optional.of(byInterest);
        }
        return Optional.empty();
    }

    private Relations checkSubToInterestStatus(UUID subscriberUuid, UUID interestUuid, Relations bySubscriber) {
        log.debug("Relations between subscriber: '{}' and interest: '{}' exists.", subscriberUuid, interestUuid);
        if(!bySubscriber.getRelationsStatus().equals(RelationsStatus.FRIEND)){
            bySubscriber.setRelationsStatus(RelationsStatus.REQUESTFORFRIEND);
            relationsRepo.save(bySubscriber);
            log.debug("Relations between subscriber: '{}' and interest: '{}' was changed to '{}'.", subscriberUuid, interestUuid,
                RelationsStatus.REQUESTFORFRIEND);
        }
        return bySubscriber;
    }

    @Transactional
    public Optional<Relations> confirmFriendship(UUID interestUuid, UUID subscriberUuid, boolean isConfirmed) {
        var optionalRelation = relationsRepo.findBySubscriberAndInterestUuids(interestUuid, subscriberUuid);
        if(optionalRelation.isPresent()){
            var relation = optionalRelation.get();
            if(isConfirmed){
                relation.setRelationsStatus(RelationsStatus.FRIEND);
            }else{
                relation.setRelationsStatus(RelationsStatus.FOLLOWER);
            }
            var updatedRelation = relationsRepo.save(relation);
            log.debug("Friendship between subscriber: '{}' and interest: '{}' with uuid: '{}' was updated successfully.", subscriberUuid,
                interestUuid, updatedRelation.getUuid());
            return Optional.of(updatedRelation);
        }
        return optionalRelation;
    }

    @Transactional
    public Optional<Relations> removeFromFriend(UUID userUuid1, UUID userUuid2) {
        var optionalRelation = relationsRepo.findBySubscriberAndInterestUuids(userUuid1, userUuid2);
        if (optionalRelation.isEmpty()){
            optionalRelation = relationsRepo.findBySubscriberAndInterestUuids(userUuid2, userUuid1);
        }
        if (optionalRelation.isPresent()){
            var relation = optionalRelation.get();
            relation.setRelationsStatus(RelationsStatus.DECLINED);
            log.debug("Friendship between '{}' and '{}' with uuid: '{}' was removed.", userUuid1, userUuid2, relation.getUuid());
            return Optional.of(relation);
        }
        return optionalRelation;
    }

    public Optional<List<UUID>> getFriends(UUID userUuid) {
        Optional<List<UUID>> interestUuidsBySubscriberUuid = relationsRepo.findInterestUuidsBySubscriberUuid(userUuid, RelationsStatus.FRIEND);
        Optional<List<UUID>> subscriberUuidsByInterestUuid = relationsRepo.findSubscriberUuidsByInterestUuid(userUuid, RelationsStatus.FRIEND);
        List<UUID> friends = new ArrayList<>();
        interestUuidsBySubscriberUuid.ifPresent(friends::addAll);
        subscriberUuidsByInterestUuid.ifPresent(friends::addAll);
        if (friends.isEmpty()){
            log.debug("No friends for user: '{}'.", userUuid);
            return interestUuidsBySubscriberUuid;
        }
        log.debug("Friends for user: '{}' was got.", userUuid);
        return Optional.of(friends);
    }

    @Transactional
    public Relations follow(UUID subscriberUuid, UUID interestUuid) {
        var optionalRelation = relationsRepo.findBySubscriberAndInterestUuids(subscriberUuid, interestUuid);
        if (optionalRelation.isPresent()){
            var relation = optionalRelation.get();
            if(relation.getRelationsStatus().equals(RelationsStatus.DECLINED)){
                relation.setRelationsStatus(RelationsStatus.FOLLOWER);
                relationsRepo.save(relation);
            }
            return relation;
        }
        Relations savedRelation = relationsRepo.save(Relations
            .builder()
            .subscriberUuid(subscriberUuid)
            .interestUuid(interestUuid)
            .relationsStatus(RelationsStatus.FOLLOWER)
            .build());
        log.debug("Request for follow between subscriber: '{}' and interest: '{}' saved with uuid: '{}'.", subscriberUuid, interestUuid,
            savedRelation.getUuid());
        return savedRelation;
    }
}
