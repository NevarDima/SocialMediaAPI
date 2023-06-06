package com.socialmedia.socialmediaapi.mapper;

import com.socialmedia.socialmediaapi.model.Post;
import com.socialmedia.socialmediaapi.model.dto.PostDto;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PostMapper {

    PostDto map(Post postEntity);
    @InheritInverseConfiguration
    Post map(PostDto postDto);

}
