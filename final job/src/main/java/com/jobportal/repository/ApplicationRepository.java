package com.jobportal.repository;

import com.jobportal.entity.Application;
import com.jobportal.entity.Job;
import com.jobportal.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findByStudent(User student);
    List<Application> findByJob(Job job);
    Optional<Application> findByStudentAndJob(User student, Job job);
    boolean existsByStudentAndJob(User student, Job job);
    
    long countByJob(Job job);
    long countByJobAndStatus(Job job, String status);
}
