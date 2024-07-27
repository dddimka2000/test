package com.CMS.kinoCMS.admin.services;

import com.CMS.kinoCMS.admin.repositories.UserRepository;
import com.CMS.kinoCMS.admin.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAllUsers(){
        return userRepository.findAll();
    }

    public Optional<User> findUserById(Long userId) {
        return userRepository.findById(userId);
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public void delete(User user) {
        userRepository.delete(user);
    }

    public List<User> findUsersByRole(String role){
        return userRepository.findAllByRole(role);
    }

    public long countByGender(String gender) {
        return userRepository.countByGender(gender);
    }
}