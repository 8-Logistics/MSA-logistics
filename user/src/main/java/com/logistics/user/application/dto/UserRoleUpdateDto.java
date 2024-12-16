package com.logistics.user.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

// 유저 허브담당자, 업체담당자 Role update API Request DTO
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleUpdateDto {

    private UUID sourceHubId;
    private UUID vendorId;

}
