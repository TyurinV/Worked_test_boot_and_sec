package com.example.boot.controllers;

import com.example.boot.models.Role;
import com.example.boot.models.User;
import com.example.boot.service.impl.RoleServiceImpl;
import com.example.boot.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserServiceImpl userServiceImpl;
    private final RoleServiceImpl roleService;


    private PasswordEncoder passwordEncoder;

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public AdminController(UserServiceImpl userServiceImpl, RoleServiceImpl roleService) {
        this.userServiceImpl = userServiceImpl;
        this.roleService = roleService;
    }

    @GetMapping(value = "/")
    public String getAllUsers(Model model) {
        List<User> users = userServiceImpl.getAllUsers();
        model.addAttribute("userList", users);
        return "allusers";
    }

    @GetMapping(value = "/edit/{id}")
    public String editPage(@PathVariable Long id, Model model) {
        User user = userServiceImpl.getById(id);
        model.addAttribute("user", user);
        return "edit";
    }

    @PostMapping(value = "/edit")
    public void editUser(@ModelAttribute("user") User user, @RequestParam(required = false) String roleAdmin) { // массив айдишников
        Set<Role> roles = new HashSet<>();
        roles.add(roleService.getRoleByName("ADMIN"));
        if (roleAdmin != null) {
            roles.add(roleService.getRoleByName("USER"));
        }
        user.setRoles(roles);
        userServiceImpl.edit(user);

//        Set<Role> roles = new HashSet<>();
//        // едит юзеров и масссив
//        roles.add(roleService.getRoleById(2l));
//        if (roleAdmin != null) {
//            roles.add(roleService.getRoleById(1l));
//        }
//        user.setRoles(roles);
//        userService.edit(user);
    }

    @GetMapping("/add")
    public void addPage(Model model) {
        model.addAttribute("user", new User());
    }

    @PostMapping(value = "/add")
    public String addUser(@ModelAttribute("user") User user, @RequestParam(required = false) String roleAdmin) {
//        String hashedPassword = passwordEncoder.encode(user.getPassword());
        Set<Role> roles = new HashSet<>();
        roles.add(roleService.getRoleByName("ADMIN"));
        if (roleAdmin != null) {
            roles.add(roleService.getRoleByName("USER"));
        }
        user.setRoles(roles);
//        user.setPassword(hashedPassword);
        userServiceImpl.add(user);
        return "redirect:/admin/";
    }


    @GetMapping(value = "/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userServiceImpl.remove(id);
        return "redirect:/admin/";
    }
}