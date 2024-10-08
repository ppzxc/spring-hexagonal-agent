package com.nanoit.agent.hexagonal.adapter.data.repository;

import com.nanoit.agent.hexagonal.adapter.data.entity.ShortMessageService;
import com.nanoit.agent.hexagonal.adapter.data.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShortMessageServiceRepository extends JpaRepository<ShortMessageService, String> {
    List<ShortMessageService> findByStatus(Status status);
}
