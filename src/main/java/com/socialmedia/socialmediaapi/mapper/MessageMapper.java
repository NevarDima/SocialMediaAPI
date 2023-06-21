package com.socialmedia.socialmediaapi.mapper;

import com.socialmedia.socialmediaapi.model.Message;
import com.socialmedia.socialmediaapi.model.dto.MessageDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface MessageMapper {

    MessageDto messageToDto(Message messageEntity);
    Message dtoToMessage(MessageDto messageDto);

    List<MessageDto> messagesToDtos(List<Message> messages);

}
