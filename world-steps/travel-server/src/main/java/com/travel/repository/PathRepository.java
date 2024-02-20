package com.travel.repository;

import com.travel.entity.Path;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;


public interface PathRepository extends MongoRepository<Path, String> {
    // You can define custom query methods here if needed
    Optional<Path> findById(String id);
    Page<Path> findByUserId(String userId, Pageable pageable);
}
