package com.logistics.delivery.application.dto;

import com.logistics.delivery.domain.entity.Status;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class DeliveryUpdateReqDto {

    @NotNull
    private Status status;
}
