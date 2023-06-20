package com.socialmedia.socialmediaapi.mapper;

import com.socialmedia.socialmediaapi.model.Follower;
import com.socialmedia.socialmediaapi.model.Friend;
import com.socialmedia.socialmediaapi.model.dto.RelationsDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RelationsMapper {
    RelationsDto friendToRelation(Friend friendEntity);
    Friend relationToFriend(RelationsDto relationsDto);

    RelationsDto followerToRelation(Follower followerEntity);
    Follower relationToFollower(RelationsDto relationsDto);
}
