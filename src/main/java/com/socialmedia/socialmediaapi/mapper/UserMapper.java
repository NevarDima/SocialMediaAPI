package com.socialmedia.socialmediaapi.mapper;

import com.socialmedia.socialmediaapi.model.User;
import com.socialmedia.socialmediaapi.model.dto.UserDto;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto map (User userEntity);
    @InheritInverseConfiguration
    User map (UserDto userDto);
}
