package com.matchskills.ia.service.repositorys;

import com.matchskills.ia.service.entitys.SoftskillEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface SoftskillRepository extends MongoRepository<SoftskillEntity, String> {
    List<SoftskillEntity> findByNameIgnoreCaseIn(Set<String> name);
    Optional<SoftskillEntity> findByNameIgnoreCase(String name);
}