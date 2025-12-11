package com.lms.booktrack.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lms.booktrack.model.User;
import com.lms.booktrack.service.AuthService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/auth")
public class AuthController {


    @Autowired
    private AuthService authService;

    @GetMapping("/register")
    public String registerUser(Model model) {
        model.addAttribute("user", new User());
        return "registration/register";
    }

    @PostMapping("/register")
    public String addBook(@Valid @ModelAttribute("user") User user,
                          BindingResult result,
                          Model model,
                          RedirectAttributes redirectAttrs) {

        System.out.println("In addBook method ...");
                      
        System.out.println(result.getAllErrors());
        if (result.hasErrors()) {
            return "registration/register";
        }
        
        authService.registerUser(user);
        System.out.println("User registered: " + user.getName());

        return "redirect:/";
    }

    @GetMapping("/login")
    public String login() {
        return "registration/admin_login";
    }

}
