package com.jobportal.service;

import com.jobportal.entity.Application;
import com.jobportal.entity.Job;
import com.jobportal.entity.User;
import com.jobportal.repository.ApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ApplicationService {

    @Autowired
    private ApplicationRepository applicationRepository;

    public Application applyForJob(Application application) {
        return applicationRepository.save(application);
    }

    public List<Application> getApplicationsByStudent(User student) {
        return applicationRepository.findByStudent(student);
    }

    public List<Application> getApplicantsByJob(Job job) {
        return applicationRepository.findByJob(job);
    }

    public Optional<Application> getApplicationById(Long id) {
        return applicationRepository.findById(id);
    }

    public Application updateApplicationStatus(Long id, String status) {
        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Application not found"));
        application.setStatus(status);
        return applicationRepository.save(application);
    }

    public boolean hasAlreadyApplied(User student, Job job) {
        return applicationRepository.existsByStudentAndJob(student, job);
    }
    
    public long countApplicants(Job job) {
        return applicationRepository.countByJob(job);
    }
    
    public long countByStatus(Job job, String status) {
        return applicationRepository.countByJobAndStatus(job, status);
    }
}
