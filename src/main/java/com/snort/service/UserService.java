package com.snort.service;

import com.snort.entities.User;
import com.snort.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    public List<User> findAllNonAdminUsers() {
        List<User> userList = userRepository.findAll();
        List<User> nonAdminUsers = userList.stream()
                .filter(user -> !user.getRole().equals("ROLE_ADMIN"))
                .collect(Collectors.toList());
        return nonAdminUsers;
    }
}
