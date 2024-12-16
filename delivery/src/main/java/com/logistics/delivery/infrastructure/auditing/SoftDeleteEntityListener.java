package com.logistics.delivery.infrastructure.auditing;

import com.logistics.delivery.domain.common.BaseEntity;
import jakarta.persistence.PreUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;

import java.time.LocalDateTime;

@RequiredArgsConstructor
public class SoftDeleteEntityListener {

    private final AuditorAware<String> auditorAware;

    // Entity가 update 되었을때 호출
    @PreUpdate
    public void softDeleteHandle(BaseEntity baseEntity) {

        // isDelete가 true이고 DeletedAt이 null일때.
        if (baseEntity.isDelete() && baseEntity.getDeletedAt() == null) {

            auditorAware.getCurrentAuditor().ifPresent(baseEntity::setDeletedBy);
            baseEntity.setDeletedAt(LocalDateTime.now());
        }

    }

}
