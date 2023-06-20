package com.socialmedia.socialmediaapi.repo;

import com.socialmedia.socialmediaapi.model.Follower;
import com.socialmedia.socialmediaapi.model.RelationsStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FollowerRepo extends JpaRepository<Follower, UUID> {
    @Query("select r from Follower r where r.subscriberUuid = ?1 and r.interestUuid = ?2")
    Optional<Follower> findBySubscriberAndInterestUuids(UUID subscriberUuid, UUID interestUuid);
    @Query("select r.interestUuid from Follower r where r.subscriberUuid = ?1 and r.relationsStatus = ?2")
    Optional<List<UUID>> findInterestUuidsBySubscriberUuid(UUID userUuid, RelationsStatus status);
    @Query("select r.subscriberUuid from Follower r where r.interestUuid = ?1 and r.relationsStatus = ?2")
    Optional<List<UUID>> findSubscriberUuidsByInterestUuid(UUID userUuid, RelationsStatus status);
}
