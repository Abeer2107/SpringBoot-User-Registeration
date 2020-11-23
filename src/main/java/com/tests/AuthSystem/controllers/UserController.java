/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tests.AuthSystem.controllers;

import com.tests.AuthSystem.entities.User;
import com.tests.AuthSystem.repos.InputValidator;
import com.tests.AuthSystem.repos.UserRepository;
import java.util.List;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author abeer
 */
@Controller
public class UserController {

    @Autowired
    UserRepository userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @RequestMapping(value = {"/", "/home"})
    public String home() {
        return "home";
    }

    @RequestMapping("/signup")
    public String signup(Model model) {
        return "signup";
    }

    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/profile")
    public String profile(Model model, HttpSession session) {
        if (session.getAttribute("email") != null) {
            User currentUser = userRepo.findByEmail(session.getAttribute("email").toString());
            model.addAttribute("UserFirstName", currentUser.getFirstName());
            model.addAttribute("UserLastName", currentUser.getLastName());
            model.addAttribute("UserEmail", currentUser.getEmail());
            return "profile";
        } else {
            return "redirect:/login";
        }
    }

    @RequestMapping("/profileEdit")
    public String editProfile(Model model, HttpSession session) {
        if (session.getAttribute("email") != null) {
            User currentUser = userRepo.findByEmail(session.getAttribute("email").toString());
            model.addAttribute("UserFirstName", currentUser.getFirstName());
            model.addAttribute("UserLastName", currentUser.getLastName());
            return "profile_edit";
        } else {
            return "redirect:/login";
        }
    }

    @RequestMapping("/passEdit")
    public String editPassword(HttpSession session) {
        if (session.getAttribute("email") != null) {
            return "profile_pass";
        } else {
            return "redirect:/login";
        }
    }

    //----------------------------------------------------------------------------------------------//
    @PostMapping("/addUser")
    public String addUser(Model model, @Valid User user) {

        //Input validation
        boolean hasErrors = false;
        if (!InputValidator.validName(user.getFirstName()) || !InputValidator.validName(user.getLastName())) {
            model.addAttribute("errorMessage", "Please enter a valid name");
            hasErrors = true;
        }
        if (!InputValidator.validEmail(user.getEmail())) {
            model.addAttribute("errorMessage", "Please enter a valid email");
            hasErrors = true;
        }
        if (!InputValidator.validPassword(user.getPassword())) {
            model.addAttribute("errorMessage", "Please enter a password with at least 8 characters including at least a number");
            hasErrors = true;
        }
        List<User> list = userRepo.findAllByEmail(user.getEmail());
        if (!list.isEmpty() && !hasErrors) {
            model.addAttribute("errorMessage", "Oops! The email provided is already registered");
            hasErrors = true;
        }

        if (!hasErrors) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepo.save(user); //to db
            model.addAttribute("errorMessage", "Signed up successfully, please login");
            return "login";
        } else {
            return "signup";
        }
    }

    @PostMapping("/editUser")
    public String editUser(@RequestParam("firstName") String fname, @RequestParam("lastName") String lname, Model model, HttpSession session) {

        //Input validation
        boolean hasErrors = false;
        if (!InputValidator.validName(fname) || !InputValidator.validName(lname)) {
            model.addAttribute("errorMessage", "Please enter a valid name");
            hasErrors = true;
        }

        if (!hasErrors) {
            userRepo.editUserProfile(fname, lname, session.getAttribute("email").toString()); //db
            model.addAttribute("errorMessage", "Data updated");
            return "redirect:/profile";
        } else {
            return "profile_edit";
        }
    }

    @PostMapping("/editPass")
    public String editPass(@RequestParam("currPass") String currPass, @RequestParam("newPass") String newPass, @RequestParam("newPassConfirm") String newPassConfirm, Model model, HttpSession session) {
        User user = userRepo.findByEmail(session.getAttribute("email").toString());
        boolean hasErrors = false;
        if (!passwordEncoder.matches(currPass, user.getPassword())) {
            model.addAttribute("errorMessage", "Incorrect password");
            hasErrors = true;
        }
        if (!InputValidator.validPassword(newPass)) {
            model.addAttribute("errorMessage", "Please enter a password with at least 8 characters including at least a number");
            hasErrors = true;
        }
        if (!newPass.equals(newPassConfirm)) {
            model.addAttribute("errorMessage", "New password and confirmed password do not match");
            hasErrors = true;
        }
        if (currPass.equals(newPass)) {
            model.addAttribute("errorMessage", "Please enter a different password");
            hasErrors = true;
        }

        if (!hasErrors) {
            userRepo.editUserPass(passwordEncoder.encode(newPass), user.getEmail());
            model.addAttribute("errorMessage", "Password updated - please login again");
            return "login";
        } else {
            return "profile_pass";
        }
    }

    @PostMapping("/login")
    public String login_user(@RequestParam("email") String email, @RequestParam("password") String password, HttpSession session, Model model) {

        User user = userRepo.findByEmail(email);

        if (user != null) {
            if (email.equalsIgnoreCase(user.getEmail()) && passwordEncoder.matches(password, user.getPassword())) {

                session.setAttribute("email", email);
                return "redirect:/profile";

            } else {
                model.addAttribute("errorMessage", "Invalid account");
                return "login";
            }
        } else {
            model.addAttribute("errorMessage", "Invalid account");
            return "login";
        }

    }

    @GetMapping(value = "/logout")
    public String logout_user(HttpSession session) {
        session.removeAttribute("email");
        session.invalidate();
        return "redirect:/login";
    }

}
