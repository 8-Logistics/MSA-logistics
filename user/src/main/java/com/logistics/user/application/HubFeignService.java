package com.logistics.user.application;

import java.util.UUID;

public interface HubFeignService {

    boolean checkHub(UUID hubId);

}
