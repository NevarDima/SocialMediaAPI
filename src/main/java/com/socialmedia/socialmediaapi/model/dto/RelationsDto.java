package com.socialmedia.socialmediaapi.model.dto;

import com.socialmedia.socialmediaapi.model.RelationsStatus;
import lombok.Data;

import java.util.UUID;

@Data
public class RelationsDto {
    private UUID uuid;
    private UUID subscriberUuid;
    private UUID interestUuid;
    private RelationsStatus relationsStatus;
}
