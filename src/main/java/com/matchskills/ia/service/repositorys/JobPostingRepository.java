package com.matchskills.ia.service.repositorys;

import com.matchskills.ia.service.entitys.JobPostingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobPostingRepository extends JpaRepository<JobPostingEntity, Long> {

}
