package com.jobportal.controller;

import com.jobportal.service.JobService;
import com.jobportal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private JobService jobService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("jobs", jobService.getAllJobs());
        model.addAttribute("totalUsers", userService.getAllUsers().size());
        model.addAttribute("totalJobs", jobService.getAllJobs().size());
        return "admin-dashboard";
    }
}
