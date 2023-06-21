package com.socialmedia.socialmediaapi.service;

import com.socialmedia.socialmediaapi.exception.MessageNotFoundException;
import com.socialmedia.socialmediaapi.model.Message;
import com.socialmedia.socialmediaapi.repo.MessageRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepo messageRepo;

    @Transactional
    public Message saveMessage(UUID fromUuid, UUID toUuid, String messageText) {
        Message savedMessage = messageRepo.save(Message
            .builder()
            .fromUuid(fromUuid)
            .toUuid(toUuid)
            .messageText(messageText)
            .createdAt(LocalDateTime.now())
            .build());
        log.debug("Message with uuid: '{}' saved.", savedMessage.getUuid());
        return savedMessage;
    }

    public List<Message> getChatMessages(UUID senderUuid, UUID receiverUuid) {
        List<Message> result = new ArrayList<>();
        var senderMessages = messageRepo.findBySenderAndReceiver(senderUuid, receiverUuid);
        var receiverMessages = messageRepo.findBySenderAndReceiver(receiverUuid, senderUuid);
        result.addAll(senderMessages);
        result.addAll(receiverMessages);
        if(result.isEmpty()){
            log.debug("No messages between sender: '{}' and receiver: '{}'", senderUuid, receiverUuid);
            return Collections.emptyList();
        }
        Collections.sort(result);
        log.debug("Sorted by 'createdAt' list of messages between sender: '{}' and receiver: '{}'", senderUuid, receiverUuid);
        return result;
    }

    @Transactional
    public Message updateMessageById(UUID senderUuid, UUID messageUuid, String messageText) {
        var optionalMessage = messageRepo.findByUuidAndSenderUuid(messageUuid, senderUuid);
        if(optionalMessage.isEmpty()){
            log.error("Message with uuid: '{}' doesn't exist.", messageUuid);
            throw new MessageNotFoundException();
        }
        var message = optionalMessage.get();
        message.setMessageText(messageText);
        message.setUpdatedAt(LocalDateTime.now());
        log.debug("Message: '{}' apdated", message.getUuid());
        return messageRepo.save(message);
    }

    @Transactional
    public Message deleteMessageById(UUID senderUuid, UUID messageUuid) {
        var optionalMessage = messageRepo.findByUuidAndSenderUuid(messageUuid, senderUuid);
        if(optionalMessage.isEmpty()){
            log.error("Message with uuid: '{}' doesn't exist.", messageUuid);
            throw new MessageNotFoundException();
        }
        var message = optionalMessage.get();
        messageRepo.delete(message);
        log.debug("Message: '{}' deleted.", message.getUuid());
        return message;
    }
}
