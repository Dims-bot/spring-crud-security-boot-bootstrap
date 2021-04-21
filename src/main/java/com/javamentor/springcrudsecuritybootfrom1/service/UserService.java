package com.javamentor.springcrudsecuritybootfrom1.service;

import com.javamentor.springcrudsecuritybootfrom1.Model.User;
import com.javamentor.springcrudsecuritybootfrom1.transferObject.NewUserRequest;

import java.util.List;

public interface UserService {
    public List<User> getAllUsers();

    public User getUserById(Long id);

    public User getUserByUsername(String username);

    public void save(User user);

    public void updateUser(Long id, NewUserRequest updatedUser);

    public void deleteUser(Long id);

    public void save(NewUserRequest newUserRequest);
}
