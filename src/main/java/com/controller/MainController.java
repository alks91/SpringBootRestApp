package com.controller;

import com.model.Role;
import com.model.User;
import com.service.RoleServiceImpl;
import com.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashSet;
import java.util.Set;

@Controller
public class MainController {
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private RoleServiceImpl roleService;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    //navigation

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String getLogin(Authentication authentication) {
        if(authentication != null) {
            return "redirect:/profile";
        }
        return "login";
    }

    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public String getProfile() {
        return "profile";
    }

    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public String getAdmin(Model model) {
        model.addAttribute("usersList", userService.allUsersList());
        return "admin";
    }

    //Registration
    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public String registration(@RequestParam(value = "username") String username, @RequestParam(value = "userpassword") String userpassword) {
        Set<Role> roles = new HashSet<>();
        roles.add(roleService.getRoleById(1L));
        String codepass = passwordEncoder.encode(userpassword);
        User user = new User(username, codepass);
        user.setRoles(roles);
        userService.addUser(user);
        return "redirect:/login";
    }

    @RequestMapping(value = "/registrationAdmin", method = RequestMethod.POST)
    public String registrationAdmin(@RequestParam(value = "username") String username,
                                    @RequestParam(value = "userpassword") String userpassword,
                                    @RequestParam(value = "roleName") String roleName) {
        Set<Role> roles = new HashSet<>();
        if(roleName.equals("ROLE_USER")) {
            roles.add(roleService.getRoleById(1L));
        } else {
            roles.add(roleService.getRoleById(2L));
        }
        String codepass = passwordEncoder.encode(userpassword);
        User user = new User(username, codepass);
        user.setRoles(roles);
        userService.addUser(user);
        return "redirect:/admin";
    }

    //UpdateDelete


    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String commitUpdate(@RequestParam(value = "id") int id, @RequestParam(value = "username") String username,
                               @RequestParam(value = "userpassword") String userpassword, @RequestParam(value = "role") String roleName) {
        Set<Role> roles = new HashSet<>();
        User user = userService.getUserById((long) id);
        user.setUsername(username);
        if(user.getUserpassword().equals(userpassword)) {

        } else {
            user.setUserpassword(passwordEncoder.encode(userpassword));
        }
        if(roleName.equals("ROLE_USER")) {
            roles.add(roleService.getRoleById(1L));
        } else {
            roles.add(roleService.getRoleById(2L));
        }
        user.setRoles(roles);
        userService.updateUser(user);
        return "redirect:/admin";
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public String delete(@RequestParam(value = "id") int id) {
        userService.removeUserById((long) id);
        return "redirect:/admin";
    }
}
