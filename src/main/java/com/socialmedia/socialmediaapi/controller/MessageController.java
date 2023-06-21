package com.socialmedia.socialmediaapi.controller;

import com.socialmedia.socialmediaapi.mapper.MessageMapper;
import com.socialmedia.socialmediaapi.model.User;
import com.socialmedia.socialmediaapi.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/v1/message")
@RequiredArgsConstructor
@Slf4j
public class MessageController {

    private final MessageService messageService;
    private final MessageMapper messageMapper;

    @PostMapping(value = "/")
    public ResponseEntity<?> createMessage(@AuthenticationPrincipal User user, @RequestBody Map<String, Object> messageMap){
        var message = messageService.saveMessage(
            user.getUuid(),
            UUID.fromString(messageMap.get("toUuid").toString()),
            messageMap.get("messageText").toString()
        );
        log.debug("Message from: '{}' to: '{}' was saved", user.getUuid(), messageMap.get("toUuid"));
        return new ResponseEntity<>(messageMapper.messageToDto(message), HttpStatus.OK);
    }

    @GetMapping(value = "/{receiverUuid}")
    public ResponseEntity<?> getChatMessages(@AuthenticationPrincipal User user, @PathVariable UUID receiverUuid){
        var messages = messageService.getChatMessages(user.getUuid(), receiverUuid);
        log.debug("Messages betweeb sender: '{}' and receiver: '{}' were got", user.getUuid(), receiverUuid);
        return new ResponseEntity<>(messageMapper.messagesToDtos(messages), HttpStatus.OK);
    }

    @PostMapping(value = "/{messageUuid}")
    public ResponseEntity<?> updateMessageById (@AuthenticationPrincipal User user,
                                                @PathVariable UUID messageUuid,
                                                @RequestBody String messageText){
        var updatedMessage = messageService.updateMessageById(user.getUuid(), messageUuid, messageText);
        log.debug("Message with uuid: '{}' updated.", updatedMessage.getUuid());
        return new ResponseEntity<>(messageMapper.messageToDto(updatedMessage), HttpStatus.OK);
    }

        @DeleteMapping(value = "/{messageUuid}")
        public ResponseEntity<?> deletePostById(@AuthenticationPrincipal User user, @PathVariable UUID messageUuid){
            var deleteMessage = messageService.deleteMessageById(user.getUuid(), messageUuid);
            log.debug("Message with uuid: '{}' deleted.", deleteMessage.getUuid());
            return new ResponseEntity<>(messageMapper.messageToDto(deleteMessage), HttpStatus.OK);
        }

}
