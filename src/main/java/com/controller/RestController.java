package com.controller;

import com.model.Role;
import com.model.User;
import com.service.RoleServiceImpl;
import com.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("users")
@CrossOrigin
public class RestController {

    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private RoleServiceImpl roleService;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @GetMapping
    public List<User> getAllUsers() {
        return userService.allUsersList();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PostMapping
    public void registrationUser(@RequestBody User user) {
        Set<Role> roles = new HashSet<>();
        roles.add(roleService.getRoleById(1L));
        String codepass = passwordEncoder.encode(user.getUserpassword());
        user.setUserpassword(codepass);
        user.setRoles(roles);
        userService.addUser(user);
    }

    @PutMapping("/{roleId}")
    public void updateUser(@RequestBody User user, @PathVariable Long roleId) {
        Set<Role> roles = new HashSet<>();
        roles.add(roleService.getRoleById(roleId));
        User updUser = userService.getUserById(user.getId());
        updUser.setRoles(roles);
        updUser.setUsername(user.getUsername());
        if(updUser.getUserpassword().equals(user.getUserpassword())) {

        } else {
            updUser.setUserpassword(passwordEncoder.encode(user.getUserpassword()));
        }
        userService.updateUser(updUser);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.removeUserById(id);
    }

}