package com.socialmedia.socialmediaapi.repo;

import com.socialmedia.socialmediaapi.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MessageRepo extends JpaRepository<Message, UUID> {

    @Query("select m from Message m where m.fromUuid = ?1 and m.toUuid = ?2")
    List<Message> findBySenderAndReceiver(UUID senderUuid, UUID receiverUuid);

    @Query("select m from Message m where m.uuid = ?1 and m.fromUuid = ?2")
    Optional<Message> findByUuidAndSenderUuid(UUID messageUuid, UUID senderUuid);
}
