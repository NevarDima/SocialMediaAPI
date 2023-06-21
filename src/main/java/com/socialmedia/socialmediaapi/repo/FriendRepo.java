package com.socialmedia.socialmediaapi.repo;

import com.socialmedia.socialmediaapi.model.Friend;
import com.socialmedia.socialmediaapi.model.RelationsStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FriendRepo extends JpaRepository<Friend, UUID> {

    @Query("select r from Friend r where r.subscriberUuid = ?1 and r.interestUuid = ?2")
    Optional<Friend> findBySubscriberAndInterestUuids(UUID subscriberUuid, UUID interestUuid);
    @Query("select r.interestUuid from Friend r where r.subscriberUuid = ?1 and r.relationsStatus = ?2")
    Optional<List<UUID>> findInterestUuidsBySubscriberUuid(UUID userUuid, RelationsStatus status);
    @Query("select r.subscriberUuid from Friend r where r.interestUuid = ?1 and r.relationsStatus = ?2")
    Optional<List<UUID>> findSubscriberUuidsByInterestUuid(UUID userUuid, RelationsStatus status);
}
