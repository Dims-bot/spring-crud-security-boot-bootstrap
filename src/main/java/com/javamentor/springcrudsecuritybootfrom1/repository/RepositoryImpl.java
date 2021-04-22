package com.javamentor.springcrudsecuritybootfrom1.repository;

import com.javamentor.springcrudsecuritybootfrom1.Model.ERole;
import com.javamentor.springcrudsecuritybootfrom1.Model.Role;
import com.javamentor.springcrudsecuritybootfrom1.Model.User;
import com.javamentor.springcrudsecuritybootfrom1.transferObject.NewUserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
public class RepositoryImpl {

    @PersistenceContext
    private EntityManager em;

    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder;

    private RoleRepository roleRepository;

    @Autowired
    public RepositoryImpl(UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    public List<User> getAllUsers() {
        List<User> users = userRepository.getAllUsers();
        return users;
    }

    public User getUserById(Long id) {

        User user = userRepository.getOne(id);

        return user;
    }

    public void save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public void savePlusRoles(NewUserRequest newUserRequest) {
        newUserRequest.setPassword(passwordEncoder.encode(newUserRequest.getPassword()));

        User user = new User(newUserRequest.getUsername(),
                newUserRequest.getPassword(),
                newUserRequest.getFirstName(),
                newUserRequest.getLastName(),
                newUserRequest.getAge());

        String rolesForSave = newUserRequest.getRoles();
        Set<Role> roles = new HashSet<>();

        //rolesForSave.forEach(r -> {
        if ("ADMIN".equals(rolesForSave)) {
            Role adminRole = roleRepository
                    .findByRole(ERole.ROLE_ADMIN)
                    .orElseThrow(() -> new RuntimeException("Error, Role ADMIN is not found"));
            roles.add(adminRole);
        } else {
            Role userRole = roleRepository
                    .findByRole(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error, Role USER is not found"));
            roles.add(userRole);
        }
        user.setRoles(roles);
        //user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setPassword(newUserRequest.getPassword());

        userRepository.save(user);
    }

    public void updateUser(Long id, NewUserRequest updatedUser) {
        User userToBeUpdated = userRepository.getOne(id);

        userToBeUpdated.setFirstName(updatedUser.getFirstName());
        userToBeUpdated.setUsername(updatedUser.getUsername());
        userToBeUpdated.setLastName(updatedUser.getLastName());
        userToBeUpdated.setAge(updatedUser.getAge());

        String rolesForSave = updatedUser.getRoles();
        Set<Role> roles = new HashSet<>();

        if ("ADMIN".equals(rolesForSave)) {
            Role adminRole = roleRepository
                    .findByRole(ERole.ROLE_ADMIN)
                    .orElseThrow(() -> new RuntimeException("Error, Role ADMIN is not found"));
            roles.add(adminRole);
            userToBeUpdated.setRoles(roles);
        } if ("USER".equals(rolesForSave)){
            Role userRole = roleRepository
                    .findByRole(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error, Role USER is not found"));
            roles.add(userRole);
            userToBeUpdated.setRoles(roles);
        }

        if ((updatedUser.getPassword()).length() > 1) {
            userToBeUpdated.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }
        userRepository.save(userToBeUpdated);
    }

    public void deleteUser(Long id) {

        userRepository.deleteById(id);
    }


}
