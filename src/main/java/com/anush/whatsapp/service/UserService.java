package com.anush.whatsapp.service;

import com.anush.whatsapp.domain.User;
import org.springframework.data.domain.Page;

import java.util.Optional;
import java.util.UUID;


public interface UserService {

    public User createUser(User user);

    public User updateUser(User user);

    public Optional<User> getUserById(UUID id);

    public Page<User> searchUser(String name, int page, int size);
}
