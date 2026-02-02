package com.taskflow.tms.clients;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "ums-service", configuration = com.taskflow.tms.config.FeignConfig.class)
public interface UserServiceClient {

    @GetMapping("/api/users/{id}")
    UserResponse getUserById(@PathVariable("id") UUID id);

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    class UserResponse {
        private UUID id;
        private String name;
        private String email;
    }
}
