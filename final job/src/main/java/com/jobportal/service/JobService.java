package com.jobportal.service;

import com.jobportal.entity.Job;
import com.jobportal.entity.User;
import com.jobportal.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class JobService {

    @Autowired
    private JobRepository jobRepository;

    public Job postJob(Job job) {
        return jobRepository.save(job);
    }

    public List<Job> getJobsByEmployer(User employer) {
        return jobRepository.findByEmployer(employer);
    }

    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    public Optional<Job> getJobById(Long id) {
        return jobRepository.findById(id);
    }

    public void deleteJob(Long id) {
        jobRepository.deleteById(id);
    }

    public List<Job> searchJobs(String title, String skill, String location) {
        return jobRepository.searchJobs(title, skill, location);
    }
}
