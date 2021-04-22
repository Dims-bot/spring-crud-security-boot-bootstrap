package com.javamentor.springcrudsecuritybootfrom1.controllers;

import com.javamentor.springcrudsecuritybootfrom1.Model.User;
import com.javamentor.springcrudsecuritybootfrom1.service.UserServiceImpl;
import com.javamentor.springcrudsecuritybootfrom1.transferObject.NewUserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@Controller
public class UserController {

    private UserServiceImpl userService;

    @Autowired
    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping("/login-error")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);

        return "login";
    }
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/users/admin")
    public String getAllUsers(ModelMap modelMap,Principal principal) {
        User user = userService.getUserByUsername(principal.getName());

        modelMap.addAttribute("users", userService.getAllUsers());
        modelMap.addAttribute("principalUsername", principal.getName());
        modelMap.addAttribute("user", userService.getUserByUsername(principal.getName()));
        modelMap.addAttribute("userRolesPrefixFree", user.getUserRolesPrefixFree());


        return "getAllUsers";
    }

    @GetMapping("/users/admin/new")
    public String newUser(Model model, Principal principal) {
        model.addAttribute("user", new User());
        model.addAttribute("principalUsername", principal.getName());
        //model.addAttribute("userPrincipal", userService.getUserByUsername(principal.getName()));
        model.addAttribute("roles", userService.getUserByUsername(principal.getName()).getUserRolesPrefixFree());

        return "newUser";
    }

    @PatchMapping("/users/admin/{id}")
    public String updateUserById(@ModelAttribute("user") @Valid  NewUserRequest user,
                                 BindingResult bindingResult, @PathVariable("id") Long id) {
        if (bindingResult.hasErrors())
            return "redirect:/users/admin";

        userService.updateUser(id, user);
        return "redirect:/users/admin";
    }

    @GetMapping("/users/admin/{id}/edit")
    public String editUserById(Model model, @PathVariable("id") Long id) {
        model.addAttribute("user", userService.getUserById(id));
        return "edit";
    }

    @GetMapping("/users/admin/{id}")
    public String getUserById(@PathVariable("id") Long id, Model model) {
        model.addAttribute("user", userService.getUserById(id));

        return "getUserByID";
    }



    @PostMapping("/users/admin")
    public String createNewUser(@ModelAttribute("user") @Valid NewUserRequest newUserRequest,
                                BindingResult bindingResult) {
        if (bindingResult.hasErrors())

           return "newUser";
        //return "redirect:/users/admin/new";
            userService.save(newUserRequest);

        return "redirect:/users/admin";
    }


    @GetMapping("/users/user")
    public String getUserByLogin(ModelMap modelMap, Principal principal) {
        User user = userService.getUserByUsername(principal.getName());

        modelMap.addAttribute("user", userService.getUserByUsername(principal.getName()));
        modelMap.addAttribute("userRolesPrefixFree", user.getUserRolesPrefixFree());
        modelMap.addAttribute("principalUsername", principal.getName());

        return "user";
    }
    @DeleteMapping("/users/admin/{id}")
    public String deleteUserById(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return "redirect:/users/admin";
    }



}
