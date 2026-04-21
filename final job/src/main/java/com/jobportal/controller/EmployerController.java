package com.jobportal.controller;

import com.jobportal.config.CustomUserDetails;
import com.jobportal.entity.Application;
import com.jobportal.entity.Job;
import com.jobportal.entity.User;
import com.jobportal.service.ApplicationService;
import com.jobportal.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/employer")
public class EmployerController {

    @Autowired
    private JobService jobService;

    @Autowired
    private ApplicationService applicationService;

    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        User employer = userDetails.getUser();
        List<Job> jobs = jobService.getJobsByEmployer(employer);
        model.addAttribute("jobs", jobs);
        model.addAttribute("totalJobs", jobs.size());
        
        long totalApplicants = 0;
        for (Job job : jobs) {
            totalApplicants += applicationService.countApplicants(job);
        }
        model.addAttribute("totalApplicants", totalApplicants);
        return "employer-dashboard";
    }

    @GetMapping("/post-job")
    public String postJobForm(Model model) {
        model.addAttribute("job", new Job());
        return "post-job";
    }

    @PostMapping("/post-job")
    public String saveJob(@AuthenticationPrincipal CustomUserDetails userDetails, 
                          @ModelAttribute Job job) {
        job.setEmployer(userDetails.getUser());
        jobService.postJob(job);
        return "redirect:/employer/dashboard?job_posted=true";
    }

    @GetMapping("/edit-job/{id}")
    public String editJobForm(@PathVariable Long id, Model model) {
        Job job = jobService.getJobById(id).orElseThrow();
        model.addAttribute("job", job);
        return "edit-job";
    }

    @PostMapping("/edit-job/{id}")
    public String updateJob(@PathVariable Long id, @ModelAttribute Job updatedJob) {
        Job job = jobService.getJobById(id).orElseThrow();
        job.setTitle(updatedJob.getTitle());
        job.setDescription(updatedJob.getDescription());
        job.setSkillsRequired(updatedJob.getSkillsRequired());
        job.setLocation(updatedJob.getLocation());
        job.setSalary(updatedJob.getSalary());
        jobService.postJob(job);
        return "redirect:/employer/dashboard?job_updated=true";
    }

    @GetMapping("/delete-job/{id}")
    public String deleteJob(@PathVariable Long id) {
        jobService.deleteJob(id);
        return "redirect:/employer/dashboard?job_deleted=true";
    }

    @GetMapping("/applicants/{jobId}")
    public String viewApplicants(@PathVariable Long jobId, Model model) {
        Job job = jobService.getJobById(jobId).orElseThrow();
        List<Application> applicants = applicationService.getApplicantsByJob(job);
        model.addAttribute("job", job);
        model.addAttribute("applicants", applicants);
        return "applicants";
    }

    @PostMapping("/application/status")
    public String updateStatus(@RequestParam Long applicationId, 
                               @RequestParam String status, 
                               @RequestParam Long jobId) {
        applicationService.updateApplicationStatus(applicationId, status);
        return "redirect:/employer/applicants/" + jobId + "?status_updated=true";
    }
}
