package com.taskflow.tms.clients;

import com.taskflow.tms.dtos.ProjectDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "project-service")
public interface ProjectServiceClient {
    
    @GetMapping("/api/projects/{id}")
    ProjectDTO getProjectById(@PathVariable("id") UUID id);
}
