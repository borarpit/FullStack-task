package com.jobportal.controller;

import com.jobportal.config.CustomUserDetails;
import com.jobportal.entity.Application;
import com.jobportal.entity.Job;
import com.jobportal.entity.User;
import com.jobportal.service.ApplicationService;
import com.jobportal.service.JobService;
import com.jobportal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;

@Controller
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private JobService jobService;

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private UserService userService;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        User student = userDetails.getUser();
        model.addAttribute("user", student);
        model.addAttribute("applications", applicationService.getApplicationsByStudent(student));
        return "student-dashboard";
    }

    @GetMapping("/jobs")
    public String viewJobs(@RequestParam(required = false) String title,
                           @RequestParam(required = false) String skill,
                           @RequestParam(required = false) String location,
                           Model model) {
        List<Job> jobs;
        if (title != null || skill != null || location != null) {
            jobs = jobService.searchJobs(title, skill, location);
        } else {
            jobs = jobService.getAllJobs();
        }
        model.addAttribute("jobs", jobs);
        return "jobs";
    }

    @PostMapping("/apply/{jobId}")
    public String applyForJob(@PathVariable Long jobId, 
                              @AuthenticationPrincipal CustomUserDetails userDetails,
                              Model model) {
        User student = userDetails.getUser();
        Job job = jobService.getJobById(jobId).orElseThrow();

        if (applicationService.hasAlreadyApplied(student, job)) {
            return "redirect:/student/jobs?error=already_applied";
        }

        Application application = new Application();
        application.setStudent(student);
        application.setJob(job);
        applicationService.applyForJob(application);

        return "redirect:/student/dashboard?applied=true";
    }

    @GetMapping("/profile")
    public String editProfile(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        model.addAttribute("user", userDetails.getUser());
        return "student-profile";
    }

    @PostMapping("/profile/update")
    public String updateProfile(@AuthenticationPrincipal CustomUserDetails userDetails,
                                @ModelAttribute User updatedUser,
                                @RequestParam("resume") MultipartFile resume) throws IOException {
        User student = userService.findByEmail(userDetails.getUsername()).orElseThrow();
        student.setName(updatedUser.getName());
        student.setSkills(updatedUser.getSkills());
        student.setLocation(updatedUser.getLocation());

        if (!resume.isEmpty()) {
            Path path = Paths.get(uploadDir);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }
            String fileName = System.currentTimeMillis() + "_" + resume.getOriginalFilename();
            Path filePath = path.resolve(fileName);
            Files.copy(resume.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            student.setResumePath(fileName);
        }

        userService.updateUser(student);
        return "redirect:/student/dashboard?profile_updated=true";
    }
    
    @GetMapping("/applications")
    public String viewApplications(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        model.addAttribute("applications", applicationService.getApplicationsByStudent(userDetails.getUser()));
        return "my-applications";
    }
}
