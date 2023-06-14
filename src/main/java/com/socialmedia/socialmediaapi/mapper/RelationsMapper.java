package com.socialmedia.socialmediaapi.mapper;

import com.socialmedia.socialmediaapi.model.Relations;
import com.socialmedia.socialmediaapi.model.dto.RelationsDto;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RelationsMapper {
    RelationsDto map(Relations relationsEntity);
    @InheritInverseConfiguration
    Relations map(RelationsDto relationsDto);
}
