package com.epam.learn.resource_service.dao;

import com.epam.learn.resource_service.model.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, Integer> {
}