package com.jobportal.repository;

import com.jobportal.entity.Job;
import com.jobportal.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface JobRepository extends JpaRepository<Job, Long> {
    List<Job> findByEmployer(User employer);

    @Query("SELECT j FROM Job j WHERE " +
           "(:title IS NULL OR LOWER(j.title) LIKE LOWER(CONCAT('%', :title, '%'))) AND " +
           "(:skill IS NULL OR LOWER(j.skillsRequired) LIKE LOWER(CONCAT('%', :skill, '%'))) AND " +
           "(:location IS NULL OR LOWER(j.location) LIKE LOWER(CONCAT('%', :location, '%')))")
    List<Job> searchJobs(@Param("title") String title, 
                        @Param("skill") String skill, 
                        @Param("location") String location);
}
