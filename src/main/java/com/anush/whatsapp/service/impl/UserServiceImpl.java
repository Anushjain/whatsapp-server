package com.anush.whatsapp.service.impl;

import com.anush.whatsapp.domain.User;
import com.anush.whatsapp.repos.UserRepository;
import com.anush.whatsapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;
    @Override
    public User createUser(User user) {
        userRepository.save(user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        userRepository.save(user);
        return user;
    }


    @Override
    public Optional<User> getUserById(UUID id) {

        return userRepository.findById(id);
    }

    @Override
    public Page<User> searchUser(String name, int page, int size) {
        return userRepository.findByUsernameContaining(name, PageRequest.of(page, size));
    }
}
